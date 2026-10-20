@echo off
chcp 65001 >nul
title TokoKu Backend Server (Kotlin + MySQL)
echo ================================================================
echo   MENJALANKAN BACKEND REST API TOKOKU (FULL KOTLIN + MYSQL)
echo ================================================================
echo.
echo Memeriksa status MySQL...
netstat -ano | findstr :3306 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] MySQL aktif di port 3306!
) else (
    echo [PERINGATAN] Port 3306 belum aktif. 
    echo Pastikan MySQL (XAMPP / Laragon) sudah dijalankan jika ingin terhubung ke database.
    echo.
)

echo Menjalankan Kotlin REST API Server via Gradle...
echo Endpoint REST API: http://localhost:8080/api/health
echo Endpoint Android Emulator: http://10.0.2.2:8080/api/health
echo.

call ".\gradlew.bat" :server:run

pause
