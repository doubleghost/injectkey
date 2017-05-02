@echo off
set Path=%cd%\adb;%Path%
:: @echo %Path%
start adb shell input swipe 0 200 800 800
choice /t 1 /d y /n >nul
start adb shell input swipe 0 200 800 800
choice /t 1 /d y /n >nul
start adb shell input swipe 0 200 800 800
choice /t 1 /d y /n >nul
start adb shell input swipe 0 200 800 800
choice /t 1 /d y /n >nul
start adb shell input swipe 0 200 800 800
