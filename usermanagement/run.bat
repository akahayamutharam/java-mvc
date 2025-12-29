@echo off
REM === Set project folders ===
set SRC=.
set BIN=bin
set LIB=lib\mysql-connector-j-9.4.0.jar
set CLASSPATH=%BIN%;%LIB%

REM === Create bin folder if not exists ===
if not exist %BIN% mkdir %BIN%

REM === Compile all Java files from dao, model, service, view ===
echo Compiling Java sources...
javac -cp %CLASSPATH% -d %BIN% ^
    dao\UserDAO.java ^
    model\User.java ^
    service\UserService.java ^
    view\UserManagementView.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

REM === Run the main class ===
echo Running User Management System...
java -cp %CLASSPATH% view.UserManagementView

pause