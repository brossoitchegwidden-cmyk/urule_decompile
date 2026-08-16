[CmdletBinding()]
param(
    [string]$WorkspaceRoot = (Split-Path $PSScriptRoot -Parent),
    [string]$OriginalJar = '',
    [string]$ResourceRoot = '',
    [string]$StageRoot = '',
    [switch]$Prune
)

$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem

if ([string]::IsNullOrWhiteSpace($OriginalJar)) {
    $OriginalJar = Join-Path $WorkspaceRoot 'inputs\urule-console-pro-4.3.0.jar'
}
if ([string]::IsNullOrWhiteSpace($ResourceRoot)) {
    $ResourceRoot = Join-Path $WorkspaceRoot 'project\urule-console-pro\src\main\resources'
}
if ([string]::IsNullOrWhiteSpace($StageRoot)) {
    $StageRoot = Join-Path $WorkspaceRoot 'work\validation\console-resources-from-original-stage'
}

$workspace = [System.IO.Path]::GetFullPath($WorkspaceRoot).TrimEnd('\')
$resource = [System.IO.Path]::GetFullPath($ResourceRoot).TrimEnd('\')
$stage = [System.IO.Path]::GetFullPath($StageRoot).TrimEnd('\')
if (-not $resource.StartsWith($workspace + '\', [System.StringComparison]::OrdinalIgnoreCase)) {
    throw "Resource root escaped workspace: $resource"
}
if (-not $stage.StartsWith($workspace + '\', [System.StringComparison]::OrdinalIgnoreCase)) {
    throw "Stage root escaped workspace: $stage"
}
if (Test-Path -LiteralPath $stage) {
    throw "Refusing to reuse stage root: $stage"
}

$expectedJarHash = 'CBFAF5AE045E96A27F145A3C0DC6C630AA1DA86CFAD6E77B2C120909E99F4DB7'
$actualJarHash = (Get-FileHash -LiteralPath $OriginalJar -Algorithm SHA256).Hash
if ($actualJarHash -ne $expectedJarHash) {
    throw "Original console JAR hash mismatch: $actualJarHash"
}

New-Item -ItemType Directory -Path $stage | Out-Null
$archive = [System.IO.Compression.ZipFile]::OpenRead($OriginalJar)
$extracted = 0
try {
    foreach ($entry in $archive.Entries) {
        if ([string]::IsNullOrEmpty($entry.Name) -or
            $entry.FullName.EndsWith('.class', [System.StringComparison]::OrdinalIgnoreCase) -or
            $entry.FullName.Equals('META-INF/MANIFEST.MF', [System.StringComparison]::OrdinalIgnoreCase)) {
            continue
        }
        $relative = $entry.FullName.Replace('/', [System.IO.Path]::DirectorySeparatorChar)
        $destination = [System.IO.Path]::GetFullPath((Join-Path $stage $relative))
        if (-not $destination.StartsWith($stage + '\', [System.StringComparison]::OrdinalIgnoreCase)) {
            throw "Archive entry escaped stage root: $($entry.FullName)"
        }
        [System.IO.Directory]::CreateDirectory((Split-Path -Parent $destination)) | Out-Null
        [System.IO.Compression.ZipFileExtensions]::ExtractToFile($entry, $destination, $false)
        $extracted++
    }
} finally {
    $archive.Dispose()
}

if ($extracted -ne 132) {
    throw "Expected 132 non-class, non-manifest files; extracted $extracted"
}

$copied = 0
$stagedRelativePaths = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::OrdinalIgnoreCase)
foreach ($sourceFile in Get-ChildItem -LiteralPath $stage -Recurse -File) {
    $relative = $sourceFile.FullName.Substring($stage.Length).TrimStart('\')
    [void]$stagedRelativePaths.Add($relative)
    $destination = [System.IO.Path]::GetFullPath((Join-Path $resource $relative))
    if (-not $destination.StartsWith($resource + '\', [System.StringComparison]::OrdinalIgnoreCase)) {
        throw "Destination escaped resource root: $destination"
    }
    [System.IO.Directory]::CreateDirectory((Split-Path -Parent $destination)) | Out-Null
    Copy-Item -LiteralPath $sourceFile.FullName -Destination $destination -Force
    $copied++
}

$pruned = 0
if ($Prune) {
    foreach ($resourceFile in Get-ChildItem -LiteralPath $resource -Recurse -File) {
        $relative = $resourceFile.FullName.Substring($resource.Length).TrimStart('\')
        if (-not $stagedRelativePaths.Contains($relative)) {
            Remove-Item -LiteralPath $resourceFile.FullName -Force
            $pruned++
        }
    }
}

Write-Output "ORIGINAL_JAR_SHA256=$actualJarHash"
Write-Output "EXTRACTED=$extracted"
Write-Output "COPIED=$copied"
Write-Output "PRUNED=$pruned"
Write-Output "RESOURCE_ROOT=$resource"
Write-Output "STAGE_ROOT=$stage"
