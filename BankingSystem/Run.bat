@echo off
setlocal

:: Set classpath
set CLASSPATH=.;lib\mysql-connector-j-9.4.0.jar;bin

:: Compile all Java files
javac -d bin -cp lib\mysql-connector-j-9.4.0.jar model\BankAccount.java dao\BankDAO.java ui\BankApp.java

:: Run the main class
java -cp %CLASSPATH% ui.BankApp

pause