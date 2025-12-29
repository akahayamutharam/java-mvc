@echo off
setlocal
set CP=.;lib\mysql-connector-j-9.4.0.jar
javac -cp %CP% model\*.java dao\*.java controller\*.java view\*.java SportsDashboard.java
java -cp %CP% SportsDashboard
pause