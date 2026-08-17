[CmdletBinding()]
param(
    [string]$HostJar = '',
    [string]$CoreJar = '',
    [string]$ConsoleJar = '',
    [string]$OutputJar = '',
    [string]$JarTool = '',
    [switch]$Force
)

$ErrorActionPreference = 'Stop'
$workspace = Split-Path $PSScriptRoot -Parent
if ([string]::IsNullOrWhiteSpace($HostJar)) {
    $HostJar = Join-Path $workspace 'gen\urule-pro-boot-orig-test.jar'
}
if ([string]::IsNullOrWhiteSpace($CoreJar)) {
    $CoreJar = Join-Path $workspace 'project\urule-core-pro\target\urule-core-pro-4.3.0.jar'
}
if ([string]::IsNullOrWhiteSpace($ConsoleJar)) {
    $ConsoleJar = Join-Path $workspace 'project\urule-console-pro\target\urule-console-pro-4.3.0.jar'
}
if ([string]::IsNullOrWhiteSpace($OutputJar)) {
    $OutputJar = Join-Path $workspace 'artifacts\urule-pro-boot-restored.jar'
}
$outputDirectory = Split-Path -Parent $OutputJar
if (-not [string]::IsNullOrWhiteSpace($outputDirectory)) {
    New-Item -ItemType Directory -Path $outputDirectory -Force | Out-Null
}

foreach ($path in @($HostJar, $CoreJar, $ConsoleJar)) {
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) {
        throw "Required JAR not found: $path"
    }
}
if ((Test-Path -LiteralPath $OutputJar) -and -not $Force) {
    throw "Output already exists; use -Force to replace it: $OutputJar"
}

if ([string]::IsNullOrWhiteSpace($JarTool)) {
    $candidates = @()
    $jarCommand = Get-Command jar.exe -ErrorAction SilentlyContinue
    if ($jarCommand) { $candidates += $jarCommand.Source }
    if ($env:JAVA_HOME) { $candidates += (Join-Path $env:JAVA_HOME 'bin\jar.exe') }
    $javaCommand = Get-Command java.exe -ErrorAction SilentlyContinue
    if ($javaCommand) { $candidates += (Join-Path (Split-Path $javaCommand.Source -Parent) 'jar.exe') }
    $candidates += 'C:\Program Files (x86)\jdk\bin\jar.exe'
    $JarTool = $candidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
}
if ([string]::IsNullOrWhiteSpace($JarTool)) {
    throw 'jar.exe was not found. Pass its full path with -JarTool.'
}

$stage = Join-Path $workspace "work\runtime-host-overlay-$PID"
if (Test-Path -LiteralPath $stage) {
    throw "Refusing to reuse staging directory: $stage"
}
$nestedLib = Join-Path $stage 'BOOT-INF\lib'
New-Item -ItemType Directory -Path $nestedLib -Force | Out-Null
Copy-Item -LiteralPath $CoreJar -Destination (Join-Path $nestedLib 'urule-core-pro-4.3.0.jar')
Copy-Item -LiteralPath $ConsoleJar -Destination (Join-Path $nestedLib 'urule-console-pro-4.3.0.jar')
Copy-Item -LiteralPath $HostJar -Destination $OutputJar -Force

try {
    Push-Location $stage
    try {
        & $JarTool '-0uf' $OutputJar `
            'BOOT-INF/lib/urule-core-pro-4.3.0.jar' `
            'BOOT-INF/lib/urule-console-pro-4.3.0.jar'
        if ($LASTEXITCODE -ne 0) {
            throw "jar.exe failed with exit code $LASTEXITCODE"
        }
    } finally {
        Pop-Location
    }

    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $archive = [System.IO.Compression.ZipFile]::OpenRead($OutputJar)
    try {
        foreach ($item in @(
            @{ Entry = 'BOOT-INF/lib/urule-core-pro-4.3.0.jar'; Source = $CoreJar },
            @{ Entry = 'BOOT-INF/lib/urule-console-pro-4.3.0.jar'; Source = $ConsoleJar }
        )) {
            $entries = @($archive.Entries | Where-Object FullName -eq $item.Entry)
            if ($entries.Count -ne 1) {
                throw "Expected exactly one nested entry $($item.Entry); found $($entries.Count)"
            }
            $stream = $entries[0].Open()
            $sha = [System.Security.Cryptography.SHA256]::Create()
            try {
                $nestedHash = [BitConverter]::ToString($sha.ComputeHash($stream)).Replace('-', '')
            } finally {
                $sha.Dispose()
                $stream.Dispose()
            }
            $sourceHash = (Get-FileHash -LiteralPath $item.Source -Algorithm SHA256).Hash
            if ($nestedHash -ne $sourceHash) {
                throw "Nested JAR hash mismatch: $($item.Entry)"
            }
        }
    } finally {
        $archive.Dispose()
    }
} finally {
    if (Test-Path -LiteralPath $stage) {
        Remove-Item -LiteralPath $stage -Recurse -Force
    }
}

Write-Output "RUNTIME_HOST=$OutputJar"
Write-Output "RUNTIME_HOST_SHA256=$((Get-FileHash -LiteralPath $OutputJar -Algorithm SHA256).Hash)"
Write-Output 'NESTED_BACKEND_JARS_VERIFIED=2'
