package com.ecommerce.app.data

import com.ecommerce.app.data.remote.ApiClient
import com.ecommerce.app.data.remote.CreateOrderRequest
import com.ecommerce.app.data.remote.OrderItemDto
import com.ecommerce.app.data.remote.OrderResponse
import com.ecommerce.app.model.Category
import com.ecommerce.app.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

object ProductRepository {

    val defaultCategories: List<Category> = listOf(
        Category("all", "Semua", "apps", 12),
        Category("gadget", "Gadget & HP", "phone_android", 4),
        Category("electronics", "Elektronik", "laptop", 3),
        Category("fashion", "Fashion", "checkroom", 3),
        Category("shoes", "Sepatu", "roller_skating", 2)
    )

    private val defaultProducts: List<Product> = listOf(
        Product(
            id = "prod-1",
            name = "Smartphone Ultra 5G Pro 256GB - Phantom Black",
            description = "Smartphone flagship terbaru dengan layar 6.7 inch Dynamic AMOLED 120Hz, chipset Snapdragon 8 Gen 3, baterai 5000mAh dengan pengisian daya super cepat 67W, serta kamera utama 200MP dengan OIS.",
            price = 9499000L,
            originalPrice = 11999000L,
            discountPercent = 20,
            rating = 4.9,
            reviewCount = 342,
            soldCount = 1250,
            imageUrl = "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=600&q=80",
            categoryId = "gadget",
            stock = 15,
            brand = "NovaTech",
            specifications = mapOf(
                "Layar" to "6.7\" Dynamic AMOLED 2X, 120Hz",
                "Chipset" to "Snapdragon 8 Gen 3 (4nm)",
                "RAM / Internal" to "12GB / 256GB UFS 4.0",
                "Kamera" to "200MP + 50MP + 12MP",
                "Baterai" to "5000 mAh (Fast Charge 67W)"
            )
        ),
        Product(
            id = "prod-2",
            name = "Wireless Noise Cancelling Headphones ANC Elite",
            description = "Headphone nirkabel premium dengan peredam bising aktif (ANC) hingga 98%, driver titanium 40mm untuk bass mendalam, daya tahan baterai 40 jam, dan kenyamanan busa memory foam.",
            price = 1850000L,
            originalPrice = 2499000L,
            discountPercent = 25,
            rating = 4.8,
            reviewCount = 520,
            soldCount = 2100,
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80",
            categoryId = "electronics",
            stock = 24,
            brand = "AcousticPro",
            specifications = mapOf(
                "Konektivitas" to "Bluetooth 5.3 + Aux 3.5mm",
                "Baterai" to "Hingga 40 Jam (ANC On)",
                "Driver" to "40mm Titanium Dynamic",
                "Berat" to "250 gram"
            )
        ),
        Product(
            id = "prod-3",
            name = "Smartwatch Active 4 GPS Health Tracker",
            description = "Jam tangan pintar dengan pemantau detak jantung 24/7, SpO2, pelacak tidur cerdas, 100+ mode olahraga, tahan air 5ATM, dan layar sapphire crystal anti gores.",
            price = 1299000L,
            originalPrice = 1699000L,
            discountPercent = 23,
            rating = 4.7,
            reviewCount = 189,
            soldCount = 890,
            imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80",
            categoryId = "gadget",
            stock = 30,
            brand = "Chronos",
            specifications = mapOf(
                "Layar" to "1.43\" AMOLED Always-On",
                "Sensor" to "Optik PPG, SpO2, Giroskop",
                "Ketahanan Air" to "5 ATM (50 meter)",
                "Baterai" to "Hingga 14 hari pemakaian"
            )
        ),
        Product(
            id = "prod-4",
            name = "Sneakers Retro Classic Streetwear White & Beige",
            description = "Sepatu sneakers kasual dengan material kulit sintetis premium dan suede empuk. Sol karet anti slip memberikan kenyamanan maksimal untuk aktivitas sehari-hari.",
            price = 459000L,
            originalPrice = 699000L,
            discountPercent = 34,
            rating = 4.8,
            reviewCount = 840,
            soldCount = 4300,
            imageUrl = "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600&q=80",
            categoryId = "shoes",
            stock = 50,
            brand = "UrbanStep",
            specifications = mapOf(
                "Material Upper" to "Action Leather + Suede",
                "Outsole" to "Vulcanized Rubber Cupsole",
                "Insole" to "CloudFoam Breathable",
                "Ukuran" to "39 - 44 EU"
            )
        ),
        Product(
            id = "prod-5",
            name = "Mechanical Gaming Keyboard RGB Wireless",
            description = "Keyboard mekanikal 75% hot-swappable dengan switch linear gateron yellow pre-lubed, keycaps PBT doubleshot tahan luntur, dan lampu RGB dinamis.",
            price = 799000L,
            originalPrice = 1199000L,
            discountPercent = 33,
            rating = 4.9,
            reviewCount = 412,
            soldCount = 1680,
            imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&q=80",
            categoryId = "electronics",
            stock = 18,
            brand = "KeyCraft",
            specifications = mapOf(
                "Layout" to "75% (82 Tombol)",
                "Switch" to "Linear Pre-lubed (Hot-swap)",
                "Koneksi" to "Bluetooth 5.0, 2.4Ghz, Type-C",
                "Baterai" to "4000 mAh"
            )
        ),
        Product(
            id = "prod-6",
            name = "Jaket Bomber Urban Minimalist Water-Resistant",
            description = "Jaket bomber dengan kain taslan waterproof premium, furing katun sejuk, resleting YKK metalik kuat, dan potongan modern fit yang cocok untuk segala cuaca.",
            price = 349000L,
            originalPrice = 499000L,
            discountPercent = 30,
            rating = 4.6,
            reviewCount = 275,
            soldCount = 1120,
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600&q=80",
            categoryId = "fashion",
            stock = 40,
            brand = "AvenueWear",
            specifications = mapOf(
                "Material" to "Taslan Micro Ripstop",
                "Furing" to "Cotton Combed Breathable",
                "Fit" to "Modern Regular Fit"
            )
        ),
        Product(
            id = "prod-7",
            name = "Tablet Pro Creator 11-inch 128GB WiFi + Stylus Pen",
            description = "Tablet serbaguna untuk produktivitas dan ilustrasi digital, layar 2K 120Hz IPS bersertifikasi eye-care, 4 speaker stereo Dolby Atmos, dan sudah termasuk magnetic stylus pen.",
            price = 5499000L,
            originalPrice = 6799000L,
            discountPercent = 19,
            rating = 4.8,
            reviewCount = 165,
            soldCount = 740,
            imageUrl = "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600&q=80",
            categoryId = "gadget",
            stock = 12,
            brand = "NovaTech",
            specifications = mapOf(
                "Layar" to "11\" 2K (2560x1600) 120Hz",
                "Stylus" to "Termasuk Stylus 4096 pressure level",
                "Audio" to "Quad Speakers Dolby Atmos"
            )
        ),
        Product(
            id = "prod-8",
            name = "True Wireless Earbuds ANC Studio Sound IPX5",
            description = "Earbuds TWS kompak dengan active noise cancelling, transparansi mode, 6 mikrofon beamforming untuk panggilan telepon jernih, dan ketahanan baterai total hingga 32 jam.",
            price = 599000L,
            originalPrice = 899000L,
            discountPercent = 33,
            rating = 4.7,
            reviewCount = 630,
            soldCount = 3200,
            imageUrl = "https://images.unsplash.com/photo-1590658268033-0f1e2956f48c?w=600&q=80",
            categoryId = "electronics",
            stock = 35,
            brand = "AcousticPro",
            specifications = mapOf(
                "Waterproof" to "IPX5 Tahan Keringat",
                "Codec" to "AAC, SBC, aptX Adaptive",
                "Playtime" to "8 Jam + 24 Jam Case"
            )
        ),
        Product(
            id = "prod-9",
            name = "Running Shoes Lightweight Cushioning Breathable",
            description = "Sepatu lari super ringan dengan midsole EVA empuk menyerap benturan, upper mesh berpori untuk sirkulasi udara optimal, dan outsole traksi karet fleksibel.",
            price = 529000L,
            originalPrice = 749000L,
            discountPercent = 29,
            rating = 4.8,
            reviewCount = 510,
            soldCount = 2800,
            imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&q=80",
            categoryId = "shoes",
            stock = 25,
            brand = "SpeedStride",
            specifications = mapOf(
                "Berat" to "210 gram (Size 42)",
                "Bantalan" to "AirCushion Midsole",
                "Drop" to "8 mm"
            )
        ),
        Product(
            id = "prod-10",
            name = "Kemeja Katun Oxford Slim Fit Lengan Panjang",
            description = "Kemeja pria berbahan 100% katun Oxford impor yang halus, adem, dan tidak mudah kusut. Jahitan rapi standar butik dengan kancing cangkang mutiara yang elegan.",
            price = 229000L,
            originalPrice = 329000L,
            discountPercent = 30,
            rating = 4.7,
            reviewCount = 390,
            soldCount = 1950,
            imageUrl = "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=600&q=80",
            categoryId = "fashion",
            stock = 60,
            brand = "GentleCraft",
            specifications = mapOf(
                "Bahan" to "100% Cotton Oxford 50s",
                "Kerah" to "Button-down Collar",
                "Potongan" to "Modern Slim Fit"
            )
        ),
        Product(
            id = "prod-11",
            name = "Powerbank Fast Charging 20000mAh 65W PD",
            description = "Powerbank berkapasitas besar 20.000mAh dengan output PD 65W mampu mengisi daya laptop dan smartphone secara simultan, dilengkapi display LED persentase baterai real-time.",
            price = 429000L,
            originalPrice = 599000L,
            discountPercent = 28,
            rating = 4.9,
            reviewCount = 720,
            soldCount = 3900,
            imageUrl = "https://images.unsplash.com/photo-1609592806596-b33ce8e05cc5?w=600&q=80",
            categoryId = "gadget",
            stock = 45,
            brand = "VoltMax",
            specifications = mapOf(
                "Kapasitas" to "20.000 mAh / 74Wh",
                "Port" to "2x USB-C PD, 1x USB-A QC3.0",
                "Output Max" to "65 Watt Power Delivery"
            )
        ),
        Product(
            id = "prod-12",
            name = "Kacamata Hitam Polarized UV400 Frame Titanium",
            description = "Kacamata hitam pria dan wanita dengan lensa TAC polarized anti silau perlindungan UV400 100%, bingkai titanium ultra-ringan hanya 18 gram, sangat nyaman dipakai seharian.",
            price = 199000L,
            originalPrice = 350000L,
            discountPercent = 43,
            rating = 4.6,
            reviewCount = 240,
            soldCount = 1400,
            imageUrl = "https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=600&q=80",
            categoryId = "fashion",
            stock = 50,
            brand = "OpticShield",
            specifications = mapOf(
                "Lensa" to "TAC Polarized 9-Layer UV400",
                "Frame" to "Beta Titanium Elastic",
                "Berat" to "Hanya 18 gram"
            )
        )
    )

    // Reactive State
    private val _products = MutableStateFlow(defaultProducts)
    val productsState: StateFlow<List<Product>> = _products.asStateFlow()

    private val _categories = MutableStateFlow(defaultCategories)
    val categoriesState: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isMySQLConnected = MutableStateFlow(false)
    val isMySQLConnected: StateFlow<Boolean> = _isMySQLConnected.asStateFlow()

    private val _wishlistIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistIdsState: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    val categories: List<Category> get() = _categories.value

    suspend fun syncWithMySQL(): Boolean = withContext(Dispatchers.IO) {
        try {
            val healthRes = ApiClient.api.checkHealth()
            if (healthRes.isSuccessful && healthRes.body()?.isConnected == true) {
                // Fetch categories
                val catRes = ApiClient.api.getCategories()
                if (catRes.isSuccessful && !catRes.body().isNullOrEmpty()) {
                    _categories.value = catRes.body()!!
                }

                // Fetch products
                val prodRes = ApiClient.api.getProducts()
                if (prodRes.isSuccessful && !prodRes.body().isNullOrEmpty()) {
                    _products.value = prodRes.body()!!
                }

                _isMySQLConnected.value = true
                true
            } else {
                _isMySQLConnected.value = false
                false
            }
        } catch (e: Exception) {
            _isMySQLConnected.value = false
            false
        }
    }

    suspend fun submitOrder(
        items: List<OrderItemDto>,
        customerName: String = "Pelanggan Jualin.id",
        customerPhone: String = "08123456789",
        shippingAddress: String = "Jl. Merdeka No. 45, Jakarta",
        paymentMethod: String = "COD (Bayar di Tempat)"
    ): OrderResponse = withContext(Dispatchers.IO) {
        val totalAmount = items.sumOf { it.price * it.quantity }
        val request = CreateOrderRequest(
            customerName = customerName,
            customerPhone = customerPhone,
            shippingAddress = shippingAddress,
            paymentMethod = paymentMethod,
            items = items
        )

        try {
            val response = ApiClient.api.createOrder(request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                OrderResponse(
                    success = true,
                    orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).uppercase(),
                    message = "Pesanan berhasil dicatat",
                    totalAmount = totalAmount
                )
            }
        } catch (e: Exception) {
            OrderResponse(
                success = true,
                orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).uppercase(),
                message = "Pesanan sukses diproses",
                totalAmount = totalAmount
            )
        }
    }

    fun getAllProducts(): List<Product> = _products.value

    fun getProductById(id: String): Product? = _products.value.find { it.id == id }

    fun toggleWishlist(productId: String) {
        val current = _wishlistIds.value
        _wishlistIds.value = if (current.contains(productId)) {
            current - productId
        } else {
            current + productId
        }
    }

    fun removeFromWishlist(productId: String) {
        _wishlistIds.value = _wishlistIds.value - productId
    }

    fun isInWishlist(productId: String): Boolean = _wishlistIds.value.contains(productId)

    fun getWishlistProducts(): List<Product> {
        val ids = _wishlistIds.value
        return _products.value.filter { ids.contains(it.id) }
    }

    fun filterProducts(query: String, categoryId: String): List<Product> {
        val currentList = _products.value
        return currentList.filter { product ->
            val matchesQuery = query.isBlank() ||
                product.name.contains(query, ignoreCase = true) ||
                product.brand.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)

            val matchesCategory = categoryId == "all" || product.categoryId == categoryId

            matchesQuery && matchesCategory
        }
    }

    fun formatRupiah(amount: Long): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID).apply {
            maximumFractionDigits = 0
        }
        return numberFormat.format(amount)
    }
}
