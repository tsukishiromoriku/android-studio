# TokoKu - Aplikasi E-Commerce Android & Backend (Full Kotlin + MySQL)

Aplikasi mobile E-Commerce profesional berbasis **Kotlin 100% murni** dengan UI deklaratif **Jetpack Compose**, **Material 3 Design**, backend **REST API Server (Kotlin)**, dan database relasional **MySQL**.

---

## 🚀 Ringkasan Perbaikan & Integrasi

1. **Perbaikan Masalah MainActivity & Run Error**:
   - Menambahkan konfigurasi eksplisit nama kelas activity `com.ecommerce.app.MainActivity` pada [AndroidManifest.xml](file:///c:/Aplikasi%20E-Commerse/app/src/main/AndroidManifest.xml) dan [.idea/workspace.xml](file:///c:/Aplikasi%20E-Commerse/.idea/workspace.xml).
   - Mengaktifkan izin jaringan `android.permission.INTERNET`, `android.permission.ACCESS_NETWORK_STATE`, dan `android:usesCleartextTraffic="true"` untuk komunikasi mulus ke local server/MySQL.
   - Membersihkan folder stray di root directory yang berpotensi mengacaukan pengindeksan IDE.
   - Aplikasi kini siap di-run langsung dengan mengklik tombol hijau **Run 'app'** di Android Studio!

2. **Database Relasional MySQL (`ecommerce_db`)**:
   - Skema database lengkap di [tokoku_ecommerce.sql](file:///c:/Aplikasi%20E-Commerse/database/tokoku_ecommerce.sql) mencakup 6 tabel: `categories`, `products`, `product_specifications`, `promo_banners`, `orders`, dan `order_items`.
   - Menggunakan encoding `utf8mb4` dengan data awal 12 produk elektronik, gadget, fashion, dan sepatu Indonesia beserta spesifikasi detail.
   - Script 1-klik [setup_database.bat](file:///c:/Aplikasi%20E-Commerse/database/setup_database.bat) untuk otomatisasi import ke XAMPP / Laragon / MySQL.

3. **Backend REST API Server (Full Kotlin)**:
   - Module [server](file:///c:/Aplikasi%20E-Commerse/server) dibangun 100% menggunakan **Kotlin murni** dengan `HttpServer`, `com.mysql:mysql-connector-j`, dan `Gson`.
   - Mendukung endpoint:
     * `GET /api/health`: Status database MySQL & jumlah produk
     * `GET /api/categories`: Daftar kategori produk
     * `GET /api/products`: Katalog produk (dukung filter kategori & query pencarian)
     * `GET /api/products/{id}`: Detail produk & tabel spesifikasi
     * `GET /api/banners`: Banner promosi aktif
     * `POST /api/orders`: Transaksi pemesanan (menyimpan ke tabel `orders` & `order_items`, serta mengurangi stok produk)
   - Dapat dijalankan instan menggunakan [run_backend.bat](file:///c:/Aplikasi%20E-Commerse/run_backend.bat) atau command `./gradlew :server:run`.

4. **Koneksi Dual-Mode / Smart Fallback di Android**:
   - Integrasi Retrofit 2 + Gson di module Android client.
   - Jika MySQL backend aktif, aplikasi otomatis menyinkronkan data katalog dan status `🟢 MySQL Online` ditampilkan di header.
   - Jika MySQL belum dijalankan, aplikasi tetap berjalan 100% normal dan lancar dalam `🟠 Offline (Lokal)` mode tanpa force close atau crash!

---

## 📋 Panduan Menjalankan Aplikasi (Langkah Cepat)

### Langkah 1: Setup Database MySQL
1. Buka **XAMPP Control Panel** (atau Laragon) lalu klik **Start** pada **MySQL**.
2. Masuk ke folder `database/` lalu klik 2x file:
   ```cmd
   setup_database.bat
   ```
   *(Database `ecommerce_db` beserta 6 tabel dan 12 data produk otomatis dibuat)*.

### Langkah 2: Jalankan Backend Kotlin Server
Klik 2x file:
```cmd
run_backend.bat
```
*(Server Kotlin akan aktif di port `8080` dan terhubung ke database MySQL)*.

### Langkah 3: Jalankan Aplikasi di Android Studio
1. Buka folder proyek ini di **Android Studio**.
2. Pilih konfigurasi run **`app`** di toolbar atas.
3. Pilih Emulator (misal: `Resizable_Experimental`) atau HP fisik Anda.
4. Klik tombol hijau **Run ▶**.

---

## 📱 Fitur-Fitur Aplikasi TokoKu

- **Branding Header**: Logo TokoKu, indikator koneksi database real-time (`🟢 MySQL Online` / `🟠 Offline`), serta ikon keranjang belanja dengan *counter badge*.
- **Pencarian Real-Time**: Kolom pencarian responsif berdasarkan nama produk, brand, dan deskripsi dengan tombol hapus instan.
- **Banner Promosi Dinamis**: Banner diskon hingga 50% dan gratis ongkir se-Indonesia.
- **Filter Kategori Cepat**: Filter produk per kategori: **Semua**, **Gadget & HP**, **Elektronik**, **Fashion**, dan **Sepatu**.
- **Katalog Grid 2-Kolom**: Kartu produk modern dengan gambar resolusi tinggi (Coil Image Loader), diskon badge, rating bintang, jumlah terjual, format Rupiah otomatis, tombol favorit, dan tombol cepat `+ Keranjang`.
- **Halaman Detail Produk**: Foto penuh, garansi 100% original, informasi stok, deskripsi komprehensif, dan tabel spesifikasi teknis lengkap.
- **Keranjang Belanja & Checkout Real-time**: Pengaturan jumlah item (+/-), hapus item, perhitungan total pembayaran otomatis, dan tombol **Checkout Sekarang** yang langsung mencatat pesanan ke tabel `orders` & `order_items` di MySQL.

---

## 📂 Struktur Proyek

```text
c:\Aplikasi E-Commerse\
├── app\                                # Client Android (Jetpack Compose & Kotlin)
│   ├── src\main\
│   │   ├── AndroidManifest.xml         # Manifest dengan MainActivity & Network Permissions
│   │   ├── java\com\ecommerce\app\
│   │   │   ├── MainActivity.kt         # Entry point Activity
│   │   │   ├── model\                  # Data models (Product, Category)
│   │   │   ├── data\
│   │   │   │   ├── ProductRepository.kt# StateFlow & Smart Sync MySQL/Lokal
│   │   │   │   └── remote\             # ApiClient, EcommerceApi, ApiModels (Retrofit)
│   │   │   └── ui\                     # Screens, Components, Theme
│   │   └── res\                        # Resources (Colors, Strings, Themes)
│   └── build.gradle.kts                # Build dependencies (Compose, Retrofit, Coil)
├── server\                             # Backend REST API (100% Kotlin)
│   ├── src\main\kotlin\com\ecommerce\server\
│   │   ├── ServerMain.kt               # HTTP Server & Endpoints
│   │   ├── DatabaseManager.kt          # Koneksi MySQL JDBC & Transaksi Orders
│   │   └── Models.kt                   # DTOs
│   └── build.gradle.kts                # Build dependencies (Kotlin JVM, MySQL, Gson)
├── database\                           # Database MySQL
│   ├── tokoku_ecommerce.sql            # Skema lengkap DDL & DML data produk
│   ├── setup_database.bat              # Script otomatis 1-klik import MySQL
│   └── README_DATABASE.md              # Panduan lengkap database
├── run_backend.bat                     # Script 1-klik menjalankan server Kotlin
├── gradlew.bat                         # Gradle Wrapper Windows
├── settings.gradle.kts                 # Multi-module settings (:app & :server)
└── build.gradle.kts                    # Root build configuration
```

---

## 🛠️ Spesifikasi Teknologi

- **Bahasa**: Kotlin (100% di Client Android & Backend Server)
- **UI Framework**: Android Jetpack Compose + Material Design 3
- **Database**: MySQL 5.7+ / MySQL 8.0+ / MariaDB 10.4+ (XAMPP / Laragon)
- **Komunikasi Data**: REST API (Retrofit 2, Gson, OkHttp)
- **Image Loader**: Coil Compose 2.7.0
- **Navigation**: Jetpack Navigation Compose 2.8.5
- **Target SDK**: Android 35 (Android 15) | **Min SDK**: Android 24 (Android 7.0+)
