[CmdletBinding()]
param(
    [switch]$BuildLegacyFrontend,
    [switch]$Clean,
    [switch]$SkipTests,
    [switch]$PrepareHost,
    [string]$OriginalJar = '',
    [string]$HostJar = '',
    [string]$MavenCommand = 'mvn'
)

$ErrorActionPreference = 'Stop'
$workspace = Split-Path $PSScriptRoot -Parent
$frontend = Join-Path $workspace 'project\urule-console-js'
$parentPom = Join-Path $workspace 'project\urule-parent-pro\pom.xml'
$coreJar = Join-Path $workspace 'project\urule-core-pro\target\urule-core-pro-4.3.0.jar'
$consoleJar = Join-Path $workspace 'project\urule-console-pro\target\urule-console-pro-4.3.0.jar'
if ([string]::IsNullOrWhiteSpace($OriginalJar)) {
    $OriginalJar = Join-Path $workspace 'inputs\urule-console-pro-4.3.0.jar'
}
if ([string]::IsNullOrWhiteSpace($HostJar)) {
    $HostJar = Join-Path $workspace 'gen\urule-pro-boot-orig-test.jar'
}

function Invoke-Native([string]$Command, [string[]]$Arguments, [string]$WorkingDirectory) {
    Push-Location $WorkingDirectory
    try {
        & $Command @Arguments
        if ($LASTEXITCODE -ne 0) {
            throw "$Command failed with exit code $LASTEXITCODE"
        }
    } finally {
        Pop-Location
    }
}

if (-not (Test-Path -LiteralPath $parentPom)) {
    throw "Parent POM not found: $parentPom"
}
if (-not (Test-Path -LiteralPath $OriginalJar)) {
    throw "Original console JAR not found: $OriginalJar"
}

if ($BuildLegacyFrontend) {
    $webpack = Join-Path $frontend 'node_modules\.bin\webpack.cmd'
    if (-not (Test-Path -LiteralPath $webpack)) {
        Invoke-Native 'npm' @('install', '--legacy-peer-deps', '--ignore-scripts') $frontend
    }
    Invoke-Native 'npm' @('run', 'build') $frontend

    $legacyOutput = Join-Path $frontend 'dist\legacy'
    $expectedLegacyBundles = @(
        'frame', 'variableEditor', 'constantEditor', 'parameterEditor',
        'actionEditor', 'packageEditor', 'flowDesigner', 'ruleSetEditor',
        'decisionTableEditor', 'scriptDecisionTableEditor',
        'decisionTreeEditor', 'clientConfigEditor', 'ulEditor',
        'scoreCardTable', 'permissionConfigEditor'
    )
    $missingLegacyBundles = @($expectedLegacyBundles | Where-Object {
        -not (Test-Path -LiteralPath (Join-Path $legacyOutput "$_.bundle.js"))
    })
    if ($missingLegacyBundles.Count -ne 0) {
        throw "Legacy frontend bundles missing: $($missingLegacyBundles -join ', ')"
    }
    Write-Output "LEGACY_FRONTEND_BUNDLES=$($expectedLegacyBundles.Count)"
    Write-Output "LEGACY_FRONTEND_OUTPUT=$legacyOutput"
}

$stamp = Get-Date -Format 'yyyyMMdd-HHmmss-fff'
$stage = Join-Path $workspace "work\validation\console-resources-$stamp-$PID"
& (Join-Path $PSScriptRoot 'restore_console_resources_from_original.ps1') `
    -WorkspaceRoot $workspace -OriginalJar $OriginalJar -StageRoot $stage -Prune

$mavenArguments = @('-f', $parentPom)
if ($Clean) {
    $mavenArguments += 'clean'
}
$mavenArguments += 'package'
if ($SkipTests) {
    $mavenArguments += '-DskipTests'
}
Invoke-Native $MavenCommand $mavenArguments $workspace

if (-not (Test-Path -LiteralPath $coreJar) -or -not (Test-Path -LiteralPath $consoleJar)) {
    throw 'Maven completed without producing both expected backend JARs.'
}

$integrityJson = Join-Path $workspace "work\validation\console-integrity-$stamp-$PID.json"
& (Join-Path $PSScriptRoot 'verify_console_jar_integrity.ps1') `
    -WorkspaceRoot $workspace -OriginalJar $OriginalJar `
    -RestoredJar $consoleJar -OutputJson $integrityJson
$integrity = Get-Content -LiteralPath $integrityJson -Raw | ConvertFrom-Json

$allowedMissingClass = 'com/bstek/urule/console/database/model/datasource/FieldType$1.class'
$unexpectedMissingClasses = @($integrity.class_set.missing | Where-Object { $_ -ne $allowedMissingClass })
if ($unexpectedMissingClasses.Count -ne 0 -or $integrity.class_set.extra.Count -ne 0) {
    throw "Unexpected class-set differences. See $integrityJson"
}
$resourceDifferenceCount = @($integrity.runtime_resources.missing).Count +
    @($integrity.runtime_resources.extra).Count +
    @($integrity.runtime_resources.length_mismatches).Count +
    @($integrity.runtime_resources.hash_mismatches).Count
if ($resourceDifferenceCount -ne 0) {
    throw "Runtime resource differences detected. See $integrityJson"
}

$artifacts = Join-Path $workspace 'artifacts'
Copy-Item -LiteralPath $coreJar -Destination (Join-Path $artifacts 'urule-core-pro-4.3.0-restored.jar') -Force
Copy-Item -LiteralPath $consoleJar -Destination (Join-Path $artifacts 'urule-console-pro-4.3.0-restored.jar') -Force

Write-Output "CORE_JAR=$coreJar"
Write-Output "CONSOLE_JAR=$consoleJar"
Write-Output "INTEGRITY_REPORT=$integrityJson"
Write-Output "CLASS_COVERAGE=$($integrity.restored.class_count)/$($integrity.original.class_count)"
Write-Output "RUNTIME_RESOURCES_VERIFIED=$($integrity.runtime_resources.compared)"

if ($PrepareHost) {
    & (Join-Path $PSScriptRoot 'prepare-runtime-host.ps1') `
        -HostJar $HostJar -CoreJar $coreJar -ConsoleJar $consoleJar -Force
}
