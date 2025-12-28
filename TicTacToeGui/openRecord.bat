@echo off
cd /d "%~dp0"
mvn javafx:run -Djavafx.args="%1"