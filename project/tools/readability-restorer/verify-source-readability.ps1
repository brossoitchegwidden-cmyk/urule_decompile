param(
    [string[]]$SourceRoots = @(
        "urule-core-pro/src/main/java",
        "urule-console-pro/src/main/java"
    )
)

$ErrorActionPreference = "Stop"

$checks = [ordered]@{
    var_placeholders = '\bvar\d+(?:_\d+)*\b'
    single_letter_methods = '^ {3}(?:(?:public|protected|private|static|final|synchronized|abstract|native|default|strictfp)\s+)*(?:<[^;{}]+>\s*)?[\w$.<>?,\[\]]+\s+[A-Za-z]\s*\('
    single_letter_fields = '^ {3}(?:(?:public|protected|private|static|final|transient|volatile)\s+)*(?:[\w$.<>?,\[\]]+\s+)+[A-Za-z](?:\s*=|\s*;)'
    single_letter_types = '^\s*(?:(?:public|protected|private|static|final|abstract|sealed|non-sealed)\s+)*(?:class|interface|enum|record)\s+[A-Za-z](?:\s|<|\{|$)'
    digit_suffixed_fields = '^ {3}(?:(?:public|protected|private|static|final|transient|volatile)\s+)*(?:[\w$.<>?,\[\]]+\s+)+[a-z][A-Za-z]*\d+(?:\s*=|\s*;)'
    mechanical_method_fallbacks = '\b(?:process[A-Z][A-Za-z0-9_$]*Data|handleRequestData|manageCacheData|processInternal\w*|buildTextInternal\w*|buildContent)\s*\('
    mechanical_field_fallbacks = '^ {3}(?:(?:public|protected|private|static|final|transient|volatile)\s+)*(?:[\w$.<>?,\[\]]+\s+)+(?:\w+(?:ValuesByKey|CacheCache|BuilderItems|HolderContext)|items|valuesByKey|threadContext|cache)(?:\s*=|\s*;)'
    generic_private_methods = '^ {3}private\s+(?:static\s+)?(?:<[^;{}]+>\s*)?[\w$.<>?,\[\]]+\s+(?:buildText\d*|executeQuery|importData|exportData|collectItems(?:Internal)?|process(?:ItemsInternal|StringBuilder|String|Long|Log|Project|Sheet)|importElement|resolveElement|resolveObjectInternal)\s*\('
    format_placeholder_fields = '^ {3}(?:(?:public|protected|private|static|final|transient|volatile)\s+)*(?:[\w$.<>?,\[\]]+\s+)+(?:S(?:_S)*\d*|MESSAGE_TEMPLATE\d+)\s*(?:=|;)'
    numeric_anonymous_classes = '\b[A-Za-z_$][\w$]*\$\d+\b'
    direct_print_stack_trace = '\.printStackTrace\s*\(\s*\)\s*;'
    placeholder_comments = '从接口复制的说明|从类复制的说明|\$VF:'
}

$failed = $false
foreach ($check in $checks.GetEnumerator()) {
    $matches = @(& rg -n --pcre2 $check.Value @SourceRoots -g '*.java' 2>$null)
    if ($LASTEXITCODE -eq 0 -and $matches.Count -gt 0) {
        $failed = $true
        Write-Host "[FAIL] $($check.Key): $($matches.Count)"
        $matches | Select-Object -First 20
    } else {
        Write-Host "[PASS] $($check.Key): 0"
    }
}

$numericFiles = @(& rg --files @SourceRoots -g '*$[0-9]*.java' 2>$null)
if ($numericFiles.Count -gt 0) {
    $failed = $true
    Write-Host "[FAIL] numeric_anonymous_class_files: $($numericFiles.Count)"
    $numericFiles | Select-Object -First 20
} else {
    Write-Host "[PASS] numeric_anonymous_class_files: 0"
}

if ($failed) {
    exit 1
}

Write-Host "All readability checks passed."
