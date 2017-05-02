@echo off
set Path=%cd%\adb;%Path%
:: @echo %Path%
adb nodaemon server
netstat -ano | findstr "5037"
pause
