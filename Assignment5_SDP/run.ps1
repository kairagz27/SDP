param([ValidateSet("demo", "test")][string]$Mode = "demo")
$ErrorActionPreference = "Stop"
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force -Path "build/classes" | Out-Null
    $sources = @(Get-ChildItem -Path "src" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName })
    & javac --release 17 -encoding UTF-8 -Xlint:all -Werror -d build/classes @sources
    if ($LASTEXITCODE -ne 0) { throw "Java compilation failed" }
    $targets = @{
        demo = "kz.agzam.study.client.Main"
        test = "kz.agzam.study.StudyPlannerTests"
    }
    & java -cp build/classes $targets[$Mode]
    if ($LASTEXITCODE -ne 0) { throw "Java execution failed" }
} finally {
    Pop-Location
}
