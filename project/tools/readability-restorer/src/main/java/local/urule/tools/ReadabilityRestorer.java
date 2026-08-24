package local.urule.tools;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.lang.model.SourceVersion;

/**
 * Restores names and documentation that are provable from exact-version Javadoc
 * or from JavaBean accessor structure. The tool deliberately skips ambiguous
 * transformations instead of guessing.
 */
public final class ReadabilityRestorer implements AutoCloseable {
    private static final Pattern DECOMPILED_NAME = Pattern.compile("(?:var\\d+(?:_\\d+)*|[a-z])");
    private static final Pattern SYNTHETIC_SOURCE = Pattern.compile(".*\\$(?:\\d+|[^/]*Context)\\.java");
    private static final Set<String> FIELD_INFERENCE_EXCLUSIONS = Set.of(
            "DynamicSpringConfigLoaderImpl",
            "FactManager",
            "KnowledgeSessionFactory",
            "RuleExecution",
            "Secret");

    private final Path sourceRoot;
    private final ZipFile javadocArchive;
    private final boolean apply;
    private final boolean verbose;
    private final boolean aliasesOnly;
    private final boolean restoreProtectedMethods;
    private final boolean methodsOnly;
    private final boolean restoreLocals;
    private final boolean restoreAllPlaceholders;
    private final boolean includeGenerated;
    private final boolean includeSynthetic;
    private final boolean restorePrivateMethods;
    private final boolean repairServletJsonCalls;
    private final boolean restorePrivateFields;
    private final String includePrefix;
    private final Map<String, Map<String, String>> fieldAliases;
    private final Map<String, String> superClassByClass = new HashMap<>();
    private final Map<String, Map<MethodSignature, String>> documentedMethodDeclarations = new HashMap<>();
    private final Map<String, Map<MethodCallKey, String>> documentedMethodCalls = new HashMap<>();
    private final Map<String, Set<MethodCallKey>> unresolvedMethodCalls = new HashMap<>();
    private final Stats stats = new Stats();

    private ReadabilityRestorer(
            Path sourceRoot,
            Path javadocJar,
            boolean apply,
            boolean verbose,
            boolean aliasesOnly,
            boolean restoreProtectedMethods,
            boolean methodsOnly,
            boolean restoreLocals,
            boolean restoreAllPlaceholders,
            boolean includeGenerated,
            boolean includeSynthetic,
            boolean restorePrivateMethods,
            boolean repairServletJsonCalls,
            boolean restorePrivateFields,
            Path fieldAliasFile,
            String includePrefix)
            throws IOException {
        this.sourceRoot = sourceRoot.toAbsolutePath().normalize();
        this.javadocArchive = new ZipFile(javadocJar.toFile(), StandardCharsets.UTF_8);
        this.apply = apply;
        this.verbose = verbose;
        this.aliasesOnly = aliasesOnly;
        this.restoreProtectedMethods = restoreProtectedMethods;
        this.methodsOnly = methodsOnly;
        this.restoreLocals = restoreLocals;
        this.restoreAllPlaceholders = restoreAllPlaceholders;
        this.includeGenerated = includeGenerated;
        this.includeSynthetic = includeSynthetic;
        this.restorePrivateMethods = restorePrivateMethods;
        this.repairServletJsonCalls = repairServletJsonCalls;
        this.restorePrivateFields = restorePrivateFields;
        this.fieldAliases = readFieldAliases(fieldAliasFile);
        this.includePrefix = includePrefix == null ? "" : includePrefix.replace('\\', '/');
    }

    public static void main(String[] args) throws Exception {
        Arguments arguments = Arguments.parse(args);
        try (ReadabilityRestorer restorer = new ReadabilityRestorer(
                arguments.sourceRoot,
                arguments.javadocJar,
                arguments.apply,
                arguments.verbose,
                arguments.aliasesOnly,
                arguments.restoreProtectedMethods,
                arguments.methodsOnly,
                arguments.restoreLocals,
                arguments.restoreAllPlaceholders,
                arguments.includeGenerated,
                arguments.includeSynthetic,
                arguments.restorePrivateMethods,
                arguments.repairServletJsonCalls,
                arguments.restorePrivateFields,
                arguments.fieldAliasFile,
                arguments.includePrefix)) {
            restorer.run();
        }
    }

    private void run() throws IOException {
        List<Path> sources;
        try (var paths = Files.walk(sourceRoot)) {
            sources = paths.filter(path -> path.toString().endsWith(".java"))
                    .sorted()
                    .collect(Collectors.toList());
        }

        buildTypeHierarchy(sources);
        if (restoreProtectedMethods && !aliasesOnly) {
            discoverDocumentedMethodAliases(sources);
        }

        for (Path source : sources) {
            String relative = sourceRoot.relativize(source).toString().replace('\\', '/');
            if (!includePrefix.isEmpty() && !relative.startsWith(includePrefix)) {
                continue;
            }
            if (!includeSynthetic && SYNTHETIC_SOURCE.matcher(relative).matches()) {
                stats.syntheticFilesSkipped++;
                continue;
            }
            restoreSource(source, relative);
        }

        System.out.println(stats.describe(apply, verbose));
    }

    private void restoreSource(Path source, String relative) throws IOException {
        String original = Files.readString(source, StandardCharsets.UTF_8);
        if (!includeGenerated && isGeneratedSource(original, relative)) {
            stats.generatedFilesSkipped++;
            return;
        }

        CompilationUnit unit;
        try {
            unit = StaticJavaParser.parse(original);
            LexicalPreservingPrinter.setup(unit);
        } catch (ParseProblemException exception) {
            stats.parseFailures.put(relative, firstLine(exception.getMessage()));
            return;
        }

        String packageName = unit.getPackageDeclaration()
                .map(declaration -> declaration.getNameAsString())
                .orElse("");
        boolean changed = false;

        for (TypeDeclaration<?> type : unit.getTypes()) {
            String qualifiedName = packageName.isEmpty()
                    ? type.getNameAsString()
                    : packageName + "." + type.getNameAsString();
            Optional<ClassDoc> classDoc = readClassDoc(qualifiedName);
            if (!aliasesOnly && classDoc.isPresent()) {
                if (restoreProtectedMethods) {
                    changed |= restoreDocumentedMethodNames(type, qualifiedName);
                }
                changed |= restoreClassDocumentation(type, classDoc.get());
                changed |= restoreCallableParametersAndDocumentation(type, classDoc.get());
            } else if (!aliasesOnly) {
                stats.classesWithoutJavadoc++;
            }
            if (!aliasesOnly && !methodsOnly) {
                changed |= restoreBeanFieldNames(type);
            }
            if (!aliasesOnly && restorePrivateFields) {
                changed |= restoreRemainingPrivateFieldNames(type);
                changed |= refineGenericPrivateFieldNames(type);
            }
            if (!aliasesOnly && restoreLocals) {
                changed |= restoreLocalNames(type);
            }
            if (!aliasesOnly && restoreAllPlaceholders) {
                changed |= restoreRemainingLocalNames(type);
                changed |= restoreInitializerNames(type);
            }
            if (!aliasesOnly && restorePrivateMethods) {
                changed |= restorePrivateMethodNames(type);
                changed |= refineGenericPrivateMethodNames(type);
            }
            if (!aliasesOnly && repairServletJsonCalls) {
                changed |= repairServletJsonCalls(type);
            }
            changed |= restoreAliasedFieldReferences(type, qualifiedName);
        }

        if (!changed) {
            stats.unchangedFiles++;
            return;
        }

        stats.changedFiles++;
        String restored = LexicalPreservingPrinter.print(unit);
        if (apply) {
            Files.writeString(source, restored, StandardCharsets.UTF_8);
        }
    }

    private boolean restoreClassDocumentation(TypeDeclaration<?> type, ClassDoc classDoc) {
        if (type.getJavadocComment().isPresent() || classDoc.description.isBlank()) {
            return false;
        }
        type.setJavadocComment(new JavadocComment(classDoc.description));
        stats.classCommentsRestored++;
        return true;
    }

    private void discoverDocumentedMethodAliases(List<Path> sources) {
        Map<String, Map<MethodCallKey, Set<String>>> callCandidates = new HashMap<>();
        Map<String, Set<MethodCallKey>> unresolvedOverloads = new HashMap<>();
        for (Path source : sources) {
            try {
                CompilationUnit unit = StaticJavaParser.parse(source);
                String packageName = unit.getPackageDeclaration()
                        .map(declaration -> declaration.getNameAsString())
                        .orElse("");
                for (TypeDeclaration<?> type : unit.getTypes()) {
                    String qualifiedName = packageName.isEmpty()
                            ? type.getNameAsString()
                            : packageName + "." + type.getNameAsString();
                    Optional<ClassDoc> classDoc = readClassDoc(qualifiedName);
                    Map<MethodSignature, String> declarations = new LinkedHashMap<>();
                    if (classDoc.isPresent()) {
                        for (MethodDeclaration method : type.getMethods()) {
                            if (!method.isProtected() || !isDecompiledName(method.getNameAsString())) {
                                continue;
                            }
                            List<String> parameterTypes = method.getParameters().stream()
                                    .map(parameter -> normalizeType(parameter.getType().asString()))
                                    .collect(Collectors.toList());
                            List<MethodDoc> matches = classDoc.get().methods.stream()
                                    .filter(documented -> documented.parameterTypes.equals(parameterTypes))
                                    .filter(documented -> !isDecompiledName(documented.name))
                                    .collect(Collectors.toList());
                            if (matches.size() != 1) {
                                continue;
                            }
                            MethodDoc documented = matches.get(0);
                            boolean targetSignatureExists = type.getMethodsByName(documented.name).stream()
                                    .filter(existing -> existing != method)
                                    .anyMatch(existing -> existing.getParameters().stream()
                                            .map(parameter -> normalizeType(parameter.getType().asString()))
                                            .collect(Collectors.toList())
                                            .equals(parameterTypes));
                            if (targetSignatureExists) {
                                continue;
                            }
                            MethodSignature signature = new MethodSignature(method.getNameAsString(), parameterTypes);
                            declarations.put(signature, documented.name);
                            MethodCallKey callKey = new MethodCallKey(
                                    method.getNameAsString(), method.getParameters().size());
                            callCandidates.computeIfAbsent(qualifiedName, ignored -> new LinkedHashMap<>())
                                    .computeIfAbsent(callKey, ignored -> new HashSet<>())
                                    .add(documented.name);
                        }
                    }
                    for (MethodDeclaration method : type.getMethods()) {
                        MethodCallKey key = new MethodCallKey(
                                method.getNameAsString(), method.getParameters().size());
                        MethodSignature signature = new MethodSignature(
                                method.getNameAsString(),
                                method.getParameters().stream()
                                        .map(parameter -> normalizeType(parameter.getType().asString()))
                                        .collect(Collectors.toList()));
                        if (isDecompiledName(method.getNameAsString()) && !declarations.containsKey(signature)) {
                            unresolvedOverloads.computeIfAbsent(qualifiedName, ignored -> new HashSet<>()).add(key);
                        }
                    }
                    if (!declarations.isEmpty()) {
                        documentedMethodDeclarations.put(qualifiedName, declarations);
                    }
                }
            } catch (IOException | ParseProblemException exception) {
                // The normal source pass reports parse failures with file context.
            }
        }

        callCandidates.forEach((className, candidatesByCall) -> {
            Map<MethodCallKey, String> aliases = new LinkedHashMap<>();
            candidatesByCall.forEach((call, names) -> {
                if (names.size() == 1
                        && !unresolvedOverloads.getOrDefault(className, Set.of()).contains(call)) {
                    aliases.put(call, names.iterator().next());
                }
            });
            if (!aliases.isEmpty()) {
                documentedMethodCalls.put(className, aliases);
            }
        });
        unresolvedOverloads.forEach((className, calls) ->
                unresolvedMethodCalls.put(className, new HashSet<>(calls)));
    }

    private boolean restoreDocumentedMethodNames(TypeDeclaration<?> type, String qualifiedName) {
        boolean changed = false;
        Map<MethodSignature, String> declarationAliases = documentedMethodDeclarations
                .getOrDefault(qualifiedName, Map.of());
        for (MethodDeclaration method : type.getMethods()) {
            MethodSignature signature = new MethodSignature(
                    method.getNameAsString(),
                    method.getParameters().stream()
                            .map(parameter -> normalizeType(parameter.getType().asString()))
                            .collect(Collectors.toList()));
            String restoredName = declarationAliases.get(signature);
            if (restoredName != null) {
                stats.protectedMethodRenameDetails.add(
                        qualifiedName + "." + method.getNameAsString() + signature.parameterTypes
                                + " -> " + restoredName);
                method.setName(restoredName);
                stats.protectedMethodsRenamed++;
                changed = true;
            }
        }

        // A subclass and its parent may both contain an obfuscated overload
        // with the same arity but different documented names. Without symbol
        // solving, renaming such a call would be a guess, so only propagate a
        // name that is unique throughout the applicable hierarchy.
        Map<MethodCallKey, Set<String>> callCandidates = new LinkedHashMap<>();
        Set<MethodCallKey> unsafeCalls = new HashSet<>();
        String owner = qualifiedName;
        Set<String> visited = new HashSet<>();
        while (owner != null && visited.add(owner)) {
            documentedMethodCalls.getOrDefault(owner, Map.of()).forEach((key, name) ->
                    callCandidates.computeIfAbsent(key, ignored -> new HashSet<>()).add(name));
            unsafeCalls.addAll(unresolvedMethodCalls.getOrDefault(owner, Set.of()));
            owner = superClassByClass.get(owner);
        }
        Map<MethodCallKey, String> applicableCalls = callCandidates.entrySet().stream()
                .filter(entry -> entry.getValue().size() == 1)
                .filter(entry -> !unsafeCalls.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().iterator().next(),
                        (left, right) -> left,
                        LinkedHashMap::new));
        if (applicableCalls.isEmpty()) {
            return changed;
        }

        Set<MethodCallKey> locallyDeclaredObfuscated = type.getMethods().stream()
                .filter(method -> isDecompiledName(method.getNameAsString()))
                .map(method -> new MethodCallKey(method.getNameAsString(), method.getParameters().size()))
                .collect(Collectors.toSet());
        for (MethodCallExpr call : type.findAll(MethodCallExpr.class)) {
            if (!belongsToType(call, type)) {
                continue;
            }
            MethodCallKey key = new MethodCallKey(call.getNameAsString(), call.getArguments().size());
            String restoredName = applicableCalls.get(key);
            if (restoredName == null || locallyDeclaredObfuscated.contains(key)) {
                continue;
            }
            if (call.getScope().isEmpty()
                    || call.getScope().get().isThisExpr()
                    || call.getScope().get().isSuperExpr()) {
                call.setName(restoredName);
                stats.protectedMethodCallsRenamed++;
                changed = true;
            }
        }
        return changed;
    }

    private boolean restoreCallableParametersAndDocumentation(TypeDeclaration<?> type, ClassDoc classDoc) {
        boolean changed = false;
        List<CallableDeclaration<?>> callables = new ArrayList<>();
        callables.addAll(type.getMethods());
        callables.addAll(type.getConstructors());

        for (CallableDeclaration<?> callable : callables) {
            String callableName = callable instanceof ConstructorDeclaration
                    ? type.getNameAsString()
                    : callable.getNameAsString();
            List<MethodDoc> candidates = classDoc.methods.stream()
                    .filter(method -> method.name.equals(callableName))
                    .filter(method -> method.parameterNames.size() == callable.getParameters().size())
                    .collect(Collectors.toList());
            Optional<MethodDoc> match = chooseMethod(candidates, callable);
            if (match.isEmpty()) {
                if (!candidates.isEmpty()) {
                    stats.ambiguousCallables++;
                }
                continue;
            }

            MethodDoc methodDoc = match.get();
            for (int index = 0; index < callable.getParameters().size(); index++) {
                Parameter parameter = callable.getParameter(index);
                String oldName = parameter.getNameAsString();
                String documentedName = methodDoc.parameterNames.get(index);
                if (!isDecompiledName(oldName)
                        || isDecompiledName(documentedName)
                        || oldName.equals(documentedName)
                        || nameAlreadyDeclared(callable, documentedName, parameter)) {
                    continue;
                }
                renameParameter(callable, parameter, oldName, documentedName);
                stats.parametersRenamed++;
                changed = true;
            }

            if (callable.getJavadocComment().isEmpty() && !methodDoc.description.isBlank()) {
                callable.setJavadocComment(new JavadocComment(methodDoc.description));
                stats.methodCommentsRestored++;
                changed = true;
            }
        }
        return changed;
    }

    private Optional<MethodDoc> chooseMethod(List<MethodDoc> candidates, CallableDeclaration<?> callable) {
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        }
        List<String> sourceTypes = callable.getParameters().stream()
                .map(parameter -> normalizeType(parameter.getType().asString()))
                .collect(Collectors.toList());
        List<MethodDoc> typeMatches = candidates.stream()
                .filter(candidate -> candidate.parameterTypes.equals(sourceTypes))
                .collect(Collectors.toList());
        return typeMatches.size() == 1 ? Optional.of(typeMatches.get(0)) : Optional.empty();
    }

    private boolean restoreBeanFieldNames(TypeDeclaration<?> type) {
        if (FIELD_INFERENCE_EXCLUSIONS.contains(type.getNameAsString())) {
            return false;
        }
        Map<String, String> inferredNames = new LinkedHashMap<>();
        Set<String> conflicts = new HashSet<>();

        for (MethodDeclaration method : type.getMethods()) {
            inferGetterField(method).ifPresent(pair -> mergeInference(inferredNames, conflicts, pair));
            inferSetterField(method).ifPresent(pair -> mergeInference(inferredNames, conflicts, pair));
        }
        conflicts.forEach(inferredNames::remove);

        // Constructor and setup assignments often preserve the semantic source
        // name on the right-hand side even when ProGuard renamed the field.
        for (AssignExpr assignment : type.findAll(AssignExpr.class)) {
            if (!belongsToType(assignment, type)) {
                continue;
            }
            Optional<String> assignedField = fieldName(assignment.getTarget());
            if (assignedField.isEmpty() || findDirectField(type, assignedField.get()).isEmpty()) {
                continue;
            }
            VariableDeclarator assignedVariable = findDirectField(type, assignedField.get()).orElseThrow();
            inferNameFromExpression(assignment.getValue())
                    .map(name -> adjustNameForType(name, assignedVariable.getType().asString()))
                    .filter(name -> isUsefulInferredName(name))
                    .ifPresent(name -> inferredNames.putIfAbsent(assignedField.get(), name));
        }

        // Initializer type and unique dependency type are weaker evidence, so
        // they are only used when no accessor or assignment supplied a name.
        Map<String, Long> typeFrequency = type.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .collect(Collectors.groupingBy(
                        variable -> normalizeType(variable.getType().asString()),
                        Collectors.counting()));
        for (FieldDeclaration declaration : type.getFields()) {
            for (VariableDeclarator variable : declaration.getVariables()) {
                String oldName = variable.getNameAsString();
                if (inferredNames.containsKey(oldName)) {
                    continue;
                }
                if (declaration.isStatic() && declaration.isFinal()
                        && variable.getType().isPrimitiveType()
                        && variable.getType().asPrimitiveType().getType()
                        == com.github.javaparser.ast.type.PrimitiveType.Primitive.LONG
                        && variable.getInitializer().map(Expression::toString)
                        .map(value -> value.replaceAll("[^0-9]", "").length() >= 12)
                        .orElse(false)) {
                    inferredNames.put(oldName, "serialVersionUID");
                    continue;
                }
                Optional<String> initializerName = variable.getInitializer()
                        .flatMap(this::inferNameFromExpression)
                        .map(name -> adjustNameForType(name, variable.getType().asString()))
                        .filter(this::isUsefulInferredName);
                if (initializerName.isPresent()) {
                    inferredNames.put(oldName, initializerName.get());
                    continue;
                }
                String normalizedType = normalizeType(variable.getType().asString());
                if (typeFrequency.getOrDefault(normalizedType, 0L) == 1L) {
                    inferNameFromType(variable.getType().asString())
                            .filter(this::isUsefulInferredName)
                            .ifPresent(name -> inferredNames.put(oldName, name));
                }
            }
        }

        Set<String> declaredNames = type.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .map(variable -> variable.getNameAsString())
                .collect(Collectors.toSet());
        boolean changed = false;

        for (Map.Entry<String, String> inference : inferredNames.entrySet()) {
            String oldName = inference.getKey();
            String newName = inference.getValue();
            if (!isDecompiledName(oldName)
                    || isDecompiledName(newName)
                    || declaredNames.contains(newName)
                    || hasLocalNameConflict(type, oldName)) {
                continue;
            }
            Optional<VariableDeclarator> field = findDirectField(type, oldName);
            if (field.isEmpty()) {
                continue;
            }
            renameField(type, field.get(), oldName, newName);
            declaredNames.remove(oldName);
            declaredNames.add(newName);
            stats.fieldsRenamed++;
            stats.fieldRenameDetails.add(type.getNameAsString() + "." + oldName + " -> " + newName);
            changed = true;
        }
        return changed;
    }

    /** Restores every remaining private one-letter field in a declaration. */
    private boolean restoreRemainingPrivateFieldNames(TypeDeclaration<?> type) {
        Set<String> reservedNames = type.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .map(VariableDeclarator::getNameAsString)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        boolean changed = false;
        for (FieldDeclaration declaration : type.getFields()) {
            if (!declaration.isPrivate()) {
                continue;
            }
            for (VariableDeclarator field : declaration.getVariables()) {
                String oldName = field.getNameAsString();
                if (!isDecompiledName(oldName)) {
                    continue;
                }
                String proposedName = inferPrivateFieldName(type, declaration, field);
                reservedNames.remove(oldName);
                String newName = uniqueName(proposedName, reservedNames);
                renamePrivateField(type, field, oldName, newName);
                reservedNames.add(newName);
                stats.fieldsRenamed++;
                stats.fieldRenameDetails.add(
                        type.getNameAsString() + "." + oldName + " -> " + newName);
                changed = true;
            }
        }
        return changed;
    }

    /** Refines neutral field fallbacks left by older exhaustive passes. */
    private boolean refineGenericPrivateFieldNames(TypeDeclaration<?> type) {
        Set<String> reservedNames = type.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .map(VariableDeclarator::getNameAsString)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        boolean changed = false;
        for (FieldDeclaration declaration : type.getFields()) {
            if (!declaration.isPrivate()) {
                continue;
            }
            for (VariableDeclarator field : declaration.getVariables()) {
                String oldName = field.getNameAsString();
                if (!Set.of("items", "valuesByKey", "threadContext", "cache").contains(oldName)) {
                    continue;
                }
                String classStem = lowerCamel(type.getNameAsString().replaceAll("\\$\\d+$", ""));
                String proposedName;
                if (oldName.equals("items")) {
                    proposedName = type.getNameAsString().contains("Query")
                            ? "queryParameters" : classStem + "Items";
                } else if (oldName.equals("valuesByKey")) {
                    proposedName = classStem + "ValuesByKey";
                } else if (oldName.equals("threadContext")) {
                    proposedName = classStem + "Context";
                } else {
                    proposedName = classStem + "Cache";
                }
                reservedNames.remove(oldName);
                String newName = uniqueName(proposedName, reservedNames);
                renamePrivateField(type, field, oldName, newName);
                reservedNames.add(newName);
                stats.fieldsRenamed++;
                stats.fieldRenameDetails.add(
                        type.getNameAsString() + "." + oldName + " -> " + newName
                                + " (semantic refinement)");
                changed = true;
            }
        }
        return changed;
    }

    private String inferPrivateFieldName(
            TypeDeclaration<?> type,
            FieldDeclaration declaration,
            VariableDeclarator field) {
        String oldName = field.getNameAsString();
        LinkedHashSet<String> assignmentNames = type.findAll(AssignExpr.class).stream()
                .filter(assignment -> belongsToType(assignment, type))
                .filter(assignment -> fieldName(assignment.getTarget())
                        .map(name -> name.equals(oldName)).orElse(false))
                .map(AssignExpr::getValue)
                .map(this::inferNameFromExpression)
                .flatMap(Optional::stream)
                .filter(this::isUsefulInferredName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (assignmentNames.size() == 1) {
            return assignmentNames.iterator().next();
        }

        if (declaration.isStatic() && declaration.isFinal()) {
            Optional<String> constantName = inferConstantFieldName(field);
            if (constantName.isPresent()) {
                return constantName.get();
            }
        }
        Optional<String> initializerName = field.getInitializer()
                .flatMap(this::inferNameFromExpression)
                .map(name -> adjustNameForType(name, field.getType().asString()))
                .filter(this::isUsefulInferredName);
        if (initializerName.isPresent()) {
            return initializerName.get();
        }
        String rawType = simpleName(field.getType().asString());
        if (Set.of("Map", "HashMap", "ConcurrentHashMap").contains(rawType)
                && type.getNameAsString().contains("Cache")) {
            return "cache";
        }
        if (rawType.equals("ThreadLocal")) {
            return "threadContext";
        }
        return neutralNameFromType(field.getType().asString());
    }

    private Optional<String> inferConstantFieldName(VariableDeclarator field) {
        String rawType = simpleName(field.getType().asString());
        if ((rawType.equals("long") || rawType.equals("Long"))
                && field.getInitializer().map(Expression::toString)
                .map(value -> value.equals("1L") || value.equals("1"))
                .orElse(false)) {
            return Optional.of("serialVersionUID");
        }
        if (field.getInitializer().isEmpty()
                || !field.getInitializer().get().isStringLiteralExpr()) {
            if (rawType.equals("char")) {
                return Optional.of("CHARACTER_VALUE");
            }
            if (rawType.equals("char[]")) {
                return Optional.of("CHARACTER_VALUES");
            }
            return Optional.empty();
        }
        String value = field.getInitializer().get().asStringLiteralExpr().getValue();
        if (value.equals(".")) {
            return Optional.of("DOT");
        }
        if (value.equals("\\.")) {
            return Optional.of("DOT_REGEX");
        }
        if (value.equalsIgnoreCase("utf-8")) {
            return Optional.of("UTF_8");
        }
        if (value.toLowerCase(Locale.ROOT).startsWith("select ")) {
            return Optional.of("SELECT_SQL");
        }
        if (value.toLowerCase(Locale.ROOT).startsWith("update ")) {
            return Optional.of("UPDATE_SQL");
        }
        if (value.toLowerCase(Locale.ROOT).startsWith("create table")) {
            return Optional.of("CREATE_TABLE_SQL");
        }
        String[] words = value.split("[^A-Za-z0-9]+", -1);
        String identifier = java.util.Arrays.stream(words)
                .filter(word -> !word.isBlank())
                .map(word -> word.toUpperCase(Locale.ROOT))
                .collect(Collectors.joining("_"));
        if (!identifier.isBlank()
                && SourceVersion.isIdentifier(identifier)
                && !SourceVersion.isKeyword(identifier.toLowerCase(Locale.ROOT))) {
            return Optional.of(identifier);
        }
        return Optional.of("MESSAGE_TEMPLATE");
    }

    private void renamePrivateField(
            TypeDeclaration<?> type,
            VariableDeclarator field,
            String oldName,
            String newName) {
        boolean staticField = field.findAncestor(FieldDeclaration.class)
                .map(FieldDeclaration::isStatic)
                .orElse(false);
        field.setName(newName);
        type.findAll(FieldAccessExpr.class).stream()
                .filter(expression -> expression.getNameAsString().equals(oldName))
                .filter(expression -> belongsToType(expression, type)
                        && (expression.getScope().isThisExpr()
                        || expression.getScope().toString().equals(type.getNameAsString())
                        || expressionHasDeclaredType(expression.getScope(), type))
                        || expression.getScope().toString().equals(type.getNameAsString() + ".this"))
                .forEach(expression -> expression.setName(newName));
        type.findAll(NameExpr.class).stream()
                .filter(expression -> expression.getNameAsString().equals(oldName))
                .filter(expression -> belongsToType(expression, type))
                .filter(expression -> !isShadowedLocalName(expression, oldName))
                .forEach(expression -> expression.replace(new FieldAccessExpr(
                        staticField ? new NameExpr(type.getNameAsString()) : new ThisExpr(),
                        newName)));
    }

    private boolean isShadowedLocalName(NameExpr expression, String name) {
        CallableDeclaration<?> callable = (CallableDeclaration<?>) expression
                .findAncestor(CallableDeclaration.class)
                .orElse(null);
        return callable != null && (callable.getParameters().stream()
                .anyMatch(parameter -> parameter.getNameAsString().equals(name))
                || callable.findAll(VariableDeclarator.class).stream()
                .filter(variable -> belongsToCallable(variable, callable))
                .anyMatch(variable -> variable.getNameAsString().equals(name)));
    }

    /**
     * Restores only local names that can be derived from a declaration's type,
     * initializer, enhanced-for element type, or caught exception type. A name
     * is skipped whenever the old declaration is not unique in its callable or
     * the proposed semantic name already exists there.
     */
    private boolean restoreLocalNames(TypeDeclaration<?> type) {
        List<CallableDeclaration<?>> callables = new ArrayList<>();
        callables.addAll(type.getMethods());
        callables.addAll(type.getConstructors());
        boolean changed = false;

        for (CallableDeclaration<?> callable : callables) {
            List<VariableDeclarator> localVariables = callable.findAll(VariableDeclarator.class).stream()
                    .filter(variable -> belongsToCallable(variable, callable))
                    .collect(Collectors.toList());
            List<Parameter> nestedParameters = callable.findAll(Parameter.class).stream()
                    .filter(parameter -> belongsToCallable(parameter, callable))
                    .collect(Collectors.toList());
            Set<String> declaredNames = new HashSet<>();
            localVariables.forEach(variable -> declaredNames.add(variable.getNameAsString()));
            nestedParameters.forEach(parameter -> declaredNames.add(parameter.getNameAsString()));

            // Catch variables have exact semantic evidence in their exception
            // type and are scoped independently from normal method parameters.
            for (CatchClause catchClause : callable.findAll(CatchClause.class)) {
                Parameter parameter = catchClause.getParameter();
                String oldName = parameter.getNameAsString();
                Optional<String> candidate = inferNameFromType(parameter.getType().asString())
                        .filter(this::isUsefulInferredName)
                        .or(() -> inferFallbackNameFromType(parameter.getType().asString()));
                if (candidate.isPresent() && renameLocalIdentifier(
                        callable, parameter, oldName, candidate.get(), declaredNames,
                        localVariables, nestedParameters)) {
                    stats.localVariablesRenamed++;
                    changed = true;
                }
            }

            // Private/package-private method parameters are not covered by
            // Javadoc. Domain types and generic element types still provide a
            // stable, non-speculative name.
            if (callable instanceof MethodDeclaration method
                    && !method.isPublic() && !method.isProtected()) {
                for (Parameter parameter : method.getParameters()) {
                    String oldName = parameter.getNameAsString();
                    Optional<String> candidate = inferNameFromType(parameter.getType().asString())
                            .filter(this::isUsefulInferredName)
                            .or(() -> inferFallbackNameFromType(parameter.getType().asString()));
                    if (candidate.isPresent() && renameLocalIdentifier(
                            callable, parameter, oldName, candidate.get(), declaredNames,
                            localVariables, nestedParameters)) {
                        stats.privateParametersRenamed++;
                        changed = true;
                    }
                }
            }

            for (VariableDeclarator variable : localVariables) {
                String oldName = variable.getNameAsString();
                if (!isDecompiledName(oldName)) {
                    continue;
                }
                Optional<String> candidate;
                if (variable.findAncestor(ForStmt.class)
                        .filter(statement -> statement.getInitialization().stream()
                                .filter(Expression::isVariableDeclarationExpr)
                                .flatMap(expression -> expression.asVariableDeclarationExpr()
                                        .getVariables().stream())
                                .anyMatch(initializer -> initializer == variable))
                        .isPresent()) {
                    candidate = Optional.of("index");
                } else if (variable.findAncestor(ForEachStmt.class)
                        .filter(statement -> statement.getVariable().getVariables().contains(variable))
                        .isPresent()) {
                    candidate = inferNameFromType(variable.getType().asString())
                            .filter(this::isUsefulInferredName)
                            .or(() -> inferFallbackNameFromType(variable.getType().asString()));
                } else {
                    candidate = inferDirectReturnName(variable, callable)
                            .or(() -> inferNameFromAssignments(
                                    callable, variable.getNameAsString(), variable.getType().asString()))
                            .or(() -> variable.getInitializer()
                            .flatMap(this::inferNameFromExpression)
                            .map(name -> adjustNameForType(name, variable.getType().asString()))
                            .filter(this::isUsefulInferredName))
                            .or(() -> inferNameFromType(variable.getType().asString())
                                    .filter(this::isUsefulInferredName))
                            .or(() -> inferFallbackNameFromType(variable.getType().asString()));
                }
                if (candidate.isPresent() && renameLocalIdentifier(
                        callable, variable, oldName, candidate.get(), declaredNames,
                        localVariables, nestedParameters)) {
                    stats.localVariablesRenamed++;
                    changed = true;
                }
            }
        }
        return changed;
    }

    /**
     * Removes every remaining decompiler placeholder from parameters and local
     * declarations. High-confidence inference runs first; this final pass uses
     * a neutral type-based name only where the source contains no stronger
     * evidence. Declarations that reused the same placeholder in disjoint
     * scopes are renamed as one group, which preserves Java's original name
     * resolution without requiring debug-local metadata.
     */
    private boolean restoreRemainingLocalNames(TypeDeclaration<?> type) {
        List<CallableDeclaration<?>> callables = new ArrayList<>();
        type.findAll(CallableDeclaration.class)
                .forEach(callable -> callables.add((CallableDeclaration<?>) callable));
        boolean changed = false;

        for (CallableDeclaration<?> callable : callables) {
            List<VariableDeclarator> localVariables = callable.findAll(VariableDeclarator.class).stream()
                    .filter(variable -> belongsToCallable(variable, callable))
                    .collect(Collectors.toList());
            List<Parameter> parameters = callable.findAll(Parameter.class).stream()
                    .filter(parameter -> belongsToCallable(parameter, callable))
                    .collect(Collectors.toList());
            Map<String, List<Node>> declarationsByName = new LinkedHashMap<>();
            parameters.stream()
                    .filter(parameter -> isDecompiledName(parameter.getNameAsString()))
                    .forEach(parameter -> declarationsByName
                            .computeIfAbsent(parameter.getNameAsString(), ignored -> new ArrayList<>())
                            .add(parameter));
            localVariables.stream()
                    .filter(variable -> isDecompiledName(variable.getNameAsString()))
                    .forEach(variable -> declarationsByName
                            .computeIfAbsent(variable.getNameAsString(), ignored -> new ArrayList<>())
                            .add(variable));
            if (declarationsByName.isEmpty()) {
                continue;
            }

            Set<String> reservedNames = new LinkedHashSet<>();
            parameters.forEach(parameter -> reservedNames.add(parameter.getNameAsString()));
            localVariables.forEach(variable -> reservedNames.add(variable.getNameAsString()));
            callable.findAncestor(TypeDeclaration.class).ifPresent(ownerNode ->
                    ((TypeDeclaration<?>) ownerNode).getFields().stream()
                            .flatMap(field -> field.getVariables().stream())
                            .forEach(field -> reservedNames.add(field.getNameAsString())));

            for (Map.Entry<String, List<Node>> entry : declarationsByName.entrySet()) {
                String oldName = entry.getKey();
                List<Node> declarations = entry.getValue();
                String proposedName = inferSharedDeclarationName(declarations, callable);
                reservedNames.remove(oldName);
                String newName = uniqueName(proposedName, reservedNames);

                for (Node declaration : declarations) {
                    if (declaration instanceof VariableDeclarator variable) {
                        variable.setName(newName);
                        stats.localVariablesRenamed++;
                    } else if (declaration instanceof Parameter parameter) {
                        parameter.setName(newName);
                        stats.privateParametersRenamed++;
                    }
                }
                callable.findAll(NameExpr.class).stream()
                        .filter(expression -> expression.getNameAsString().equals(oldName))
                        .filter(expression -> referencesParameter(expression, callable, oldName))
                        .forEach(expression -> expression.setName(newName));
                reservedNames.add(newName);
                changed = true;
            }
        }
        return changed;
    }

    /** Restores names inside static and instance initializer blocks. */
    private boolean restoreInitializerNames(TypeDeclaration<?> type) {
        boolean changed = false;
        for (InitializerDeclaration initializer : type.findAll(InitializerDeclaration.class)) {
            List<VariableDeclarator> variables = initializer.findAll(VariableDeclarator.class).stream()
                    .filter(variable -> variable.findAncestor(CallableDeclaration.class).isEmpty())
                    .collect(Collectors.toList());
            List<Parameter> catchParameters = initializer.findAll(CatchClause.class).stream()
                    .map(CatchClause::getParameter)
                    .collect(Collectors.toList());
            Map<String, List<Node>> declarationsByName = new LinkedHashMap<>();
            variables.stream().filter(variable -> isDecompiledName(variable.getNameAsString()))
                    .forEach(variable -> declarationsByName
                            .computeIfAbsent(variable.getNameAsString(), ignored -> new ArrayList<>())
                            .add(variable));
            catchParameters.stream().filter(parameter -> isDecompiledName(parameter.getNameAsString()))
                    .forEach(parameter -> declarationsByName
                            .computeIfAbsent(parameter.getNameAsString(), ignored -> new ArrayList<>())
                            .add(parameter));

            Set<String> reservedNames = new LinkedHashSet<>();
            variables.forEach(variable -> reservedNames.add(variable.getNameAsString()));
            catchParameters.forEach(parameter -> reservedNames.add(parameter.getNameAsString()));
            for (Map.Entry<String, List<Node>> entry : declarationsByName.entrySet()) {
                String oldName = entry.getKey();
                LinkedHashSet<String> candidates = entry.getValue().stream()
                        .map(this::inferInitializerDeclarationName)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                String proposedName = candidates.size() == 1
                        ? candidates.iterator().next()
                        : "temporaryValue";
                reservedNames.remove(oldName);
                String newName = uniqueName(proposedName, reservedNames);
                for (Node declaration : entry.getValue()) {
                    if (declaration instanceof VariableDeclarator variable) {
                        variable.setName(newName);
                        stats.localVariablesRenamed++;
                    } else if (declaration instanceof Parameter parameter) {
                        parameter.setName(newName);
                        stats.localVariablesRenamed++;
                    }
                }
                initializer.findAll(NameExpr.class).stream()
                        .filter(expression -> expression.getNameAsString().equals(oldName))
                        .forEach(expression -> expression.setName(newName));
                reservedNames.add(newName);
                changed = true;
            }
        }
        return changed;
    }

    private String inferInitializerDeclarationName(Node declaration) {
        if (declaration instanceof VariableDeclarator variable) {
            if (variable.findAncestor(ForStmt.class).isPresent()) {
                return "index";
            }
            Optional<String> initializerName = variable.getInitializer()
                    .flatMap(this::inferNameFromExpression)
                    .map(name -> adjustNameForType(name, variable.getType().asString()))
                    .filter(this::isUsefulInferredName);
            if (initializerName.isPresent()) {
                return initializerName.get();
            }
        }
        return declarationType(declaration)
                .map(this::neutralNameFromType)
                .orElse("localValue");
    }

    /**
     * Renames private one-letter method overload groups together with calls
     * that are provably dispatched on the declaring class. Keeping an entire
     * overload group on one target name preserves Java overload resolution and
     * avoids speculative argument-type matching.
     */
    private boolean restorePrivateMethodNames(TypeDeclaration<?> rootType) {
        List<TypeDeclaration<?>> types = new ArrayList<>();
        types.add(rootType);
        rootType.findAll(TypeDeclaration.class).stream()
                .map(node -> (TypeDeclaration<?>) node)
                .filter(type -> type != rootType)
                .forEach(types::add);
        boolean changed = false;

        for (TypeDeclaration<?> type : types) {
            Map<String, List<MethodDeclaration>> groups = type.getMethods().stream()
                    .filter(MethodDeclaration::isPrivate)
                    .filter(method -> isDecompiledName(method.getNameAsString()))
                    .collect(Collectors.groupingBy(
                            MethodDeclaration::getNameAsString,
                            LinkedHashMap::new,
                            Collectors.toList()));
            for (Map.Entry<String, List<MethodDeclaration>> entry : groups.entrySet()) {
                String oldName = entry.getKey();
                List<MethodDeclaration> methods = entry.getValue();
                Set<Integer> arities = methods.stream()
                        .map(method -> method.getParameters().size())
                        .collect(Collectors.toSet());
                boolean ambiguousWithNonPrivateOverload = type.getMethods().stream()
                        .filter(method -> !methods.contains(method))
                        .filter(method -> method.getNameAsString().equals(oldName))
                        .anyMatch(method -> arities.contains(method.getParameters().size()));
                if (ambiguousWithNonPrivateOverload) {
                    stats.privateMethodGroupsSkipped++;
                    continue;
                }

                String targetName = availablePrivateMethodName(
                        type, methods, inferPrivateMethodGroupName(type, methods));
                methods.forEach(method -> method.setName(targetName));
                int[] renamedCalls = {0};
                type.findAll(MethodCallExpr.class).stream()
                        .filter(call -> call.getNameAsString().equals(oldName))
                        .filter(call -> arities.contains(call.getArguments().size()))
                        .filter(call -> isPrivateCallOnType(call, type))
                        .forEach(call -> {
                            call.setName(targetName);
                            renamedCalls[0]++;
                        });
                type.findAll(MethodReferenceExpr.class).stream()
                        .filter(reference -> reference.getIdentifier().equals(oldName))
                        .filter(reference -> isPrivateReferenceOnType(reference, type))
                        .forEach(reference -> {
                            reference.setIdentifier(targetName);
                            renamedCalls[0]++;
                        });
                stats.privateMethodsRenamed += methods.size();
                stats.privateMethodCallsRenamed += renamedCalls[0];
                stats.privateMethodRenameDetails.add(
                        type.getNameAsString() + "." + oldName + " -> " + targetName
                                + " (" + methods.size() + " overloads)");
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Replaces the neutral fallback names emitted by older restoration passes.
     * Every overload in one fallback group keeps a common, class-specific verb,
     * so Java overload resolution and recursive calls remain unchanged.
     */
    private boolean refineGenericPrivateMethodNames(TypeDeclaration<?> rootType) {
        List<TypeDeclaration<?>> types = new ArrayList<>();
        types.add(rootType);
        rootType.findAll(TypeDeclaration.class).stream()
                .map(node -> (TypeDeclaration<?>) node)
                .filter(type -> type != rootType)
                .forEach(types::add);
        boolean changed = false;

        for (TypeDeclaration<?> type : types) {
            Map<String, List<MethodDeclaration>> groups = type.getMethods().stream()
                    .filter(MethodDeclaration::isPrivate)
                    .filter(method -> isGenericMethodName(method.getNameAsString()))
                    .collect(Collectors.groupingBy(
                            MethodDeclaration::getNameAsString,
                            LinkedHashMap::new,
                            Collectors.toList()));
            for (Map.Entry<String, List<MethodDeclaration>> entry : groups.entrySet()) {
                String oldName = entry.getKey();
                List<MethodDeclaration> methods = entry.getValue();
                String proposedName = inferClassSpecificMethodName(type, oldName);
                String targetName = availablePrivateMethodName(type, methods, proposedName);
                methods.forEach(method -> method.setName(targetName));
                int[] renamedCalls = {0};
                type.findAll(MethodCallExpr.class).stream()
                        .filter(call -> call.getNameAsString().equals(oldName))
                        .filter(call -> isPrivateCallOnType(call, type))
                        .forEach(call -> {
                            call.setName(targetName);
                            renamedCalls[0]++;
                        });
                stats.privateMethodsRenamed += methods.size();
                stats.privateMethodCallsRenamed += renamedCalls[0];
                stats.privateMethodRenameDetails.add(
                        type.getNameAsString() + "." + oldName + " -> " + targetName
                                + " (semantic refinement, " + methods.size() + " overloads)");
                changed = true;
            }
        }
        return changed;
    }

    private boolean isGenericMethodName(String name) {
        return name.matches("processInternal(?:Internal|\\d+)*")
                || name.matches("buildTextInternal(?:Internal|\\d+)*");
    }

    private String inferClassSpecificMethodName(TypeDeclaration<?> type, String oldName) {
        String className = type.getNameAsString().replaceAll("\\$\\d+$", "");
        boolean secondary = oldName.contains("InternalInternal") || oldName.matches(".*\\d+$");
        if (oldName.startsWith("buildText")) {
            return "buildQueryConditions";
        }
        if (className.endsWith("QueryImpl") || className.endsWith("Query")) {
            return secondary ? "mapQueryResults" : "executeQuery";
        }
        if (className.contains("Import")) {
            return secondary ? "importReferencedResources" : "importData";
        }
        if (className.contains("Export")) {
            return secondary ? "exportReferencedResources" : "exportData";
        }
        if (className.endsWith("Parser")) {
            return secondary ? "parseNestedDefinition" : "parseDefinition";
        }
        if (className.endsWith("Builder")) {
            return secondary ? "buildNestedContent" : "buildContent";
        }
        if (className.endsWith("ServletHandler")) {
            return secondary ? "handleNestedRequestData" : "handleRequestData";
        }
        if (className.contains("Cache")) {
            return secondary ? "refreshDependentCacheData" : "manageCacheData";
        }
        if (className.endsWith("Service") || className.endsWith("ServiceImpl")) {
            return secondary ? "executeDependentServiceOperation" : "executeServiceOperation";
        }
        if (className.equals("SpringBootHome")) {
            return secondary ? "locateNestedApplicationHome" : "locateApplicationHome";
        }
        if (className.equals("AuthenticationManager")) {
            return secondary ? "evaluateInheritedPermission" : "evaluatePermission";
        }
        if (className.equals("PermissionProvider")) {
            return secondary ? "buildNestedPermissions" : "buildPermissions";
        }
        return (secondary ? "processNested" : "process") + capitalizeIdentifier(className) + "Data";
    }

    private String inferPrivateMethodGroupName(
            TypeDeclaration<?> type,
            List<MethodDeclaration> methods) {
        Set<String> returnTypes = methods.stream()
                .map(method -> normalizeType(method.getType().asString()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Set<String> firstParameterTypes = methods.stream()
                .filter(method -> !method.getParameters().isEmpty())
                .map(method -> simpleName(method.getParameter(0).getType().asString()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        boolean allWithoutParameters = methods.stream()
                .allMatch(method -> method.getParameters().isEmpty());

        if (returnTypes.size() == 1) {
            String returnType = simpleName(returnTypes.iterator().next());
            if (returnType.equals("void")) {
                if (allWithoutParameters) {
                    return "initializeState";
                }
                if (firstParameterTypes.size() == 1) {
                    String parameterType = firstParameterTypes.iterator().next();
                    return switch (parameterType) {
                        case "HttpServletResponse" -> "writeResponse";
                        case "Connection" -> methods.stream()
                                .anyMatch(method -> method.toString().contains(".close("))
                                ? "closeConnection" : "configureConnection";
                        case "List", "Collection", "Set" -> "processItems";
                        case "Map" -> "populateValues";
                        case "Element" -> type.getNameAsString().contains("Import")
                                ? "importElement" : "processElement";
                        case "Statement" -> "executeStatement";
                        default -> "process" + capitalizeIdentifier(parameterType);
                    };
                }
                return "processInternal";
            }
            return switch (returnType) {
                case "boolean", "Boolean" -> "evaluateCondition";
                case "int", "Integer", "long", "Long", "short", "Short" -> "calculateValue";
                case "String", "StringBuilder", "StringBuffer" -> "buildText";
                case "List", "Collection", "Set" -> "collectItems";
                case "Map" -> "buildValuesByKey";
                case "byte[]" -> "buildBytes";
                case "Class" -> "resolveClass";
                case "File" -> "resolveFile";
                default -> "resolve" + capitalizeIdentifier(returnType);
            };
        }
        return "processInternal";
    }

    private String capitalizeIdentifier(String value) {
        String simple = simpleName(value);
        if (simple.endsWith("[]")) {
            simple = simple.substring(0, simple.length() - 2) + "Array";
        }
        return simple.isEmpty()
                ? "Value"
                : Character.toUpperCase(simple.charAt(0)) + simple.substring(1);
    }

    private String availablePrivateMethodName(
            TypeDeclaration<?> type,
            List<MethodDeclaration> methods,
            String proposedName) {
        for (int attempt = 0; ; attempt++) {
            String candidate = attempt == 0
                    ? proposedName
                    : attempt == 1 ? proposedName + "Internal" : proposedName + attempt;
            boolean conflicts = methods.stream().anyMatch(method -> type.getMethods().stream()
                    .filter(other -> !methods.contains(other))
                    .filter(other -> other.getNameAsString().equals(candidate))
                    .anyMatch(other -> sameParameterTypes(method, other)));
            if (!conflicts) {
                return candidate;
            }
        }
    }

    private boolean sameParameterTypes(MethodDeclaration left, MethodDeclaration right) {
        if (left.getParameters().size() != right.getParameters().size()) {
            return false;
        }
        for (int index = 0; index < left.getParameters().size(); index++) {
            if (!erasedType(left.getParameter(index).getType().asString())
                    .equals(erasedType(right.getParameter(index).getType().asString()))) {
                return false;
            }
        }
        return true;
    }

    private String erasedType(String type) {
        String normalized = normalizeType(type);
        StringBuilder erased = new StringBuilder();
        int genericDepth = 0;
        for (int index = 0; index < normalized.length(); index++) {
            char character = normalized.charAt(index);
            if (character == '<') {
                genericDepth++;
            } else if (character == '>') {
                genericDepth--;
            } else if (genericDepth == 0) {
                erased.append(character);
            }
        }
        return erased.toString();
    }

    private boolean isPrivateCallOnType(MethodCallExpr call, TypeDeclaration<?> type) {
        if (call.getScope().isPresent()) {
            Expression scopeExpression = call.getScope().get();
            String scope = scopeExpression.toString();
            return scope.equals("this")
                    || scope.equals(type.getNameAsString())
                    || scope.equals(type.getNameAsString() + ".this")
                    || expressionHasDeclaredType(scopeExpression, type);
        }
        return call.findAncestor(TypeDeclaration.class)
                .map(owner -> owner == type)
                .orElse(false);
    }

    private boolean expressionHasDeclaredType(Expression expression, TypeDeclaration<?> type) {
        String expectedType = type.getNameAsString();
        if (expression.isCastExpr()) {
            return simpleName(expression.asCastExpr().getType().asString()).equals(expectedType);
        }
        if (expression.isObjectCreationExpr()) {
            return simpleName(expression.asObjectCreationExpr().getType().asString()).equals(expectedType);
        }
        String variableName;
        if (expression.isNameExpr()) {
            variableName = expression.asNameExpr().getNameAsString();
        } else if (expression.isFieldAccessExpr()
                && expression.asFieldAccessExpr().getScope().isThisExpr()) {
            variableName = expression.asFieldAccessExpr().getNameAsString();
        } else {
            return false;
        }
        Set<String> declaredTypes = new LinkedHashSet<>();
        type.findAll(VariableDeclarator.class).stream()
                .filter(variable -> variable.getNameAsString().equals(variableName))
                .map(variable -> simpleName(variable.getType().asString()))
                .forEach(declaredTypes::add);
        type.findAll(Parameter.class).stream()
                .filter(parameter -> parameter.getNameAsString().equals(variableName))
                .map(parameter -> simpleName(parameter.getType().asString()))
                .forEach(declaredTypes::add);
        return declaredTypes.size() == 1 && declaredTypes.contains(expectedType);
    }

    /**
     * Repairs servlet JSON responses that were historically emitted through
     * the inherited one-letter compatibility bridge. A call is changed only
     * when its first argument is statically an HttpServletResponse and none of
     * the same-named private overloads can accept the observed argument types.
     */
    private boolean repairServletJsonCalls(TypeDeclaration<?> rootType) {
        boolean[] changed = {false};
        List<TypeDeclaration<?>> types = new ArrayList<>();
        types.add(rootType);
        rootType.findAll(TypeDeclaration.class).stream()
                .map(node -> (TypeDeclaration<?>) node)
                .filter(type -> type != rootType)
                .forEach(types::add);
        for (TypeDeclaration<?> type : types) {
            for (MethodCallExpr call : type.findAll(MethodCallExpr.class)) {
                if (call.getArguments().size() != 2
                        || call.getNameAsString().equals("writeObjectToJson")
                        || !isPrivateCallOnType(call, type)) {
                    continue;
                }
                Optional<String> firstType = inferExpressionType(call.getArgument(0), call, type);
                if (firstType.isEmpty() || !simpleName(firstType.get()).equals("HttpServletResponse")) {
                    continue;
                }
                List<MethodDeclaration> candidates = type.getMethods().stream()
                        .filter(MethodDeclaration::isPrivate)
                        .filter(method -> method.getNameAsString().equals(call.getNameAsString()))
                        .filter(method -> method.getParameters().size() == 2)
                        .collect(Collectors.toList());
                boolean privateMatch = candidates.stream()
                        .anyMatch(method -> callCouldMatch(method, call, type));
                if (!privateMatch) {
                    call.setName("writeObjectToJson");
                    stats.servletJsonCallsRepaired++;
                    changed[0] = true;
                }
            }
        }
        return changed[0];
    }

    private boolean callCouldMatch(
            MethodDeclaration method,
            MethodCallExpr call,
            TypeDeclaration<?> type) {
        for (int index = 0; index < method.getParameters().size(); index++) {
            Optional<String> argumentType = inferExpressionType(call.getArgument(index), call, type);
            if (argumentType.isPresent() && !couldAssign(
                    argumentType.get(), method.getParameter(index).getType().asString())) {
                return false;
            }
        }
        return true;
    }

    private Optional<String> inferExpressionType(
            Expression expression,
            Node useSite,
            TypeDeclaration<?> type) {
        if (expression.isEnclosedExpr()) {
            return inferExpressionType(expression.asEnclosedExpr().getInner(), useSite, type);
        }
        if (expression.isCastExpr()) {
            return Optional.of(expression.asCastExpr().getType().asString());
        }
        if (expression.isObjectCreationExpr()) {
            return Optional.of(expression.asObjectCreationExpr().getType().asString());
        }
        if (expression.isStringLiteralExpr()) {
            return Optional.of("String");
        }
        if (expression.isBooleanLiteralExpr()) {
            return Optional.of("boolean");
        }
        if (expression.isIntegerLiteralExpr()) {
            return Optional.of("int");
        }
        if (expression.isLongLiteralExpr()) {
            return Optional.of("long");
        }
        if (expression.isDoubleLiteralExpr()) {
            return Optional.of("double");
        }
        String variableName;
        if (expression.isNameExpr()) {
            variableName = expression.asNameExpr().getNameAsString();
        } else if (expression.isFieldAccessExpr()
                && expression.asFieldAccessExpr().getScope().isThisExpr()) {
            variableName = expression.asFieldAccessExpr().getNameAsString();
        } else {
            return Optional.empty();
        }

        CallableDeclaration<?> callable = (CallableDeclaration<?>) useSite
                .findAncestor(CallableDeclaration.class)
                .orElse(null);
        if (callable != null) {
            LinkedHashSet<String> localTypes = new LinkedHashSet<>();
            callable.getParameters().stream()
                    .filter(parameter -> parameter.getNameAsString().equals(variableName))
                    .map(parameter -> parameter.getType().asString())
                    .forEach(localTypes::add);
            callable.findAll(VariableDeclarator.class).stream()
                    .filter(variable -> belongsToCallable(variable, callable))
                    .filter(variable -> variable.getNameAsString().equals(variableName))
                    .map(variable -> variable.getType().asString())
                    .forEach(localTypes::add);
            if (localTypes.size() == 1) {
                return Optional.of(localTypes.iterator().next());
            }
        }
        LinkedHashSet<String> fieldTypes = type.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .filter(field -> field.getNameAsString().equals(variableName))
                .map(field -> field.getType().asString())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return fieldTypes.size() == 1
                ? Optional.of(fieldTypes.iterator().next())
                : Optional.empty();
    }

    private boolean couldAssign(String argumentType, String parameterType) {
        String argument = simpleName(erasedType(argumentType));
        String parameter = simpleName(erasedType(parameterType));
        if (argument.equals(parameter) || parameter.equals("Object")) {
            return true;
        }
        if (Set.of("ArrayList", "LinkedList").contains(argument)
                && Set.of("List", "Collection", "Iterable").contains(parameter)) {
            return true;
        }
        if (Set.of("HashSet", "LinkedHashSet").contains(argument)
                && Set.of("Set", "Collection", "Iterable").contains(parameter)) {
            return true;
        }
        if (Set.of("HashMap", "LinkedHashMap", "ConcurrentHashMap").contains(argument)
                && parameter.equals("Map")) {
            return true;
        }
        return Map.of(
                "Boolean", "boolean",
                "Byte", "byte",
                "Short", "short",
                "Integer", "int",
                "Long", "long",
                "Float", "float",
                "Double", "double",
                "Character", "char")
                .getOrDefault(argument, argument)
                .equals(Map.of(
                        "Boolean", "boolean",
                        "Byte", "byte",
                        "Short", "short",
                        "Integer", "int",
                        "Long", "long",
                        "Float", "float",
                        "Double", "double",
                        "Character", "char")
                        .getOrDefault(parameter, parameter));
    }

    private boolean isPrivateReferenceOnType(
            MethodReferenceExpr reference,
            TypeDeclaration<?> type) {
        String scope = reference.getScope().toString();
        return scope.equals("this")
                || scope.equals(type.getNameAsString())
                || scope.equals(type.getNameAsString() + ".this");
    }

    private String inferSharedDeclarationName(
            List<Node> declarations,
            CallableDeclaration<?> callable) {
        LinkedHashSet<String> candidates = declarations.stream()
                .map(declaration -> inferExhaustiveDeclarationName(declaration, callable))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (candidates.size() == 1) {
            return candidates.iterator().next();
        }

        LinkedHashSet<String> typeNames = declarations.stream()
                .map(this::declarationType)
                .flatMap(Optional::stream)
                .map(this::neutralNameFromType)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return typeNames.size() == 1 ? typeNames.iterator().next() : "temporaryValue";
    }

    private String inferExhaustiveDeclarationName(Node declaration, CallableDeclaration<?> callable) {
        String type = declarationType(declaration).orElse("Object");
        if (declaration instanceof VariableDeclarator variable) {
            if (variable.findAncestor(ForStmt.class)
                    .filter(statement -> statement.getInitialization().stream()
                            .filter(Expression::isVariableDeclarationExpr)
                            .flatMap(expression -> expression.asVariableDeclarationExpr()
                                    .getVariables().stream())
                            .anyMatch(initializer -> initializer == variable))
                    .isPresent()) {
                return "index";
            }
            Optional<String> inferred = inferDirectReturnName(variable, callable)
                    .or(() -> inferNameFromAssignments(
                            callable, variable.getNameAsString(), variable.getType().asString()))
                    .or(() -> variable.getInitializer().flatMap(this::inferNameFromExpression))
                    .map(name -> adjustNameForType(name, variable.getType().asString()))
                    .filter(this::isUsefulInferredName);
            if (inferred.isPresent()) {
                return inferred.get();
            }
        }
        return neutralNameFromType(type);
    }

    private Optional<String> declarationType(Node declaration) {
        if (declaration instanceof VariableDeclarator variable) {
            return Optional.of(variable.getType().asString());
        }
        if (declaration instanceof Parameter parameter) {
            return Optional.of(parameter.getType().asString());
        }
        return Optional.empty();
    }

    private String neutralNameFromType(String type) {
        return inferNameFromType(type)
                .filter(this::isUsefulInferredName)
                .or(() -> inferFallbackNameFromType(type))
                .filter(this::isUsefulInferredName)
                .orElse("localValue");
    }

    private String uniqueName(String proposedName, Set<String> reservedNames) {
        String baseName = isUsefulInferredName(proposedName) ? proposedName : "localValue";
        if (!reservedNames.contains(baseName)) {
            return baseName;
        }
        for (int suffix = 2; ; suffix++) {
            String candidate = baseName + suffix;
            if (!reservedNames.contains(candidate)) {
                return candidate;
            }
        }
    }

    private Optional<String> inferNameFromAssignments(
            CallableDeclaration<?> callable,
            String variableName,
            String variableType) {
        Set<String> candidates = callable.findAll(AssignExpr.class).stream()
                .filter(assignment -> belongsToCallable(assignment, callable))
                .filter(assignment -> assignment.getTarget().isNameExpr())
                .filter(assignment -> assignment.getTarget().asNameExpr()
                        .getNameAsString().equals(variableName))
                .map(AssignExpr::getValue)
                .map(this::inferNameFromExpression)
                .flatMap(Optional::stream)
                .map(name -> adjustNameForType(name, variableType))
                .filter(this::isUsefulInferredName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return candidates.size() == 1
                ? Optional.of(candidates.iterator().next())
                : Optional.empty();
    }

    /**
     * A variable that is returned directly represents the result named by its
     * containing method. This is stronger evidence than its implementation
     * type (for example, an ArrayList returned by parseParameters is a list of
     * parameters, not merely an "arrayList").
     */
    private Optional<String> inferDirectReturnName(
            VariableDeclarator variable,
            CallableDeclaration<?> callable) {
        if (!(callable instanceof MethodDeclaration method)) {
            return Optional.empty();
        }
        String variableName = variable.getNameAsString();
        boolean directlyReturned = method.findAll(ReturnStmt.class).stream()
                .filter(statement -> belongsToCallable(statement, method))
                .map(ReturnStmt::getExpression)
                .flatMap(Optional::stream)
                .anyMatch(expression -> expression.isNameExpr()
                        && expression.asNameExpr().getNameAsString().equals(variableName));
        if (!directlyReturned) {
            return Optional.empty();
        }
        return inferNameFromMethod(method.getNameAsString())
                .filter(this::isUsefulInferredName);
    }

    private Optional<String> inferNameFromMethod(String methodName) {
        if (methodName == null || methodName.isBlank() || isDecompiledName(methodName)) {
            return Optional.empty();
        }
        int conversionMarker = methodName.lastIndexOf('2');
        if (conversionMarker >= 0 && conversionMarker + 1 < methodName.length()
                && Character.isUpperCase(methodName.charAt(conversionMarker + 1))) {
            return Optional.of(lowerCamel(methodName.substring(conversionMarker + 1)));
        }
        for (String prefix : List.of(
                "get", "find", "load", "build", "create", "read", "parse", "query",
                "resolve", "convert", "format", "calculate", "compute", "collect",
                "extract", "generate", "evaluate")) {
            if (methodName.startsWith(prefix) && methodName.length() > prefix.length()
                    && Character.isUpperCase(methodName.charAt(prefix.length()))) {
                return Optional.of(lowerCamel(methodName.substring(prefix.length())));
            }
        }
        return Optional.of(methodName + "Result");
    }

    private boolean renameLocalIdentifier(
            CallableDeclaration<?> callable,
            Node declaration,
            String oldName,
            String proposedName,
            Set<String> declaredNames,
            List<VariableDeclarator> localVariables,
            List<Parameter> parameters) {
        if (!isDecompiledName(oldName) || !isUsefulInferredName(proposedName)
                || proposedName.equals(oldName) || declaredNames.contains(proposedName)) {
            return false;
        }
        long declarationsWithOldName = localVariables.stream()
                .filter(variable -> variable.getNameAsString().equals(oldName)).count()
                + parameters.stream().filter(parameter -> parameter.getNameAsString().equals(oldName)).count();
        if (declarationsWithOldName != 1) {
            return false;
        }
        if (declaration instanceof VariableDeclarator variable) {
            variable.setName(proposedName);
        } else if (declaration instanceof Parameter parameter) {
            parameter.setName(proposedName);
        } else {
            return false;
        }
        callable.findAll(NameExpr.class).stream()
                .filter(expression -> expression.getNameAsString().equals(oldName))
                .filter(expression -> referencesParameter(expression, callable, oldName))
                .forEach(expression -> expression.setName(proposedName));
        declaredNames.remove(oldName);
        declaredNames.add(proposedName);
        return true;
    }

    private boolean belongsToCallable(Node node, CallableDeclaration<?> callable) {
        return node.findAncestor(CallableDeclaration.class)
                .map(owner -> owner == callable)
                .orElse(false);
    }

    private boolean restoreAliasedFieldReferences(TypeDeclaration<?> type, String qualifiedName) {
        Map<String, String> applicableAliases = new LinkedHashMap<>();
        String owner = qualifiedName;
        Set<String> visited = new HashSet<>();
        while (owner != null && visited.add(owner)) {
            Map<String, String> aliases = fieldAliases.get(owner);
            if (aliases != null) {
                aliases.forEach(applicableAliases::putIfAbsent);
            }
            owner = superClassByClass.get(owner);
        }
        if (applicableAliases.isEmpty()) {
            return false;
        }

        boolean[] changed = {false};
        for (FieldAccessExpr fieldAccess : type.findAll(FieldAccessExpr.class)) {
            if (!belongsToType(fieldAccess, type)) {
                continue;
            }
            String restoredName = applicableAliases.get(fieldAccess.getNameAsString());
            if (restoredName == null) {
                continue;
            }
            boolean sameClassCrossInstance = fieldAliases.getOrDefault(qualifiedName, Map.of())
                    .containsKey(fieldAccess.getNameAsString());
            if (fieldAccess.getScope().isThisExpr()
                    || fieldAccess.getScope().isSuperExpr()
                    || sameClassCrossInstance) {
                fieldAccess.setName(restoredName);
                stats.inheritedFieldReferencesRenamed++;
                changed[0] = true;
            }
        }
        return changed[0];
    }

    private void buildTypeHierarchy(List<Path> sources) {
        for (Path source : sources) {
            try {
                CompilationUnit unit = StaticJavaParser.parse(source);
                String packageName = unit.getPackageDeclaration()
                        .map(declaration -> declaration.getNameAsString())
                        .orElse("");
                Map<String, String> explicitImports = unit.getImports().stream()
                        .filter(importDeclaration -> !importDeclaration.isAsterisk())
                        .filter(importDeclaration -> !importDeclaration.isStatic())
                        .collect(Collectors.toMap(
                                importDeclaration -> simpleName(importDeclaration.getNameAsString()),
                                importDeclaration -> importDeclaration.getNameAsString(),
                                (left, right) -> left));
                for (TypeDeclaration<?> type : unit.getTypes()) {
                    if (!type.isClassOrInterfaceDeclaration()
                            || type.asClassOrInterfaceDeclaration().getExtendedTypes().isEmpty()) {
                        continue;
                    }
                    String className = packageName.isEmpty()
                            ? type.getNameAsString()
                            : packageName + "." + type.getNameAsString();
                    String extendedName = type.asClassOrInterfaceDeclaration()
                            .getExtendedTypes(0)
                            .getNameWithScope();
                    String resolvedSuper = extendedName.contains(".")
                            ? extendedName
                            : explicitImports.getOrDefault(
                            extendedName,
                            packageName.isEmpty() ? extendedName : packageName + "." + extendedName);
                    superClassByClass.put(className, resolvedSuper);
                }
            } catch (IOException | ParseProblemException exception) {
                // The normal source pass reports parse failures with file context.
            }
        }
    }

    private Map<String, Map<String, String>> readFieldAliases(Path aliasFile) throws IOException {
        if (aliasFile == null) {
            return Map.of();
        }
        Map<String, Map<String, String>> aliases = new LinkedHashMap<>();
        for (String line : Files.readAllLines(aliasFile, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            int equals = trimmed.indexOf('=');
            int fieldSeparator = trimmed.lastIndexOf('.', equals);
            if (equals < 1 || fieldSeparator < 1) {
                throw new IllegalArgumentException("Invalid field alias: " + line);
            }
            String className = trimmed.substring(0, fieldSeparator);
            String oldName = trimmed.substring(fieldSeparator + 1, equals);
            String newName = trimmed.substring(equals + 1);
            aliases.computeIfAbsent(className, ignored -> new LinkedHashMap<>())
                    .put(oldName, newName);
        }
        return aliases;
    }

    private Optional<String> inferNameFromExpression(Expression expression) {
        if (expression.isEnclosedExpr()) {
            return inferNameFromExpression(expression.asEnclosedExpr().getInner());
        }
        if (expression instanceof CastExpr cast) {
            // A concrete cast carries more semantic information than the
            // source expression. For example, `(TemplateAction) action`
            // should become `templateAction`, not collide with `action` and
            // remain as a decompiler-generated name.
            return inferNameFromType(cast.getType().asString())
                    .or(() -> inferNameFromExpression(cast.getExpression()));
        }
        if (expression.isNameExpr()) {
            return Optional.of(expression.asNameExpr().getNameAsString());
        }
        if (expression instanceof ObjectCreationExpr creation) {
            return Optional.of(lowerCamel(simpleName(creation.getType().getNameAsString())));
        }
        if (expression instanceof MethodCallExpr call) {
            Optional<String> beanCollection = inferBeanCollectionName(call);
            if (beanCollection.isPresent()) {
                return beanCollection;
            }
            if (call.getNameAsString().equals("get")
                    && !call.getArguments().isEmpty()
                    && call.getArgument(0).isStringLiteralExpr()) {
                Optional<String> keyName = identifierFromKey(
                        call.getArgument(0).asStringLiteralExpr().getValue());
                if (keyName.isPresent()) {
                    return keyName;
                }
            }
            if (call.getNameAsString().equals("getBean")
                    && !call.getArguments().isEmpty()
                    && call.getArgument(0).isClassExpr()) {
                return Optional.of(lowerCamel(simpleName(
                        call.getArgument(0).asClassExpr().getType().asString())));
            }
            String methodName = call.getNameAsString();
            Optional<String> transformedText = switch (methodName) {
                case "trim" -> Optional.of("trimmedText");
                case "toUpperCase" -> Optional.of("uppercaseText");
                case "toLowerCase" -> Optional.of("lowercaseText");
                case "substring" -> Optional.of("substring");
                case "split" -> Optional.of("parts");
                case "replace", "replaceAll" -> Optional.of("replacedText");
                default -> Optional.empty();
            };
            if (transformedText.isPresent()) {
                return transformedText;
            }
            for (String prefix : List.of("get", "find", "load", "build", "create", "read", "parse")) {
                if (methodName.startsWith(prefix) && methodName.length() > prefix.length()) {
                    String suffix = methodName.substring(prefix.length());
                    if (Character.isUpperCase(suffix.charAt(0))) {
                        return Optional.of(lowerCamel(suffix));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Optional<String> identifierFromKey(String key) {
        if (key == null || key.isBlank()) {
            return Optional.empty();
        }
        String[] words = key.trim().split("[^A-Za-z0-9_$]+");
        StringBuilder identifier = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (identifier.length() == 0) {
                identifier.append(lowerCamel(word));
            } else {
                identifier.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
        }
        String candidate = identifier.toString();
        return SourceVersion.isIdentifier(candidate) && !SourceVersion.isKeyword(candidate)
                ? Optional.of(candidate)
                : Optional.empty();
    }

    private Optional<String> inferBeanCollectionName(MethodCallExpr call) {
        MethodCallExpr current = call;
        while (true) {
            if (current.getNameAsString().equals("getBeansOfType")
                    && current.getArguments().size() == 1
                    && current.getArgument(0).isClassExpr()) {
                String elementName = lowerCamel(simpleName(
                        current.getArgument(0).asClassExpr().getType().asString()));
                return Optional.of(pluralize(elementName));
            }
            if (current.getScope().isEmpty() || !current.getScope().get().isMethodCallExpr()) {
                return Optional.empty();
            }
            current = current.getScope().get().asMethodCallExpr();
        }
    }

    private Optional<String> inferNameFromType(String type) {
        String compact = type.replaceAll("\\s+", "");
        int genericStart = compact.indexOf('<');
        String rawType = simpleName(genericStart < 0 ? compact : compact.substring(0, genericStart));
        if (Set.of("List", "Set", "Collection", "Iterable").contains(rawType) && genericStart > 0) {
            int genericEnd = compact.lastIndexOf('>');
            if (genericEnd > genericStart) {
                String elementType = compact.substring(genericStart + 1, genericEnd);
                int comma = elementType.indexOf(',');
                if (comma >= 0) {
                    elementType = elementType.substring(0, comma);
                }
                return Optional.of(pluralize(lowerCamel(simpleName(elementType))));
            }
        }
        if (Set.of(
                "String", "Object", "Class", "Date", "Map", "List", "Set", "Collection",
                "Iterable", "Iterator", "ThreadLocal", "Boolean", "Byte", "Short", "Integer",
                "Long", "Float", "Double", "BigDecimal", "BigInteger", "char", "byte", "short",
                "int", "long", "float", "double", "boolean").contains(rawType)) {
            return Optional.empty();
        }
        return Optional.of(lowerCamel(rawType));
    }

    /**
     * Neutral names for common Java types. These deliberately describe only
     * representation, not unproven business meaning, but are still easier to
     * follow than var0/var1. Scope-conflict checks limit each name to one
     * declaration per callable.
     */
    private Optional<String> inferFallbackNameFromType(String type) {
        String compact = type.replaceAll("\\s+", "");
        if (compact.endsWith("[]")) {
            return Optional.of(compact.equals("byte[]") ? "bytes" : "values");
        }
        int genericStart = compact.indexOf('<');
        String rawType = simpleName(genericStart < 0 ? compact : compact.substring(0, genericStart));
        return switch (rawType) {
            case "String", "StringBuffer", "StringBuilder", "char", "Character" -> Optional.of("text");
            case "Object" -> Optional.of("objectValue");
            case "Class" -> Optional.of("valueType");
            case "Date", "Calendar" -> Optional.of("dateValue");
            case "Map", "HashMap", "LinkedHashMap", "ConcurrentHashMap" -> Optional.of("valuesByKey");
            case "List", "ArrayList", "LinkedList", "Collection", "Iterable" -> Optional.of("items");
            case "Set", "HashSet", "LinkedHashSet" -> Optional.of("uniqueItems");
            case "Iterator" -> Optional.of("iterator");
            case "boolean", "Boolean" -> Optional.of("flag");
            case "byte", "Byte" -> Optional.of("byteValue");
            case "short", "Short" -> Optional.of("shortValue");
            case "int", "Integer" -> Optional.of("number");
            case "long", "Long" -> Optional.of("longValue");
            case "float", "Float" -> Optional.of("floatValue");
            case "double", "Double" -> Optional.of("doubleValue");
            case "BigDecimal" -> Optional.of("decimalValue");
            case "BigInteger" -> Optional.of("integerValue");
            default -> Optional.empty();
        };
    }

    private String adjustNameForType(String candidate, String type) {
        String rawType = simpleName(type);
        if (rawType.equals("DataSource")) {
            return "dataSource";
        }
        if (rawType.equals("Log") || rawType.equals("Logger")) {
            return "logger";
        }
        return candidate;
    }

    private String simpleName(String type) {
        String withoutArray = type.replace("[]", "").replace("...", "");
        int generic = withoutArray.indexOf('<');
        if (generic >= 0) {
            withoutArray = withoutArray.substring(0, generic);
        }
        int dot = withoutArray.lastIndexOf('.');
        return dot < 0 ? withoutArray : withoutArray.substring(dot + 1);
    }

    private String lowerCamel(String value) {
        return value.isEmpty() ? value : Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    private String pluralize(String value) {
        if (value.endsWith("y") && value.length() > 1) {
            return value.substring(0, value.length() - 1) + "ies";
        }
        if (value.endsWith("s")) {
            return value;
        }
        return value + "s";
    }

    private boolean isUsefulInferredName(String name) {
        return name != null
                && !name.isBlank()
                && SourceVersion.isIdentifier(name)
                && !SourceVersion.isKeyword(name)
                && !isDecompiledName(name)
                && !Set.of(
                "value", "result", "data", "item", "object", "bean", "global", "default", "instance",
                "arrayList", "linkedList", "hashMap", "hashSet", "linkedHashMap", "linkedHashSet",
                "concurrentHashMap", "copyOnWriteArrayList", "threadLocal").contains(name);
    }

    private Optional<NamePair> inferGetterField(MethodDeclaration method) {
        if (!method.getParameters().isEmpty() || method.getBody().isEmpty()) {
            return Optional.empty();
        }
        String property = propertyFromAccessor(method.getNameAsString(), "get", "is");
        if (property == null || method.getBody().get().getStatements().size() != 1) {
            return Optional.empty();
        }
        Statement statement = method.getBody().get().getStatement(0);
        if (!statement.isReturnStmt()) {
            return Optional.empty();
        }
        Expression expression = statement.asReturnStmt().getExpression().orElse(null);
        return fieldName(expression).map(field -> new NamePair(field, property));
    }

    private Optional<NamePair> inferSetterField(MethodDeclaration method) {
        if (method.getParameters().size() != 1 || method.getBody().isEmpty()) {
            return Optional.empty();
        }
        String property = propertyFromAccessor(method.getNameAsString(), "set");
        if (property == null || method.getBody().get().getStatements().size() != 1) {
            return Optional.empty();
        }
        Statement statement = method.getBody().get().getStatement(0);
        if (!(statement instanceof ExpressionStmt expressionStmt)
                || !expressionStmt.getExpression().isAssignExpr()) {
            return Optional.empty();
        }
        AssignExpr assignment = expressionStmt.getExpression().asAssignExpr();
        String parameterName = method.getParameter(0).getNameAsString();
        if (!assignment.getValue().isNameExpr()
                || !assignment.getValue().asNameExpr().getNameAsString().equals(parameterName)) {
            return Optional.empty();
        }
        return fieldName(assignment.getTarget()).map(field -> new NamePair(field, property));
    }

    private Optional<String> fieldName(Expression expression) {
        if (expression == null) {
            return Optional.empty();
        }
        if (expression.isFieldAccessExpr()) {
            FieldAccessExpr field = expression.asFieldAccessExpr();
            if (field.getScope().isThisExpr() || field.getScope().isNameExpr()) {
                return Optional.of(field.getNameAsString());
            }
        }
        if (expression.isNameExpr()) {
            return Optional.of(expression.asNameExpr().getNameAsString());
        }
        return Optional.empty();
    }

    private void renameParameter(
            CallableDeclaration<?> callable,
            Parameter parameter,
            String oldName,
            String newName) {
        parameter.setName(newName);
        callable.findAll(NameExpr.class).stream()
                .filter(expression -> expression.getNameAsString().equals(oldName))
                .filter(expression -> referencesParameter(expression, callable, oldName))
                .forEach(expression -> expression.setName(newName));
    }

    private boolean referencesParameter(
            NameExpr expression,
            CallableDeclaration<?> declaringCallable,
            String oldName) {
        Node cursor = expression;
        while (true) {
            Optional<CallableDeclaration> owner = cursor.findAncestor(CallableDeclaration.class);
            if (owner.isEmpty()) {
                return false;
            }
            CallableDeclaration<?> callable = owner.get();
            if (callable == declaringCallable) {
                return true;
            }
            boolean shadowsOuterParameter = callable.getParameters().stream()
                    .anyMatch(parameter -> parameter.getNameAsString().equals(oldName))
                    || callable.findAll(VariableDeclarator.class).stream()
                    .filter(variable -> belongsToCallable(variable, callable))
                    .anyMatch(variable -> variable.getNameAsString().equals(oldName));
            if (shadowsOuterParameter) {
                return false;
            }
            cursor = callable;
        }
    }

    private void renameField(
            TypeDeclaration<?> type,
            VariableDeclarator field,
            String oldName,
            String newName) {
        boolean staticField = field.findAncestor(FieldDeclaration.class)
                .map(FieldDeclaration::isStatic)
                .orElse(false);
        field.setName(newName);
        type.findAll(FieldAccessExpr.class).stream()
                .filter(expression -> expression.getNameAsString().equals(oldName))
                .filter(expression -> belongsToType(expression, type))
                .filter(expression -> expression.getScope().isThisExpr()
                        || expression.getScope().isNameExpr()
                        && expression.getScope().asNameExpr().getNameAsString()
                        .equals(type.getNameAsString()))
                .forEach(expression -> expression.setName(newName));
        type.findAll(NameExpr.class).stream()
                .filter(expression -> expression.getNameAsString().equals(oldName))
                .filter(expression -> belongsToType(expression, type))
                .forEach(expression -> expression.replace(new FieldAccessExpr(
                        staticField ? new NameExpr(type.getNameAsString()) : new ThisExpr(),
                        newName)));
    }

    private boolean belongsToType(Node node, TypeDeclaration<?> type) {
        return node.findAncestor(TypeDeclaration.class)
                .map(owner -> owner == type)
                .orElse(false);
    }

    private Optional<VariableDeclarator> findDirectField(TypeDeclaration<?> type, String name) {
        return type.getFields().stream()
                .flatMap(field -> field.getVariables().stream())
                .filter(variable -> variable.getNameAsString().equals(name))
                .findFirst();
    }

    private boolean hasLocalNameConflict(TypeDeclaration<?> type, String fieldName) {
        return type.getMethods().stream().anyMatch(method -> method.getParameters().stream()
                        .anyMatch(parameter -> parameter.getNameAsString().equals(fieldName))
                || method.findAll(VariableDeclarator.class).stream()
                        .anyMatch(variable -> variable.getNameAsString().equals(fieldName)))
                || type.getConstructors().stream().anyMatch(constructor -> constructor.getParameters().stream()
                        .anyMatch(parameter -> parameter.getNameAsString().equals(fieldName))
                || constructor.findAll(VariableDeclarator.class).stream()
                        .anyMatch(variable -> variable.getNameAsString().equals(fieldName)));
    }

    private boolean nameAlreadyDeclared(CallableDeclaration<?> callable, String name, Parameter current) {
        return callable.getParameters().stream()
                .filter(parameter -> parameter != current)
                .anyMatch(parameter -> parameter.getNameAsString().equals(name))
                || callable.findAll(VariableDeclarator.class).stream()
                .anyMatch(variable -> variable.getNameAsString().equals(name));
    }

    private void mergeInference(
            Map<String, String> inferredNames,
            Set<String> conflicts,
            NamePair inference) {
        String previous = inferredNames.putIfAbsent(inference.oldName, inference.newName);
        if (previous != null && !previous.equals(inference.newName)) {
            conflicts.add(inference.oldName);
        }
    }

    private Optional<ClassDoc> readClassDoc(String qualifiedName) {
        String entryName = qualifiedName.replace('.', '/') + ".html";
        ZipEntry entry = javadocArchive.getEntry(entryName);
        if (entry == null) {
            return Optional.empty();
        }
        try (InputStream input = javadocArchive.getInputStream(entry)) {
            Document document = Jsoup.parse(input, StandardCharsets.UTF_8.name(), "");
            String classDescription = extractClassDescription(document);
            List<MethodDoc> methods = extractMethodDocs(document);
            return Optional.of(new ClassDoc(classDescription, methods));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read " + entryName, exception);
        }
    }

    private String extractClassDescription(Document document) {
        Element description = document.selectFirst("div.description li.blockList > div.block");
        return description == null ? "" : cleanText(description.text());
    }

    private List<MethodDoc> extractMethodDocs(Document document) {
        List<MethodDoc> methods = new ArrayList<>();
        for (Element block : document.select("li.blockList")) {
            Element heading = directChild(block, "h4");
            Element signature = directChild(block, "pre");
            if (heading == null || signature == null || !signature.text().contains("(")) {
                continue;
            }
            String name = heading.text().trim();
            List<String> declarations = splitParameters(betweenParentheses(signature.text()));
            List<String> parameterNames = new ArrayList<>();
            List<String> parameterTypes = new ArrayList<>();
            boolean valid = true;
            for (String declaration : declarations) {
                ParameterDoc parameter = parseParameter(declaration);
                if (parameter == null) {
                    valid = false;
                    break;
                }
                parameterNames.add(parameter.name);
                parameterTypes.add(parameter.type);
            }
            if (!valid) {
                continue;
            }
            Element description = directChild(block, "div", "block");
            methods.add(new MethodDoc(
                    name,
                    parameterNames,
                    parameterTypes,
                    description == null ? "" : cleanText(description.text())));
        }
        return methods;
    }

    private Element directChild(Element parent, String tag, String... classes) {
        for (Element child : parent.children()) {
            if (!child.tagName().equals(tag)) {
                continue;
            }
            boolean matches = true;
            for (String className : classes) {
                matches &= child.hasClass(className);
            }
            if (matches) {
                return child;
            }
        }
        return null;
    }

    private String betweenParentheses(String signature) {
        int start = signature.indexOf('(');
        int end = signature.lastIndexOf(')');
        return start >= 0 && end > start ? signature.substring(start + 1, end).trim() : "";
    }

    private List<String> splitParameters(String parameters) {
        if (parameters.isBlank()) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        int genericDepth = 0;
        int arrayDepth = 0;
        int start = 0;
        for (int index = 0; index < parameters.length(); index++) {
            char character = parameters.charAt(index);
            if (character == '<') {
                genericDepth++;
            } else if (character == '>') {
                genericDepth--;
            } else if (character == '[') {
                arrayDepth++;
            } else if (character == ']') {
                arrayDepth--;
            } else if (character == ',' && genericDepth == 0 && arrayDepth == 0) {
                result.add(parameters.substring(start, index).trim());
                start = index + 1;
            }
        }
        result.add(parameters.substring(start).trim());
        return result;
    }

    private ParameterDoc parseParameter(String declaration) {
        String compact = declaration.replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
        int separator = compact.lastIndexOf(' ');
        if (separator < 1 || separator == compact.length() - 1) {
            return null;
        }
        String name = compact.substring(separator + 1).trim();
        String type = normalizeType(compact.substring(0, separator));
        return new ParameterDoc(name, type);
    }

    private static String normalizeType(String type) {
        String normalized = type.replace("...", "[]")
                .replaceAll("@[A-Za-z0-9_$.]+(?:\\([^)]*\\))?\\s*", "")
                .replaceAll("\\s+", "")
                .replace("?extends", "")
                .replace("?super", "");
        StringBuilder result = new StringBuilder();
        StringBuilder identifier = new StringBuilder();
        for (int index = 0; index < normalized.length(); index++) {
            char character = normalized.charAt(index);
            if (Character.isJavaIdentifierPart(character) || character == '.') {
                identifier.append(character);
            } else {
                appendSimpleIdentifier(result, identifier);
                result.append(character);
            }
        }
        appendSimpleIdentifier(result, identifier);
        return result.toString();
    }

    private static void appendSimpleIdentifier(StringBuilder result, StringBuilder identifier) {
        if (identifier.length() == 0) {
            return;
        }
        String value = identifier.toString();
        int dot = value.lastIndexOf('.');
        result.append(dot < 0 ? value : value.substring(dot + 1));
        identifier.setLength(0);
    }

    private String propertyFromAccessor(String methodName, String... prefixes) {
        for (String prefix : prefixes) {
            if (methodName.startsWith(prefix) && methodName.length() > prefix.length()) {
                String suffix = methodName.substring(prefix.length());
                if (Character.isUpperCase(suffix.charAt(0))) {
                    return Character.toLowerCase(suffix.charAt(0)) + suffix.substring(1);
                }
            }
        }
        return null;
    }

    private boolean isGeneratedSource(String source, String relative) {
        String head = source.substring(0, Math.min(source.length(), 1500)).toLowerCase(Locale.ROOT);
        return head.contains("generated from")
                || head.contains("generated by antlr")
                || head.contains("do not edit this file")
                || source.contains("extends Parser {")
                || source.contains("extends Lexer {")
                || source.contains("extends ParserRuleContext {")
                // These two files are generated by the URule ANTLR grammar.
                // Other *Visitor classes in this source tree contain handwritten
                // rule-building logic and must not be excluded by suffix alone.
                || relative.endsWith("/RuleParserBaseVisitor.java")
                || relative.endsWith("\\RuleParserBaseVisitor.java")
                || relative.endsWith("/RuleParserVisitor.java")
                || relative.endsWith("\\RuleParserVisitor.java");
    }

    private boolean isDecompiledName(String name) {
        return DECOMPILED_NAME.matcher(name).matches();
    }

    private String cleanText(String text) {
        return text.replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
    }

    private String firstLine(String text) {
        int newline = text.indexOf('\n');
        return newline < 0 ? text : text.substring(0, newline);
    }

    @Override
    public void close() throws IOException {
        javadocArchive.close();
    }

    private record NamePair(String oldName, String newName) {
    }

    private record ParameterDoc(String name, String type) {
    }

    private record MethodDoc(
            String name,
            List<String> parameterNames,
            List<String> parameterTypes,
            String description) {
    }

    private record MethodSignature(String name, List<String> parameterTypes) {
    }

    private record MethodCallKey(String name, int arity) {
    }

    private record ClassDoc(String description, List<MethodDoc> methods) {
    }

    private static final class Stats {
        int changedFiles;
        int unchangedFiles;
        int generatedFilesSkipped;
        int syntheticFilesSkipped;
        int classesWithoutJavadoc;
        int ambiguousCallables;
        int parametersRenamed;
        int privateParametersRenamed;
        int localVariablesRenamed;
        int fieldsRenamed;
        int classCommentsRestored;
        int methodCommentsRestored;
        int inheritedFieldReferencesRenamed;
        int protectedMethodsRenamed;
        int protectedMethodCallsRenamed;
        int privateMethodsRenamed;
        int privateMethodCallsRenamed;
        int privateMethodGroupsSkipped;
        int servletJsonCallsRepaired;
        final Map<String, String> parseFailures = new LinkedHashMap<>();
        final List<String> fieldRenameDetails = new ArrayList<>();
        final List<String> protectedMethodRenameDetails = new ArrayList<>();
        final List<String> privateMethodRenameDetails = new ArrayList<>();

        String describe(boolean applied, boolean verbose) {
            StringBuilder summary = new StringBuilder();
            summary.append(applied ? "Applied readability restoration" : "Readability restoration preview")
                    .append(System.lineSeparator());
            summary.append("changed files: ").append(changedFiles).append(System.lineSeparator());
            summary.append("unchanged files: ").append(unchangedFiles).append(System.lineSeparator());
            summary.append("parameters renamed: ").append(parametersRenamed).append(System.lineSeparator());
            summary.append("private parameters renamed: ").append(privateParametersRenamed)
                    .append(System.lineSeparator());
            summary.append("local variables renamed: ").append(localVariablesRenamed)
                    .append(System.lineSeparator());
            summary.append("fields renamed: ").append(fieldsRenamed).append(System.lineSeparator());
            summary.append("class comments restored: ").append(classCommentsRestored).append(System.lineSeparator());
            summary.append("method comments restored: ").append(methodCommentsRestored).append(System.lineSeparator());
            summary.append("inherited field references renamed: ")
                    .append(inheritedFieldReferencesRenamed)
                    .append(System.lineSeparator());
            summary.append("protected methods renamed: ").append(protectedMethodsRenamed)
                    .append(System.lineSeparator());
            summary.append("protected method calls renamed: ").append(protectedMethodCallsRenamed)
                    .append(System.lineSeparator());
            summary.append("private methods renamed: ").append(privateMethodsRenamed)
                    .append(System.lineSeparator());
            summary.append("private method calls renamed: ").append(privateMethodCallsRenamed)
                    .append(System.lineSeparator());
            summary.append("private method groups skipped: ").append(privateMethodGroupsSkipped)
                    .append(System.lineSeparator());
            summary.append("servlet JSON calls repaired: ").append(servletJsonCallsRepaired)
                    .append(System.lineSeparator());
            summary.append("generated files skipped: ").append(generatedFilesSkipped).append(System.lineSeparator());
            summary.append("synthetic files skipped: ").append(syntheticFilesSkipped).append(System.lineSeparator());
            summary.append("classes without exact Javadoc: ").append(classesWithoutJavadoc).append(System.lineSeparator());
            summary.append("ambiguous callables skipped: ").append(ambiguousCallables).append(System.lineSeparator());
            summary.append("parse failures: ").append(parseFailures.size());
            parseFailures.forEach((file, error) -> summary.append(System.lineSeparator())
                    .append("  ").append(file).append(": ").append(error));
            if (verbose && !fieldRenameDetails.isEmpty()) {
                summary.append(System.lineSeparator()).append("field rename evidence:");
                fieldRenameDetails.stream().sorted().forEach(detail -> summary.append(System.lineSeparator())
                        .append("  ").append(detail));
            }
            if (verbose && !protectedMethodRenameDetails.isEmpty()) {
                summary.append(System.lineSeparator()).append("protected method rename evidence:");
                protectedMethodRenameDetails.stream().sorted().forEach(detail -> summary
                        .append(System.lineSeparator()).append("  ").append(detail));
            }
            if (verbose && !privateMethodRenameDetails.isEmpty()) {
                summary.append(System.lineSeparator()).append("private method rename evidence:");
                privateMethodRenameDetails.stream().sorted().forEach(detail -> summary
                        .append(System.lineSeparator()).append("  ").append(detail));
            }
            return summary.toString();
        }
    }

    private record Arguments(
            Path sourceRoot,
            Path javadocJar,
            boolean apply,
            boolean verbose,
            boolean aliasesOnly,
            boolean restoreProtectedMethods,
            boolean methodsOnly,
            boolean restoreLocals,
            boolean restoreAllPlaceholders,
            boolean includeGenerated,
            boolean includeSynthetic,
            boolean restorePrivateMethods,
            boolean repairServletJsonCalls,
            boolean restorePrivateFields,
            Path fieldAliasFile,
            String includePrefix) {
        static Arguments parse(String[] args) {
            Map<String, String> values = new HashMap<>();
            boolean apply = false;
            boolean verbose = false;
            boolean aliasesOnly = false;
            boolean restoreProtectedMethods = false;
            boolean methodsOnly = false;
            boolean restoreLocals = false;
            boolean restoreAllPlaceholders = false;
            boolean includeGenerated = false;
            boolean includeSynthetic = false;
            boolean restorePrivateMethods = false;
            boolean repairServletJsonCalls = false;
            boolean restorePrivateFields = false;
            for (int index = 0; index < args.length; index++) {
                if (args[index].equals("--apply")) {
                    apply = true;
                } else if (args[index].equals("--verbose")) {
                    verbose = true;
                } else if (args[index].equals("--aliases-only")) {
                    aliasesOnly = true;
                } else if (args[index].equals("--restore-protected-methods")) {
                    restoreProtectedMethods = true;
                } else if (args[index].equals("--methods-only")) {
                    methodsOnly = true;
                } else if (args[index].equals("--restore-locals")) {
                    restoreLocals = true;
                } else if (args[index].equals("--restore-all-placeholders")) {
                    restoreAllPlaceholders = true;
                    restoreLocals = true;
                } else if (args[index].equals("--include-generated")) {
                    includeGenerated = true;
                } else if (args[index].equals("--include-synthetic")) {
                    includeSynthetic = true;
                } else if (args[index].equals("--restore-private-methods")) {
                    restorePrivateMethods = true;
                } else if (args[index].equals("--repair-servlet-json-calls")) {
                    repairServletJsonCalls = true;
                } else if (args[index].equals("--restore-private-fields")) {
                    restorePrivateFields = true;
                } else if (args[index].startsWith("--")) {
                    if (index + 1 >= args.length) {
                        throw new IllegalArgumentException("Missing value for " + args[index]);
                    }
                    values.put(args[index], args[++index]);
                }
            }
            String sourceRoot = require(values, "--source-root");
            String javadocJar = require(values, "--javadoc-jar");
            return new Arguments(
                    Path.of(sourceRoot),
                    Path.of(javadocJar),
                    apply,
                    verbose,
                    aliasesOnly,
                    restoreProtectedMethods,
                    methodsOnly,
                    restoreLocals,
                    restoreAllPlaceholders,
                    includeGenerated,
                    includeSynthetic,
                    restorePrivateMethods,
                    repairServletJsonCalls,
                    restorePrivateFields,
                    values.containsKey("--field-alias-file")
                            ? Path.of(values.get("--field-alias-file"))
                            : null,
                    values.getOrDefault("--include-prefix", ""));
        }

        private static String require(Map<String, String> values, String name) {
            String value = values.get(name);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(name + " is required");
            }
            return value;
        }
    }
}
