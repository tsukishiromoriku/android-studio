package com.ecommerce.server

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

object DatabaseManager {

    private val dbHost = System.getenv("MYSQL_HOST") ?: "localhost"
    private val dbPort = System.getenv("MYSQL_PORT") ?: "3306"
    private val dbName = System.getenv("MYSQL_DATABASE") ?: "ecommerce_db"
    private val dbUser = System.getenv("MYSQL_USER") ?: "root"
    private val dbPassword = System.getenv("MYSQL_PASSWORD") ?: ""

    private val jdbcUrl = "jdbc:mysql://$dbHost:$dbPort/$dbName?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta&characterEncoding=UTF-8"

    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            println("[ERROR] MySQL Driver not found: ${e.message}")
        }
    }

    private fun getConnection(): Connection {
        return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)
    }

    fun isConnected(): Boolean {
        return try {
            getConnection().use { conn ->
                conn.isValid(2)
            }
        } catch (e: Exception) {
            false
        }
    }

    fun getProductCount(): Int {
        return try {
            getConnection().use { conn ->
                val sql = "SELECT COUNT(*) FROM products"
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    if (rs.next()) rs.getInt(1) else 0
                }
            }
        } catch (e: Exception) {
            0
        }
    }

    fun getCategories(): List<CategoryDto> {
        val list = mutableListOf<CategoryDto>()
        try {
            getConnection().use { conn ->
                val sql = "SELECT id, name, icon_name, item_count FROM categories ORDER BY id = 'all' DESC, name ASC"
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        list.add(
                            CategoryDto(
                                id = rs.getString("id"),
                                name = rs.getString("name"),
                                iconName = rs.getString("icon_name"),
                                itemCount = rs.getInt("item_count")
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("[DB] getCategories error: ${e.message}")
        }
        return list
    }

    fun getProducts(categoryId: String? = null, query: String? = null): List<ProductDto> {
        val products = mutableListOf<ProductDto>()
        try {
            getConnection().use { conn ->
                val sb = java.lang.StringBuilder("SELECT * FROM products WHERE 1=1")
                val params = mutableListOf<Any>()

                if (!categoryId.isNullOrBlank() && categoryId != "all") {
                    sb.append(" AND category_id = ?")
                    params.add(categoryId)
                }

                if (!query.isNullOrBlank()) {
                    sb.append(" AND (LOWER(name) LIKE ? OR LOWER(brand) LIKE ? OR LOWER(description) LIKE ?)")
                    val searchPattern = "%${query.trim().lowercase()}%"
                    params.add(searchPattern)
                    params.add(searchPattern)
                    params.add(searchPattern)
                }

                sb.append(" ORDER BY sold_count DESC, id ASC")

                val specMap = getSpecificationsMap(conn)

                conn.prepareStatement(sb.toString()).use { stmt ->
                    params.forEachIndexed { index, param ->
                        stmt.setObject(index + 1, param)
                    }
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        val prodId = rs.getString("id")
                        products.add(
                            ProductDto(
                                id = prodId,
                                categoryId = rs.getString("category_id"),
                                name = rs.getString("name"),
                                description = rs.getString("description"),
                                price = rs.getLong("price"),
                                originalPrice = rs.getObject("original_price") as? Long,
                                discountPercent = rs.getInt("discount_percent"),
                                rating = rs.getDouble("rating"),
                                reviewCount = rs.getInt("review_count"),
                                soldCount = rs.getInt("sold_count"),
                                imageUrl = rs.getString("image_url"),
                                stock = rs.getInt("stock"),
                                brand = rs.getString("brand") ?: "TokoKu Official",
                                isFavorite = rs.getBoolean("is_favorite"),
                                specifications = specMap[prodId] ?: emptyMap()
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("[DB] getProducts error: ${e.message}")
        }
        return products
    }

    fun getProductById(id: String): ProductDto? {
        try {
            getConnection().use { conn ->
                val sql = "SELECT * FROM products WHERE id = ?"
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id)
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        val prodId = rs.getString("id")
                        val specs = getProductSpecifications(conn, prodId)
                        return ProductDto(
                            id = prodId,
                            categoryId = rs.getString("category_id"),
                            name = rs.getString("name"),
                            description = rs.getString("description"),
                            price = rs.getLong("price"),
                            originalPrice = rs.getObject("original_price") as? Long,
                            discountPercent = rs.getInt("discount_percent"),
                            rating = rs.getDouble("rating"),
                            reviewCount = rs.getInt("review_count"),
                            soldCount = rs.getInt("sold_count"),
                            imageUrl = rs.getString("image_url"),
                            stock = rs.getInt("stock"),
                            brand = rs.getString("brand") ?: "TokoKu Official",
                            isFavorite = rs.getBoolean("is_favorite"),
                            specifications = specs
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("[DB] getProductById error: ${e.message}")
        }
        return null
    }

    fun getBanners(): List<BannerDto> {
        val list = mutableListOf<BannerDto>()
        try {
            getConnection().use { conn ->
                val sql = "SELECT * FROM promo_banners WHERE is_active = 1"
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        list.add(
                            BannerDto(
                                id = rs.getString("id"),
                                title = rs.getString("title"),
                                subtitle = rs.getString("subtitle"),
                                discountText = rs.getString("discount_text"),
                                badgeText = rs.getString("badge_text"),
                                isActive = rs.getBoolean("is_active")
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("[DB] getBanners error: ${e.message}")
        }
        return list
    }

    fun createOrder(request: CreateOrderRequest): OrderResponse {
        val orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
        val orderId = "ORD-$orderDate-${UUID.randomUUID().toString().substring(0, 4).uppercase()}"

        val totalAmount = request.items.sumOf { it.price * it.quantity }

        try {
            getConnection().use { conn ->
                conn.autoCommit = false
                try {
                    // 1. Insert order
                    val insertOrderSql = """
                        INSERT INTO orders (id, customer_name, customer_phone, shipping_address, total_amount, payment_method, status)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """.trimIndent()
                    conn.prepareStatement(insertOrderSql).use { stmt ->
                        stmt.setString(1, orderId)
                        stmt.setString(2, request.customerName ?: "Pelanggan TokoKu")
                        stmt.setString(3, request.customerPhone ?: "08123456789")
                        stmt.setString(4, request.shippingAddress ?: "Alamat Pengiriman")
                        stmt.setLong(5, totalAmount)
                        stmt.setString(6, request.paymentMethod ?: "COD (Bayar di Tempat)")
                        stmt.setString(7, "Diproses")
                        stmt.executeUpdate()
                    }

                    // 2. Insert order items & reduce stock
                    val insertItemSql = """
                        INSERT INTO order_items (order_id, product_id, product_name, price, quantity, subtotal)
                        VALUES (?, ?, ?, ?, ?, ?)
                    """.trimIndent()

                    val updateStockSql = "UPDATE products SET stock = GREATEST(0, stock - ?), sold_count = sold_count + ? WHERE id = ?"

                    request.items.forEach { item ->
                        conn.prepareStatement(insertItemSql).use { stmt ->
                            stmt.setString(1, orderId)
                            stmt.setString(2, item.productId)
                            stmt.setString(3, item.productName)
                            stmt.setLong(4, item.price)
                            stmt.setInt(5, item.quantity)
                            stmt.setLong(6, item.price * item.quantity)
                            stmt.executeUpdate()
                        }

                        conn.prepareStatement(updateStockSql).use { stmt ->
                            stmt.setInt(1, item.quantity)
                            stmt.setInt(2, item.quantity)
                            stmt.setString(3, item.productId)
                            stmt.executeUpdate()
                        }
                    }

                    conn.commit()
                    return OrderResponse(
                        success = true,
                        orderId = orderId,
                        message = "Pesanan berhasil disimpan di database MySQL!",
                        totalAmount = totalAmount
                    )
                } catch (e: Exception) {
                    conn.rollback()
                    throw e
                } finally {
                    conn.autoCommit = true
                }
            }
        } catch (e: Exception) {
            println("[DB] createOrder error: ${e.message}")
            return OrderResponse(
                success = false,
                orderId = "",
                message = "Gagal menyimpan pesanan: ${e.message}",
                totalAmount = totalAmount
            )
        }
    }

    private fun getSpecificationsMap(conn: Connection): Map<String, Map<String, String>> {
        val result = mutableMapOf<String, MutableMap<String, String>>()
        try {
            val sql = "SELECT product_id, spec_key, spec_value FROM product_specifications"
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    val pId = rs.getString("product_id")
                    val key = rs.getString("spec_key")
                    val value = rs.getString("spec_value")
                    val map = result.getOrPut(pId) { mutableMapOf() }
                    map[key] = value
                }
            }
        } catch (e: Exception) {
            println("[DB] getSpecificationsMap error: ${e.message}")
        }
        return result
    }

    private fun getProductSpecifications(conn: Connection, productId: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        try {
            val sql = "SELECT spec_key, spec_value FROM product_specifications WHERE product_id = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, productId)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    result[rs.getString("spec_key")] = rs.getString("spec_value")
                }
            }
        } catch (e: Exception) {
            println("[DB] getProductSpecifications error: ${e.message}")
        }
        return result
    }
}
