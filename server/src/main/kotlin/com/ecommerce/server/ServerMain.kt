package com.ecommerce.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

val gson: Gson = GsonBuilder().setPrettyPrinting().create()

fun main() {
    val port = (System.getenv("PORT") ?: "8080").toInt()
    val server = HttpServer.create(InetSocketAddress(port), 0)

    println("=========================================================")
    println("   🚀 TOKOKU BACKEND REST API SERVER (KOTLIN + MYSQL)")
    println("=========================================================")
    println("Port                : $port")
    println("Target Emulator     : http://10.0.2.2:$port")
    println("Target Localhost    : http://localhost:$port")

    val isConnected = DatabaseManager.isConnected()
    if (isConnected) {
        val count = DatabaseManager.getProductCount()
        println("Status Database     : ✅ TERHUBUNG KE MYSQL (ecommerce_db)")
        println("Jumlah Produk       : $count Produk")
    } else {
        println("Status Database     : ⚠️ BELUM TERHUBUNG KE MYSQL")
        println("                      Pastikan XAMPP / MySQL berjalan di port 3306")
        println("                      dan jalankan database/setup_database.bat")
    }
    println("=========================================================")

    // Register Handlers
    server.createContext("/api/health", HealthHandler())
    server.createContext("/api/categories", CategoriesHandler())
    server.createContext("/api/products", ProductsHandler())
    server.createContext("/api/banners", BannersHandler())
    server.createContext("/api/orders", OrdersHandler())

    server.executor = Executors.newFixedThreadPool(10)
    server.start()

    println("Server siap melayani request API TokoKu!")
    println("Tekan Ctrl+C untuk menghentikan server.")
}

abstract class BaseHandler : HttpHandler {
    override fun handle(exchange: HttpExchange) {
        // Handle CORS
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type, Authorization")
        exchange.responseHeaders.add("Content-Type", "application/json; charset=UTF-8")

        if (exchange.requestMethod.equals("OPTIONS", ignoreCase = true)) {
            exchange.sendResponseHeaders(204, -1)
            return
        }

        try {
            process(exchange)
        } catch (e: Exception) {
            System.err.println("[ERROR] ${exchange.requestURI}: ${e.message}")
            val errorResponse = mapOf("error" to (e.message ?: "Internal Server Error"))
            sendJson(exchange, 500, errorResponse)
        }
    }

    abstract fun process(exchange: HttpExchange)

    fun sendJson(exchange: HttpExchange, statusCode: Int, data: Any) {
        val json = gson.toJson(data)
        val bytes = json.toByteArray(StandardCharsets.UTF_8)
        exchange.sendResponseHeaders(statusCode, bytes.size.toLong())
        exchange.responseBody.use { os ->
            os.write(bytes)
        }
    }

    fun parseQueryParams(exchange: HttpExchange): Map<String, String> {
        val query = exchange.requestURI.rawQuery ?: return emptyMap()
        val result = mutableMapOf<String, String>()
        query.split("&").forEach { pair ->
            val parts = pair.split("=")
            if (parts.isNotEmpty()) {
                val key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8.name())
                val value = if (parts.size > 1) URLDecoder.decode(parts[1], StandardCharsets.UTF_8.name()) else ""
                result[key] = value
            }
        }
        return result
    }
}

class HealthHandler : BaseHandler() {
    override fun process(exchange: HttpExchange) {
        val connected = DatabaseManager.isConnected()
        val count = if (connected) DatabaseManager.getProductCount() else 0
        val health = HealthResponse(
            status = if (connected) "UP" else "DEGRADED",
            database = if (connected) "MySQL Connected (ecommerce_db)" else "MySQL Offline",
            isConnected = connected,
            productCount = count
        )
        sendJson(exchange, 200, health)
    }
}

class CategoriesHandler : BaseHandler() {
    override fun process(exchange: HttpExchange) {
        val categories = DatabaseManager.getCategories()
        sendJson(exchange, 200, categories)
    }
}

class ProductsHandler : BaseHandler() {
    override fun process(exchange: HttpExchange) {
        val path = exchange.requestURI.path
        // Check if path is /api/products/{id}
        val prefix = "/api/products/"
        if (path.length > prefix.length) {
            val id = path.substring(prefix.length)
            val product = DatabaseManager.getProductById(id)
            if (product != null) {
                sendJson(exchange, 200, product)
            } else {
                sendJson(exchange, 404, mapOf("error" to "Produk dengan ID '$id' tidak ditemukan"))
            }
            return
        }

        // List products with query / category filters
        val params = parseQueryParams(exchange)
        val categoryId = params["category"]
        val query = params["query"]

        val products = DatabaseManager.getProducts(categoryId, query)
        sendJson(exchange, 200, products)
    }
}

class BannersHandler : BaseHandler() {
    override fun process(exchange: HttpExchange) {
        val banners = DatabaseManager.getBanners()
        sendJson(exchange, 200, banners)
    }
}

class OrdersHandler : BaseHandler() {
    override fun process(exchange: HttpExchange) {
        if (!exchange.requestMethod.equals("POST", ignoreCase = true)) {
            sendJson(exchange, 405, mapOf("error" to "Method Not Allowed"))
            return
        }

        val reader = InputStreamReader(exchange.requestBody, StandardCharsets.UTF_8)
        val request = gson.fromJson(reader, CreateOrderRequest::class.java)

        if (request == null || request.items.isEmpty()) {
            sendJson(exchange, 400, mapOf("error" to "Daftar item pesanan tidak boleh kosong!"))
            return
        }

        val response = DatabaseManager.createOrder(request)
        val status = if (response.success) 201 else 500
        sendJson(exchange, status, response)
    }
}
