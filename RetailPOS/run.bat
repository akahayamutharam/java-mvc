@echo off
setlocal
set CP=lib\mysql-connector-j-9.4.0.jar; 
if not exist bin mkdir bin
javac -cp %CP% -d . RetailPOSMain.java controller\*.java model\*.java dao\*.java view\*.java
if errorlevel 1 (
  echo Build failed.
  pause
  exit /b 1
)
java -cp .;%CP% RetailPOSMain
pause
