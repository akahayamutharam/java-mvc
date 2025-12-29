@echo off
setlocal
set CP=.;lib\mysql-connector-j-9.4.0.jar
javac -cp %CP% model\*.java dao\*.java service\*.java controller\*.java view\*.java FinanceApp.java
java -cp %CP% FinanceApp
pause