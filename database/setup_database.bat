@echo off
chcp 65001 >nul
title Setup Database MySQL TokoKu E-Commerce
echo ================================================================
echo   SETUP DATABASE MYSQL TOKOKU E-COMMERCE (FULL KOTLIN + MYSQL)
echo ================================================================
echo.

set MYSQL_BIN=""

if exist "C:\xampp\mysql\bin\mysql.exe" (
    set MYSQL_BIN="C:\xampp\mysql\bin\mysql.exe"
    echo [INFO] Terdeteksi XAMPP MySQL di C:\xampp\mysql\bin\mysql.exe
    goto FOUND_MYSQL
)

if exist "C:\laragon\bin\mysql\mysql-8.0.30-winx64\bin\mysql.exe" (
    set MYSQL_BIN="C:\laragon\bin\mysql\mysql-8.0.30-winx64\bin\mysql.exe"
    echo [INFO] Terdeteksi Laragon MySQL
    goto FOUND_MYSQL
)

for /d %%i in ("C:\Program Files\MySQL\MySQL Server*") do (
    if exist "%%i\bin\mysql.exe" (
        set MYSQL_BIN="%%i\bin\mysql.exe"
        echo [INFO] Terdeteksi MySQL Server di %%i\bin\mysql.exe
        goto FOUND_MYSQL
    )
)

where mysql >nul 2>&1
if %ERRORLEVEL% equ 0 (
    set MYSQL_BIN=mysql
    echo [INFO] Terdeteksi MySQL di System PATH
    goto FOUND_MYSQL
)

echo [ERROR] MySQL tidak ditemukan di path umum (XAMPP / Laragon / Program Files / PATH)!
echo Pastikan XAMPP atau MySQL telah diinstal dan berjalan.
echo Jika menggunakan XAMPP, silakan buka XAMPP Control Panel lalu klik Start pada MySQL.
pause
exit /b 1

:FOUND_MYSQL
echo.
echo [1/2] Membuat Database 'ecommerce_db'...
%MYSQL_BIN% -u root -e "CREATE DATABASE IF NOT EXISTS ecommerce_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [PERINGATAN] Gagal membuat database otomatis tanpa password.
    echo Mencoba dengan konfirmasi password atau pastikan Service MySQL XAMPP sudah di-START!
    echo Silakan jalankan: %MYSQL_BIN% -u root -p ecommerce_db ^< "%~dp0tokoku_ecommerce.sql"
)

echo [2/2] Mengimpor Tabel dan Data Produk ke 'ecommerce_db'...
%MYSQL_BIN% -u root ecommerce_db < "%~dp0tokoku_ecommerce.sql"
if %ERRORLEVEL% equ 0 (
    echo.
    echo ================================================================
    echo   BERHASIL! Database 'ecommerce_db' telah sukses di-setup!
    echo   - Tabel: categories, products, product_specifications, 
    echo            promo_banners, orders, order_items
    echo   - Data: 12 Produk Lengkap, Kategori, Banner, & Spesifikasi
    echo ================================================================
) else (
    echo.
    echo [CATATAN] Jika MySQL Anda memiliki password root, silakan masukkan manual:
    echo %MYSQL_BIN% -u root -p ecommerce_db ^< "%~dp0tokoku_ecommerce.sql"
)

echo.
pause
