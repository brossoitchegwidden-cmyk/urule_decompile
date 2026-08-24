package local.urule.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadabilityRestorerTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void restoresHighConfidenceLocalNamesInHandwrittenVisitor() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("source");
        Path source = sourceRoot.resolve("sample/BuildRulesVisitor.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, """
                package sample;

                import java.util.Map;

                class BuildRulesVisitor {
                    private String parseLabel(String var0) {
                        String var1 = var0.trim();
                        for (int var2 = 0; var2 < 1; var2++) {
                            System.out.println(var2);
                        }
                        return var1;
                    }

                    private void inspect(Action action, Map var3) {
                        TemplateAction var4 = (TemplateAction) action;
                        Boolean var5 = null;
                        var5 = (Boolean) var3.get("enabled");
                        System.out.println(var4 + ":" + var5);
                    }

                    static class Action {}
                    static class TemplateAction extends Action {}
                }
                """, StandardCharsets.UTF_8);

        runRestorer(sourceRoot);

        String restored = Files.readString(source, StandardCharsets.UTF_8);
        assertTrue(restored.contains("parseLabel(String text)"));
        assertTrue(restored.contains("String label = text.trim()"));
        assertTrue(restored.contains("int index = 0"));
        assertTrue(restored.contains("Map valuesByKey"));
        assertTrue(restored.contains("TemplateAction templateAction"));
        assertTrue(restored.contains("Boolean enabled"));
        assertFalse(restored.matches("(?s).*\\bvar[0-9]+\\b.*"));
    }

    @Test
    void stillSkipsActualGeneratedVisitorInterface() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("generated-source");
        Path source = sourceRoot.resolve("sample/RuleParserVisitor.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, """
                package sample;
                interface RuleParserVisitor {
                    default String parseLabel(String var0) {
                        String var1 = var0.trim();
                        return var1;
                    }
                }
                """, StandardCharsets.UTF_8);

        runRestorer(sourceRoot);

        String restored = Files.readString(source, StandardCharsets.UTF_8);
        assertTrue(restored.contains("String var1 = var0.trim()"));
    }

    @Test
    void keepsCompatibilityBridgeWhenReadableSignatureAlreadyExists() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("bridge-source");
        Path source = sourceRoot.resolve("sample/CompatibilityHandler.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, """
                package sample;

                class CompatibilityHandler {
                    protected void writeObjectToJson(Response response, Object value) {}

                    @Deprecated
                    protected void a(Response response, Object value) {
                        writeObjectToJson(response, value);
                    }
                }

                class Response {}
                """, StandardCharsets.UTF_8);
        Path javadoc = createJavadoc(Map.of(
                "sample/CompatibilityHandler.html", methodJavadoc(
                        "writeObjectToJson",
                        "protected void writeObjectToJson(sample.Response response, java.lang.Object value)")));

        runProtectedRestorer(sourceRoot, javadoc);

        String restored = Files.readString(source, StandardCharsets.UTF_8);
        assertTrue(restored.contains("protected void writeObjectToJson(Response response, Object value)"));
        assertTrue(restored.contains("protected void a(Response response, Object value)"));
    }

    @Test
    void doesNotRenameInheritedOverloadCallsByArityAlone() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("hierarchy-source");
        Path packageRoot = sourceRoot.resolve("sample");
        Files.createDirectories(packageRoot);
        Files.writeString(packageRoot.resolve("GrandParent.java"), """
                package sample;
                import java.util.Map;
                class GrandParent {
                    protected int a(Result result, Map values) { return 0; }
                }
                class Result {}
                """, StandardCharsets.UTF_8);
        Files.writeString(packageRoot.resolve("Parent.java"), """
                package sample;
                class Parent extends GrandParent {
                    protected void a(Connection connection, Resolver resolver) {}
                }
                class Connection {}
                class Resolver {}
                """, StandardCharsets.UTF_8);
        Path child = packageRoot.resolve("Child.java");
        Files.writeString(child, """
                package sample;
                import java.util.Map;
                class Child extends Parent {
                    int finish(Result result, Map values) {
                        return this.a(result, values);
                    }
                }
                """, StandardCharsets.UTF_8);
        Path javadoc = createJavadoc(Map.of(
                "sample/Parent.html", methodJavadoc(
                        "prepareStmt",
                        "protected void prepareStmt(sample.Connection connection, sample.Resolver resolver)")));

        runProtectedRestorer(sourceRoot, javadoc);

        String restoredParent = Files.readString(packageRoot.resolve("Parent.java"), StandardCharsets.UTF_8);
        String restoredChild = Files.readString(child, StandardCharsets.UTF_8);
        assertTrue(restoredParent.contains("protected void prepareStmt(Connection connection, Resolver resolver)"));
        assertTrue(restoredChild.contains("this.a(result, values)"));
        assertFalse(restoredChild.contains("prepareStmt(result, values)"));
    }

    @Test
    void exhaustivelyRemovesPlaceholdersWithoutDebugLocalMetadata() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("exhaustive-source");
        Path source = sourceRoot.resolve("sample/ExhaustiveExample.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, """
                package sample;

                class ExhaustiveExample {
                    static {
                        String var8 = "ready";
                        try {
                            System.out.println(var8);
                        } catch (RuntimeException var9) {
                            throw var9;
                        }
                    }

                    public Object convert(Object var0, String var1) {
                        Object var2 = null;
                        if (var0 != null) {
                            String var3 = var1.trim();
                            var2 = var3;
                        }
                        if (var1.isEmpty()) {
                            Integer var3 = 1;
                            var2 = var3;
                        }
                        return var2;
                    }
                }
                """, StandardCharsets.UTF_8);

        runExhaustiveRestorer(sourceRoot, false);

        String restored = Files.readString(source, StandardCharsets.UTF_8);
        assertFalse(restored.matches("(?s).*\\bvar[0-9]+\\b.*"));
        assertTrue(restored.contains("Object objectValue"));
        assertTrue(restored.contains("String text"));
        assertTrue(restored.contains("Object convertResult"));
        assertTrue(restored.contains("temporaryValue"));
    }

    @Test
    void generatedSourcesRequireExplicitOptInForExhaustiveRestoration() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("generated-opt-in");
        Path source = sourceRoot.resolve("sample/GeneratedParser.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, """
                // Generated by ANTLR
                package sample;
                class GeneratedParser {
                    String parse(String var0) {
                        String var1 = var0.trim();
                        return var1;
                    }
                }
                """, StandardCharsets.UTF_8);

        runExhaustiveRestorer(sourceRoot, false);
        assertTrue(Files.readString(source, StandardCharsets.UTF_8).contains("String var1"));

        runExhaustiveRestorer(sourceRoot, true);
        assertFalse(Files.readString(source, StandardCharsets.UTF_8)
                .matches("(?s).*\\bvar[0-9]+\\b.*"));
    }

    @Test
    void renamesPrivateOverloadGroupsWithoutTouchingOtherReceivers() throws Exception {
        Path sourceRoot = temporaryDirectory.resolve("private-method-source");
        Path source = sourceRoot.resolve("sample/PrivateHelpers.java");
        Files.createDirectories(source.getParent());
        Files.writeString(source, """
                package sample;

                class PrivateHelpers {
                    private String a(String text) { return text.trim(); }
                    private String a(int number) { return Integer.toString(number); }
                    private void b() { System.out.println(a(1)); }

                    String render(Other other) {
                        b();
                        return this.a("x") + other.a("y");
                    }
                }

                class Other {
                    String a(String text) { return text; }
                }
                """, StandardCharsets.UTF_8);

        runPrivateMethodRestorer(sourceRoot);

        String restored = Files.readString(source, StandardCharsets.UTF_8);
        assertTrue(restored.contains("private String buildText(String text)"));
        assertTrue(restored.contains("private String buildText(int number)"));
        assertTrue(restored.contains("private void initializeState()"));
        assertTrue(restored.contains("this.buildText(\"x\")"));
        assertTrue(restored.contains("other.a(\"y\")"));
        assertFalse(restored.matches("(?s).*private\\s+[^\\n]+\\s+[a-z]\\s*\\(.*"));
    }

    private void runRestorer(Path sourceRoot) throws Exception {
        Path emptyJavadoc = createEmptyZip();
        ReadabilityRestorer.main(new String[]{
                "--source-root", sourceRoot.toString(),
                "--javadoc-jar", emptyJavadoc.toString(),
                "--methods-only",
                "--restore-locals",
                "--apply"
        });
    }

    private void runProtectedRestorer(Path sourceRoot, Path javadoc) throws Exception {
        ReadabilityRestorer.main(new String[]{
                "--source-root", sourceRoot.toString(),
                "--javadoc-jar", javadoc.toString(),
                "--methods-only",
                "--restore-protected-methods",
                "--apply"
        });
    }

    private void runExhaustiveRestorer(Path sourceRoot, boolean includeGenerated) throws Exception {
        Path emptyJavadoc = createEmptyZip();
        if (includeGenerated) {
            ReadabilityRestorer.main(new String[]{
                    "--source-root", sourceRoot.toString(),
                    "--javadoc-jar", emptyJavadoc.toString(),
                    "--methods-only",
                    "--restore-all-placeholders",
                    "--include-generated",
                    "--include-synthetic",
                    "--apply"
            });
        } else {
            ReadabilityRestorer.main(new String[]{
                    "--source-root", sourceRoot.toString(),
                    "--javadoc-jar", emptyJavadoc.toString(),
                    "--methods-only",
                    "--restore-all-placeholders",
                    "--apply"
            });
        }
    }

    private void runPrivateMethodRestorer(Path sourceRoot) throws Exception {
        Path emptyJavadoc = createEmptyZip();
        ReadabilityRestorer.main(new String[]{
                "--source-root", sourceRoot.toString(),
                "--javadoc-jar", emptyJavadoc.toString(),
                "--methods-only",
                "--restore-private-methods",
                "--apply"
        });
    }

    private Path createJavadoc(Map<String, String> entries) throws IOException {
        Path archive = temporaryDirectory.resolve("javadoc-" + System.nanoTime() + ".jar");
        try (ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(archive))) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                output.putNextEntry(new ZipEntry(entry.getKey()));
                output.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                output.closeEntry();
            }
        }
        return archive;
    }

    private String methodJavadoc(String methodName, String signature) {
        return "<html><body><ul><li class=\"blockList\"><h4>" + methodName
                + "</h4><pre>" + signature + "</pre></li></ul></body></html>";
    }

    private Path createEmptyZip() throws IOException {
        Path archive = temporaryDirectory.resolve("empty-javadoc-" + System.nanoTime() + ".jar");
        try (ZipOutputStream ignored = new ZipOutputStream(Files.newOutputStream(archive))) {
            // A valid empty ZIP is sufficient because this test exercises
            // structural local-name recovery without Javadoc matching.
        }
        return archive;
    }
}
