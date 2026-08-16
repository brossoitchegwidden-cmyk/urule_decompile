[CmdletBinding()]
param(
    [string]$HostJar = '',
    [string]$JavaPath = '',
    [int]$Port = 8081,
    [string]$Profile = 'embed',
    [string]$UruleHome = '',
    [string]$DatabaseUrl = '',
    [switch]$Background
)

$ErrorActionPreference = 'Stop'
$workspace = Split-Path $PSScriptRoot -Parent
if ([string]::IsNullOrWhiteSpace($HostJar)) {
    $HostJar = Join-Path $workspace 'artifacts\urule-pro-boot-restored.jar'
}
if ([string]::IsNullOrWhiteSpace($UruleHome)) {
    $UruleHome = Join-Path $workspace 'work\runtime\urule-home'
}
if ([string]::IsNullOrWhiteSpace($DatabaseUrl)) {
    $databasePath = (Join-Path $UruleHome 'data\uruledb').Replace('\', '/')
    $DatabaseUrl = "jdbc:hsqldb:file:$databasePath"
}
if (-not (Test-Path -LiteralPath $HostJar -PathType Leaf)) {
    throw "Prepared runtime host not found: $HostJar. Run prepare-runtime-host.ps1 first."
}

if ([string]::IsNullOrWhiteSpace($JavaPath)) {
    $javaCommand = Get-Command java.exe -ErrorAction SilentlyContinue
    if ($javaCommand) { $JavaPath = $javaCommand.Source }
    if (-not $JavaPath -and $env:JAVA_HOME) {
        $candidate = Join-Path $env:JAVA_HOME 'bin\java.exe'
        if (Test-Path -LiteralPath $candidate) { $JavaPath = $candidate }
    }
    if (-not $JavaPath) {
        $candidate = 'C:\Program Files (x86)\jdk\bin\java.exe'
        if (Test-Path -LiteralPath $candidate) { $JavaPath = $candidate }
    }
}
if ([string]::IsNullOrWhiteSpace($JavaPath) -or -not (Test-Path -LiteralPath $JavaPath)) {
    throw 'java.exe was not found. Pass its full path with -JavaPath.'
}

New-Item -ItemType Directory -Path $UruleHome -Force | Out-Null
$arguments = @(
    "-DuruleHome=$UruleHome",
    '-jar', $HostJar,
    "--server.port=$Port",
    "--spring.profiles.active=$Profile",
    "--urule.store.database.url=$DatabaseUrl"
)

Write-Output "HOST_JAR=$HostJar"
Write-Output "PORT=$Port"
Write-Output "URULE_HOME=$UruleHome"
Write-Output "DATABASE_URL=$DatabaseUrl"

if ($Background) {
    $logDirectory = Join-Path $workspace 'logs'
    New-Item -ItemType Directory -Path $logDirectory -Force | Out-Null
    $stdout = Join-Path $logDirectory "urule-$Port.out.log"
    $stderr = Join-Path $logDirectory "urule-$Port.err.log"
    $process = Start-Process -FilePath $JavaPath -ArgumentList $arguments -WorkingDirectory $workspace `
        -RedirectStandardOutput $stdout -RedirectStandardError $stderr -PassThru
    Write-Output "PID=$($process.Id)"
    Write-Output "STDOUT=$stdout"
    Write-Output "STDERR=$stderr"
} else {
    & $JavaPath @arguments
    exit $LASTEXITCODE
}
