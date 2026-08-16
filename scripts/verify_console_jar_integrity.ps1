[CmdletBinding()]
param(
    [string]$WorkspaceRoot = (Split-Path $PSScriptRoot -Parent),
    [string]$OriginalJar = '',
    [string]$RestoredJar = '',
    [string]$OutputJson = ''
)

$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem

if ([string]::IsNullOrWhiteSpace($OriginalJar)) {
    $OriginalJar = Join-Path $WorkspaceRoot 'inputs\urule-console-pro-4.3.0.jar'
}
if ([string]::IsNullOrWhiteSpace($RestoredJar)) {
    $RestoredJar = Join-Path $WorkspaceRoot 'project\urule-console-pro\target\urule-console-pro-4.3.0.jar'
}
if ([string]::IsNullOrWhiteSpace($OutputJson)) {
    $OutputJson = Join-Path $WorkspaceRoot 'work\validation\console-final-integrity.json'
}
if (Test-Path -LiteralPath $OutputJson) {
    throw "Refusing to overwrite output: $OutputJson"
}

$metadataExclusions = @(
    'META-INF/MANIFEST.MF',
    'META-INF/maven/com.bstek.urule/urule-console-pro/pom.xml',
    'META-INF/maven/com.bstek.urule/urule-console-pro/pom.properties'
)

function Read-Jar([string]$Path) {
    $archive = [System.IO.Compression.ZipFile]::OpenRead($Path)
    $classes = [System.Collections.Generic.List[string]]::new()
    $resources = @{}
    try {
        foreach ($entry in $archive.Entries) {
            if ([string]::IsNullOrEmpty($entry.Name)) {
                continue
            }
            if ($entry.FullName.EndsWith('.class', [System.StringComparison]::OrdinalIgnoreCase)) {
                $classes.Add($entry.FullName)
                continue
            }
            if ($metadataExclusions -contains $entry.FullName) {
                continue
            }
            $stream = $entry.Open()
            $sha = [System.Security.Cryptography.SHA256]::Create()
            try {
                $hash = [System.BitConverter]::ToString($sha.ComputeHash($stream)).Replace('-', '')
            } finally {
                $sha.Dispose()
                $stream.Dispose()
            }
            $resources[$entry.FullName] = [pscustomobject]@{
                length = $entry.Length
                sha256 = $hash
            }
        }
    } finally {
        $archive.Dispose()
    }
    $item = Get-Item -LiteralPath $Path
    return [pscustomobject]@{
        path = $item.FullName
        bytes = $item.Length
        sha256 = (Get-FileHash -LiteralPath $Path -Algorithm SHA256).Hash
        classes = @($classes | Sort-Object)
        resources = $resources
    }
}

$original = Read-Jar $OriginalJar
$restored = Read-Jar $RestoredJar
$missingClasses = @(Compare-Object $original.classes $restored.classes | Where-Object SideIndicator -eq '<=' | ForEach-Object InputObject)
$extraClasses = @(Compare-Object $original.classes $restored.classes | Where-Object SideIndicator -eq '=>' | ForEach-Object InputObject)
$missingResources = [System.Collections.Generic.List[string]]::new()
$extraResources = [System.Collections.Generic.List[string]]::new()
$lengthMismatches = [System.Collections.Generic.List[string]]::new()
$hashMismatches = [System.Collections.Generic.List[string]]::new()

foreach ($name in $original.resources.Keys) {
    if (-not $restored.resources.ContainsKey($name)) {
        $missingResources.Add($name)
        continue
    }
    if ($original.resources[$name].length -ne $restored.resources[$name].length) {
        $lengthMismatches.Add($name)
    }
    if ($original.resources[$name].sha256 -ne $restored.resources[$name].sha256) {
        $hashMismatches.Add($name)
    }
}
foreach ($name in $restored.resources.Keys) {
    if (-not $original.resources.ContainsKey($name)) {
        $extraResources.Add($name)
    }
}

$result = [pscustomobject]@{
    generated_at = (Get-Date -Format o)
    metadata_exclusions = $metadataExclusions
    original = [pscustomobject]@{
        path = $original.path
        bytes = $original.bytes
        sha256 = $original.sha256
        class_count = $original.classes.Count
        runtime_resource_count = $original.resources.Count
    }
    restored = [pscustomobject]@{
        path = $restored.path
        bytes = $restored.bytes
        sha256 = $restored.sha256
        class_count = $restored.classes.Count
        runtime_resource_count = $restored.resources.Count
    }
    class_set = [pscustomobject]@{
        missing = @($missingClasses)
        extra = @($extraClasses)
    }
    runtime_resources = [pscustomobject]@{
        compared = $original.resources.Count
        missing = @($missingResources)
        extra = @($extraResources)
        length_mismatches = @($lengthMismatches)
        hash_mismatches = @($hashMismatches)
    }
}

$result | ConvertTo-Json -Depth 8 | Set-Content -LiteralPath $OutputJson -Encoding UTF8
Write-Output "ORIGINAL_CLASSES=$($result.original.class_count)"
Write-Output "RESTORED_CLASSES=$($result.restored.class_count)"
Write-Output "MISSING_CLASSES=$($result.class_set.missing.Count)"
Write-Output "EXTRA_CLASSES=$($result.class_set.extra.Count)"
Write-Output "RESOURCES_COMPARED=$($result.runtime_resources.compared)"
Write-Output "RESOURCE_MISSING=$($result.runtime_resources.missing.Count)"
Write-Output "RESOURCE_EXTRA=$($result.runtime_resources.extra.Count)"
Write-Output "RESOURCE_LENGTH_MISMATCH=$($result.runtime_resources.length_mismatches.Count)"
Write-Output "RESOURCE_HASH_MISMATCH=$($result.runtime_resources.hash_mismatches.Count)"
Write-Output "OUTPUT=$OutputJson"
