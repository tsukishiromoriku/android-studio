package com.ecommerce.app.data.remote

data class OrderItemDto(
    val productId: String,
    val productName: String,
    val price: Long,
    val quantity: Int
)

data class CreateOrderRequest(
    val customerName: String = "Pelanggan ShellIn",
    val customerPhone: String = "08123456789",
    val shippingAddress: String = "Alamat Pengiriman (Indonesia)",
    val paymentMethod: String = "COD (Bayar di Tempat)",
    val items: List<OrderItemDto>
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
