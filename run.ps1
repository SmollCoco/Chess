param(
	[switch]$NoRun
)

# Ensure bin directory exists
if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }

Write-Host "Compiling Java sources..." -ForegroundColor Cyan

# Compile all sources (default packages: board, game, gui, pieces, and Main)
javac -encoding UTF-8 -d bin `
	src\chess\Main.java `
	src\chess\board\*.java `
	src\chess\game\*.java `
	src\chess\gui\*.java `
	src\chess\pieces\*.java

if ($LASTEXITCODE -ne 0) {
	Write-Error "Compilation failed. See errors above."; exit 1
}

if ($NoRun) { exit 0 }

Write-Host "Starting Chess..." -ForegroundColor Green

# Include src\chess on the classpath so ImageLoader can access /resources/* at runtime
java -cp "bin;src\chess" Main

if ($LASTEXITCODE -ne 0) {
	Write-Error "Application exited with errors."; exit 1
}
