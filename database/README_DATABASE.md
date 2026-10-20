# Panduan Database MySQL & Backend TokoKu (Full Kotlin)

Dokumentasi ini menjelaskan konfigurasi, skema relasional, dan cara menjalankan **Database MySQL** serta **Backend REST API Server Kotlin** untuk aplikasi **TokoKu**.

---

## 🗄️ Struktur Database (`ecommerce_db`)

Database dirancang dengan standar relasional modern, mendukung karakter `utf8mb4` (Bahasa Indonesia, simbol, dan emoji), serta integritas foreign key dan indeks pencarian cepat.

### Tabel-Tabel Utama:
1. **`categories`**: Menyimpan kategori produk (Semua, Gadget & HP, Elektronik, Fashion, Sepatu).
2. **`products`**: Data detail produk lengkap (nama, harga diskon, harga asli, rating, terjual, foto, stok, brand).
3. **`product_specifications`**: Spesifikasi teknis setiap produk (layar, chipset, RAM, baterai, material, dll).
4. **`promo_banners`**: Banner promosi diskon dan gratis ongkir untuk carousel beranda.
5. **`orders`**: Transaksi pemesanan (nama pembeli, nomor telepon, alamat, total harga, metode bayar, status).
6. **`order_items`**: Rincian produk yang dipesan dalam setiap transaksi.

---

## 🚀 Cara Import Database ke MySQL

### Cara 1: Menggunakan Script Otomatis (Paling Mudah)
1. Pastikan **MySQL di XAMPP / Laragon** sudah aktif (**Start**).
2. Masuk ke folder `database\`.
3. Klik 2x file:
   ```cmd
   setup_database.bat
   ```
4. Script akan otomatis mendeteksi MySQL XAMPP/Laragon, membuat database `ecommerce_db`, dan mengimpor seluruh tabel beserta data 12 produk awal.

---

### Cara 2: Melalui phpMyAdmin (Browser)
1. Buka browser dan akses [http://localhost/phpmyadmin](http://localhost/phpmyadmin).
2. Klik tab **New** (Baru) di panel kiri, isi nama database: `ecommerce_db`, klik **Create**.
3. Pilih database `ecommerce_db`, lalu klik tab **Import**.
4. Klik tombol **Choose File** dan pilih file:
   `c:\Aplikasi E-Commerse\database\tokoku_ecommerce.sql`
5. Gulir ke bawah lalu klik **Import / Go**.
6. Selesai! Seluruh tabel dan data produk siap digunakan.

---

### Cara 3: Melalui Command Prompt / Terminal
```cmd
cd "c:\Aplikasi E-Commerse\database"
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS ecommerce_db CHARACTER SET utf8mb4;"
mysql -u root -p ecommerce_db < tokoku_ecommerce.sql
```
*(Jika MySQL tidak memiliki password, hilangkan opsi `-p`)*.

---

## ⚡ Menjalankan Backend REST API Server (Full Kotlin)

Backend server ditulis 100% menggunakan **Kotlin murni** dan berjalan di port `8080`.

### Menjalankan Server:
Cukup klik 2x file:
```cmd
run_backend.bat
```
Atau melalui terminal/PowerShell di folder proyek:
```powershell
.\gradlew.bat :server:run
```

### Endpoint REST API yang Tersedia:
| Method | Endpoint | Fungsi |
|---|---|---|
| `GET` | `/api/health` | Cek status server & koneksi ke MySQL |
| `GET` | `/api/categories` | Mengambil seluruh daftar kategori |
| `GET` | `/api/products` | Mengambil produk (dukung `?category=gadget&query=hp`) |
| `GET` | `/api/products/{id}` | Mengambil detail 1 produk lengkap dengan spesifikasi |
| `GET` | `/api/banners` | Mengambil daftar banner promo aktif |
| `POST`| `/api/orders` | Checkout pesanan baru (menyimpan ke tabel `orders` & `order_items`, serta mengurangi stok) |

---

## 📱 Koneksi dari Android App (Kotlin Jetpack Compose)

- **Android Emulator**: Mengakses host PC melalui alamat IP `http://10.0.2.2:8080/`.
- **Perangkat Fisik (HP)**: Pastikan HP dan laptop berada di jaringan Wi-Fi yang sama, gunakan IP laptop (contoh: `http://192.168.1.50:8080/`).
- **Mode Dual / Hybrid**: Jika server MySQL belum dinyalakan, aplikasi Android **TETAP BISA BERJALAN 100% NORMAL** menggunakan penyimpanan lokal (tidak akan pernah force close atau crash). Begitu server dinyalakan, aplikasi otomatis menyinkronkan data dengan MySQL.
