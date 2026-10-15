param([ValidateSet("demo", "test", "direct")][string]$Mode = "demo")
$ErrorActionPreference = "Stop"
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force -Path "build/classes" | Out-Null
    $sources = @(Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName })
    & java com.sun.tools.javac.Main --release 17 -Xlint:all -Werror -d build/classes @sources
    if ($LASTEXITCODE -ne 0) { throw "Java compilation failed" }
    $targets = @{
        demo = "kz.agzam.carwash.client.Main"
        test = "kz.agzam.carwash.facade.CarWashTests"
        direct = "kz.agzam.carwash.advanced.DirectSubsystemExample"
    }
    & java -cp build/classes $targets[$Mode]
    if ($LASTEXITCODE -ne 0) { throw "Java execution failed" }
} finally {
    Pop-Location
}
