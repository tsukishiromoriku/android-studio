package com.ecommerce.server

data class CategoryDto(
    val id: String,
    val name: String,
    val iconName: String,
    val itemCount: Int
)

data class ProductDto(
    val id: String,
    val name: String,
    val description: String,
    val price: Long,
    val originalPrice: Long? = null,
    val discountPercent: Int = 0,
    val rating: Double = 5.0,
    val reviewCount: Int = 0,
    val soldCount: Int = 0,
    val imageUrl: String,
    val categoryId: String,
    val isFavorite: Boolean = false,
    val stock: Int = 10,
    val brand: String = "TokoKu Official",
    val specifications: Map<String, String> = emptyMap()
)

data class BannerDto(
    val id: String,
    val title: String,
    val subtitle: String,
    val discountText: String,
    val badgeText: String,
    val isActive: Boolean = true
)

data class OrderItemRequest(
    val productId: String,
    val productName: String,
    val price: Long,
    val quantity: Int
)

data class CreateOrderRequest(
    val customerName: String? = "Pelanggan TokoKu",
    val customerPhone: String? = "08123456789",
    val shippingAddress: String? = "Alamat Pengiriman",
    val paymentMethod: String? = "COD (Bayar di Tempat)",
    val items: List<OrderItemRequest> = emptyList()
)

data class OrderResponse(
    val success: Boolean,
    val orderId: String,
    val message: String,
    val totalAmount: Long
)

data class HealthResponse(
    val status: String,
    val database: String,
    val isConnected: Boolean,
    val productCount: Int
)
