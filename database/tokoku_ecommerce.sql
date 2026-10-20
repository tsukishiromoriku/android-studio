-- ========================================================
-- TokoKu E-Commerce MySQL Database Schema & Seed Data
-- ========================================================
-- Versi: 1.0.0
-- Karakter: utf8mb4 (Full Emoji & Bahasa Indonesia support)
-- Target: MySQL 5.7+, MySQL 8.0+, MariaDB 10.4+ (XAMPP/Laragon)
-- ========================================================

CREATE DATABASE IF NOT EXISTS `ecommerce_db`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `ecommerce_db`;

-- --------------------------------------------------------
-- 1. Table structure for table `categories`
-- --------------------------------------------------------
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `product_specifications`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `promo_banners`;
DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
    `id` VARCHAR(50) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `icon_name` VARCHAR(50) NOT NULL,
    `item_count` INT NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Dumping data for table `categories`
-- --------------------------------------------------------
INSERT INTO `categories` (`id`, `name`, `icon_name`, `item_count`) VALUES
('all', 'Semua', 'apps', 12),
('gadget', 'Gadget & HP', 'phone_android', 4),
('electronics', 'Elektronik', 'laptop', 3),
('fashion', 'Fashion', 'checkroom', 3),
('shoes', 'Sepatu', 'roller_skating', 2);

-- --------------------------------------------------------
-- 2. Table structure for table `products`
-- --------------------------------------------------------
CREATE TABLE `products` (
    `id` VARCHAR(50) NOT NULL,
    `category_id` VARCHAR(50) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `price` BIGINT NOT NULL,
    `original_price` BIGINT DEFAULT NULL,
    `discount_percent` INT NOT NULL DEFAULT 0,
    `rating` DECIMAL(3, 2) NOT NULL DEFAULT 5.00,
    `review_count` INT NOT NULL DEFAULT 0,
    `sold_count` INT NOT NULL DEFAULT 0,
    `image_url` TEXT NOT NULL,
    `stock` INT NOT NULL DEFAULT 10,
    `brand` VARCHAR(100) NOT NULL DEFAULT 'TokoKu Official',
    `is_favorite` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_products_category` (`category_id`),
    CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Dumping data for table `products`
-- --------------------------------------------------------
INSERT INTO `products` (`id`, `category_id`, `name`, `description`, `price`, `original_price`, `discount_percent`, `rating`, `review_count`, `sold_count`, `image_url`, `stock`, `brand`, `is_favorite`) VALUES
('prod-1', 'gadget', 'Smartphone Ultra 5G Pro 256GB - Phantom Black', 'Smartphone flagship terbaru dengan layar 6.7 inch Dynamic AMOLED 120Hz, chipset Snapdragon 8 Gen 3, baterai 5000mAh dengan pengisian daya super cepat 67W, serta kamera utama 200MP dengan OIS.', 9499000, 11999000, 20, 4.90, 342, 1250, 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&q=80', 15, 'NovaTech', 1),
('prod-2', 'electronics', 'Wireless Noise Cancelling Headphones ANC Elite', 'Headphone nirkabel premium dengan peredam bising aktif (ANC) hingga 98%, driver titanium 40mm untuk bass mendalam, daya tahan baterai 40 jam, dan kenyamanan busa memory foam.', 1850000, 2499000, 25, 4.80, 520, 2100, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80', 24, 'AcousticPro', 0),
('prod-3', 'gadget', 'Smartwatch Active 4 GPS Health Tracker', 'Jam tangan pintar dengan pemantau detak jantung 24/7, SpO2, pelacak tidur cerdas, 100+ mode olahraga, tahan air 5ATM, dan layar sapphire crystal anti gores.', 1299000, 1699000, 23, 4.70, 189, 890, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80', 30, 'Chronos', 0),
('prod-4', 'shoes', 'Sneakers Retro Classic Streetwear White & Beige', 'Sepatu sneakers kasual dengan material kulit sintetis premium dan suede empuk. Sol karet anti slip memberikan kenyamanan maksimal untuk aktivitas sehari-hari.', 459000, 699000, 34, 4.80, 840, 4300, 'https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600&q=80', 50, 'UrbanStep', 1),
('prod-5', 'electronics', 'Mechanical Gaming Keyboard RGB Wireless Triple Mode', 'Keyboard mekanikal 75% hot-swappable dengan switch linear gateron yellow pre-lubed, keycaps PBT doubleshot tahan luntur, dan lampu RGB dinamis dengan software kustomisasi.', 799000, 1199000, 33, 4.90, 412, 1680, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&q=80', 18, 'KeyCraft', 0),
('prod-6', 'fashion', 'Jaket Bomber Urban Minimalist Water-Resistant', 'Jaket bomber dengan kain taslan waterproof premium, furing katun sejuk, resleting YKK metalik kuat, dan potongan modern fit yang cocok untuk segala cuaca.', 349000, 499000, 30, 4.60, 275, 1120, 'https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600&q=80', 40, 'AvenueWear', 0),
('prod-7', 'gadget', 'Tablet Pro Creator 11-inch 128GB WiFi + Stylus Pen', 'Tablet serbaguna untuk produktivitas dan ilustrasi digital, layar 2K 120Hz IPS bersertifikasi eye-care, 4 speaker stereo Dolby Atmos, dan sudah termasuk magnetic stylus pen berpresisi tinggi.', 5499000, 6799000, 19, 4.80, 165, 740, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600&q=80', 12, 'NovaTech', 0),
('prod-8', 'electronics', 'True Wireless Earbuds ANC Studio Sound IPX5', 'Earbuds TWS kompak dengan active noise cancelling, transparansi mode, 6 mikrofon beamforming untuk panggilan telepon jernih, dan ketahanan baterai total hingga 32 jam.', 599000, 899000, 33, 4.70, 630, 3200, 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&q=80', 35, 'AcousticPro', 0),
('prod-9', 'shoes', 'Running Shoes Lightweight Cushioning Breathable Mesh', 'Sepatu lari super ringan dengan midsole EVA empuk menyerap benturan, upper mesh berpori untuk sirkulasi udara optimal, dan outsole traksi karet fleksibel.', 529000, 749000, 29, 4.80, 510, 2800, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&q=80', 25, 'SpeedStride', 0),
('prod-10', 'fashion', 'Kemeja Katun Oxford Slim Fit Lengan Panjang', 'Kemeja pria berbahan 100% katun Oxford impor yang halus, adem, dan tidak mudah kusut. Jahitan rapi standar butik dengan kancing cangkang mutiara yang elegan.', 229000, 329000, 30, 4.70, 390, 1950, 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&q=80', 60, 'GentleCraft', 0),
('prod-11', 'gadget', 'Powerbank Fast Charging 20000mAh 65W Power Delivery', 'Powerbank berkapasitas besar 20.000mAh dengan output PD 65W mampu mengisi daya laptop dan smartphone secara simultan, dilengkapi display LED persentase baterai real-time.', 429000, 599000, 28, 4.90, 720, 3900, 'https://images.unsplash.com/photo-1609592806596-b33ce8e05cc5?w=600&q=80', 45, 'VoltMax', 1),
('prod-12', 'fashion', 'Kacamata Hitam Polarized UV400 Frame Titanium', 'Kacamata hitam pria dan wanita dengan lensa TAC polarized anti silau perlindungan UV400 100%, bingkai titanium ultra-ringan hanya 18 gram, sangat nyaman dipakai seharian.', 199000, 350000, 43, 4.60, 240, 1400, 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600&q=80', 50, 'OpticShield', 0);

-- --------------------------------------------------------
-- 3. Table structure for table `product_specifications`
-- --------------------------------------------------------
CREATE TABLE `product_specifications` (
    `id` INT AUTO_INCREMENT NOT NULL,
    `product_id` VARCHAR(50) NOT NULL,
    `spec_key` VARCHAR(100) NOT NULL,
    `spec_value` TEXT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_spec_product` (`product_id`),
    CONSTRAINT `fk_spec_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Dumping data for table `product_specifications`
-- --------------------------------------------------------
INSERT INTO `product_specifications` (`product_id`, `spec_key`, `spec_value`) VALUES
('prod-1', 'Layar', '6.7\" Dynamic AMOLED 2X, 120Hz, HDR10+'),
('prod-1', 'Chipset', 'Qualcomm Snapdragon 8 Gen 3 (4nm)'),
('prod-1', 'RAM / Internal', '12GB LPDDR5X / 256GB UFS 4.0'),
('prod-1', 'Kamera', '200MP Utama + 50MP Periskop + 12MP Ultra-wide'),
('prod-1', 'Baterai', '5000 mAh (Fast Charge 67W, Wireless 15W)'),

('prod-2', 'Konektivitas', 'Bluetooth 5.3 + Aux 3.5mm Gold-plated'),
('prod-2', 'Baterai', 'Hingga 40 Jam (ANC On) / 60 Jam (ANC Off)'),
('prod-2', 'Driver', '40mm Custom Titanium Dynamic Driver'),
('prod-2', 'Berat', '250 gram dengan Memory Foam Ear-cushions'),

('prod-3', 'Layar', '1.43\" AMOLED Always-On Retina Display'),
('prod-3', 'Sensor', 'Optik PPG Detak Jantung, SpO2, Kompas, Giroskop'),
('prod-3', 'Ketahanan Air', '5 ATM (Hingga kedalaman 50 meter)'),
('prod-3', 'Baterai', 'Hingga 14 hari pemakaian normal'),

('prod-4', 'Material Upper', 'Action Leather Sintetis Grade A + Suede'),
('prod-4', 'Outsole', 'Vulcanized High Traction Rubber Cupsole'),
('prod-4', 'Insole', 'CloudFoam Breathable Anti-Bacterial'),
('prod-4', 'Tersedia Ukuran', '39, 40, 41, 42, 43, 44 EU'),

('prod-5', 'Layout', '75% Compact (82 Keys) + Knob Volume'),
('prod-5', 'Switch', 'Gateron Yellow Linear Pre-lubed (Hot-swap 5-pin)'),
('prod-5', 'Koneksi', '2.4Ghz Wireless + Bluetooth 5.0 + Type-C Detachable'),
('prod-5', 'Baterai', '4000 mAh Li-ion Rechargeable'),

('prod-6', 'Material Luar', 'Premium Taslan Micro Ripstop Water-Repellent'),
('prod-6', 'Material Dalam', '100% Soft Cotton Breathable Lining'),
('prod-6', 'Resleting', 'YKK Metalic Heavy Duty Zipper'),
('prod-6', 'Saku', '2 Saku Luar + 1 Saku Dada Rahasia');

-- --------------------------------------------------------
-- 4. Table structure for table `promo_banners`
-- --------------------------------------------------------
CREATE TABLE `promo_banners` (
    `id` VARCHAR(50) NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `subtitle` VARCHAR(255) NOT NULL,
    `discount_text` VARCHAR(50) NOT NULL,
    `badge_text` VARCHAR(50) NOT NULL,
    `is_active` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `promo_banners` (`id`, `title`, `subtitle`, `discount_text`, `badge_text`, `is_active`) VALUES
('banner-1', 'Flash Sale Gajian!', 'Diskon kilat produk elektronik & gadget unggulan', 'Hingga 50% OFF', '⚡ PROMO TERBATAS', 1),
('banner-2', 'Gratis Ongkir Se-Indonesia', 'Tanpa minimum belanja untuk seluruh produk resmi', 'Hemat 100%', '🚚 FREE ONGKIR', 1),
('banner-3', 'Weekend Special Style', 'Koleksi fashion dan sepatu hits terbaru', 'Cashback 30%', '🏷️ SPESIAL AKHIR PEKAN', 1);

-- --------------------------------------------------------
-- 5. Table structure for table `orders`
-- --------------------------------------------------------
CREATE TABLE `orders` (
    `id` VARCHAR(50) NOT NULL,
    `customer_name` VARCHAR(100) NOT NULL,
    `customer_phone` VARCHAR(50) NOT NULL,
    `shipping_address` TEXT NOT NULL,
    `total_amount` BIGINT NOT NULL,
    `payment_method` VARCHAR(50) NOT NULL DEFAULT 'COD (Bayar di Tempat)',
    `status` VARCHAR(50) NOT NULL DEFAULT 'Menunggu Konfirmasi',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- 6. Table structure for table `order_items`
-- --------------------------------------------------------
CREATE TABLE `order_items` (
    `id` INT AUTO_INCREMENT NOT NULL,
    `order_id` VARCHAR(50) NOT NULL,
    `product_id` VARCHAR(50) NOT NULL,
    `product_name` VARCHAR(255) NOT NULL,
    `price` BIGINT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `subtotal` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_item_order` (`order_id`),
    CONSTRAINT `fk_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------
-- Sample initial order
-- --------------------------------------------------------
INSERT INTO `orders` (`id`, `customer_name`, `customer_phone`, `shipping_address`, `total_amount`, `payment_method`, `status`) VALUES
('ORD-20261019-001', 'Pelanggan TokoKu', '081234567890', 'Jl. Merdeka No. 45, Jakarta Selatan', 2309000, 'COD (Bayar di Tempat)', 'Diproses');

INSERT INTO `order_items` (`order_id`, `product_id`, `product_name`, `price`, `quantity`, `subtotal`) VALUES
('ORD-20261019-001', 'prod-2', 'Wireless Noise Cancelling Headphones ANC Elite', 1850000, 1, 1850000),
('ORD-20261019-001', 'prod-4', 'Sneakers Retro Classic Streetwear White & Beige', 459000, 1, 459000);

-- Selesai! Database siap digunakan.
