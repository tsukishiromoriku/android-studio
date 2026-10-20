package com.ecommerce.app.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Long,
    val originalPrice: Long? = null,
    val discountPercent: Int = 0,
    val rating: Double,
    val reviewCount: Int,
    val soldCount: Int,
    val imageUrl: String,
    val categoryId: String,
    val isFavorite: Boolean = false,
    val stock: Int = 10,
    val brand: String = "Jualin.id Official",
    val specifications: Map<String, String> = emptyMap()
)
