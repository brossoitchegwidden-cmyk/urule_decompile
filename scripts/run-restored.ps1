[CmdletBinding()]
param(
    [string]$HostJar = '',
    [string]$JavaPath = '',
    [int]$Port = 8081,
    [string]$Profile = 'embed',
    [string]$UruleHome = '',
    [string]$DatabaseUrl = '',
    [string]$LicenseAdmins = 'admin',
    [switch]$LicenseIssuerEnabled,
    [string]$LicenseIssuerPrivateKey = '',
    [string]$LicenseIssuerPublicKey = '',
    [string]$LicenseIssuerDefaultLicensee = 'Portable License',
    [long]$LicenseIssuerDefaultLimit = -1,
    [bool]$LicenseIssuerDefaultPortable = $true,
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
$jvmArguments = @(
    "-DuruleHome=$UruleHome",
    "-Durule.license.admins=$LicenseAdmins"
)
if (-not [string]::IsNullOrWhiteSpace($LicenseIssuerPublicKey)) {
    if (-not (Test-Path -LiteralPath $LicenseIssuerPublicKey -PathType Leaf)) {
        throw "License issuer public key not found: $LicenseIssuerPublicKey"
    }
    $LicenseIssuerPublicKey = (Resolve-Path -LiteralPath $LicenseIssuerPublicKey).Path
    $jvmArguments += "-Durule.license.issuer.public-key=$LicenseIssuerPublicKey"
}
if ($LicenseIssuerEnabled) {
    if ([string]::IsNullOrWhiteSpace($LicenseIssuerPrivateKey) -or [string]::IsNullOrWhiteSpace($LicenseIssuerPublicKey)) {
        throw 'LicenseIssuerEnabled requires both LicenseIssuerPrivateKey and LicenseIssuerPublicKey.'
    }
    if (-not (Test-Path -LiteralPath $LicenseIssuerPrivateKey -PathType Leaf)) {
        throw "License issuer private key not found: $LicenseIssuerPrivateKey"
    }
    if ([string]::IsNullOrWhiteSpace($LicenseIssuerDefaultLicensee) -or $LicenseIssuerDefaultLicensee.Trim().Length -gt 256) {
        throw 'LicenseIssuerDefaultLicensee must contain between 1 and 256 characters.'
    }
    if ($LicenseIssuerDefaultLimit -lt -1 -or ($LicenseIssuerDefaultLimit -ge 0 -and $LicenseIssuerDefaultLimit -le [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds())) {
        throw 'LicenseIssuerDefaultLimit must be -1 or a future epoch millisecond value.'
    }
    $LicenseIssuerPrivateKey = (Resolve-Path -LiteralPath $LicenseIssuerPrivateKey).Path
    $portableValue = $LicenseIssuerDefaultPortable.ToString().ToLowerInvariant()
    $jvmArguments += '-Durule.license.issuer.enabled=true'
    $jvmArguments += "-Durule.license.issuer.private-key=$LicenseIssuerPrivateKey"
    $jvmArguments += "-Durule.license.issuer.default-licensee=$($LicenseIssuerDefaultLicensee.Trim())"
    $jvmArguments += "-Durule.license.issuer.default-limit=$LicenseIssuerDefaultLimit"
    $jvmArguments += "-Durule.license.issuer.default-portable=$portableValue"
}
$arguments = $jvmArguments + @(
    '-jar', $HostJar,
    "--server.port=$Port",
    "--spring.profiles.active=$Profile",
    "--urule.store.database.url=$DatabaseUrl"
)

Write-Output "HOST_JAR=$HostJar"
Write-Output "PORT=$Port"
Write-Output "URULE_HOME=$UruleHome"
Write-Output "DATABASE_URL=$DatabaseUrl"
Write-Output "LICENSE_ADMINS=$LicenseAdmins"
Write-Output "LICENSE_ISSUER_ENABLED=$($LicenseIssuerEnabled.IsPresent)"
Write-Output "LICENSE_ISSUER_PUBLIC_KEY_CONFIGURED=$(-not [string]::IsNullOrWhiteSpace($LicenseIssuerPublicKey))"
Write-Output "LICENSE_ISSUE_DEFAULT_CONFIGURED=$($LicenseIssuerEnabled.IsPresent)"

if ($Background) {
    $logDirectory = Join-Path $workspace 'logs'
    New-Item -ItemType Directory -Path $logDirectory -Force | Out-Null
    $stdout = Join-Path $logDirectory "urule-$Port.out.log"
    $stderr = Join-Path $logDirectory "urule-$Port.err.log"
    $processArguments = @($arguments | ForEach-Object {
        if ($_ -match '[\s"]') {
            '"' + $_.Replace('"', '\"') + '"'
        } else {
            $_
        }
    })
    $process = Start-Process -FilePath $JavaPath -ArgumentList $processArguments -WorkingDirectory $workspace `
        -RedirectStandardOutput $stdout -RedirectStandardError $stderr -PassThru
    Write-Output "PID=$($process.Id)"
    Write-Output "STDOUT=$stdout"
    Write-Output "STDERR=$stderr"
} else {
    & $JavaPath @arguments
    exit $LASTEXITCODE
}
