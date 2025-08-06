if (!(Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" }

cd .\src

javac -d ..\bin chess\*.java chess\board\*.java chess\game\*.java chess\gui\*.java chess\pieces\*.java

cd .\chess

java -cp bin Main.java
