@echo off
echo Building .jar file...
:: Create JAR Manifest
if exist "manifest.txt" (
    echo manifest.txt exists.
) else (
    echo Creating manifest...
    echo Main-Class: src.com.fvjapps.brickgame.Start >> manifest.txt
    echo. >> manifest.txt
)
:: Build JAR
javac src\com\fvjapps\brickgame\*.java
jar -cfm brickgame.jar manifest.txt src\com\fvjapps\brickgame\*.wav src\com\fvjapps\brickgame\*.class
:: Cleanup
del src\com\fvjapps\brickgame\*.class
echo .jar file created.
java -jar brickgame.jar