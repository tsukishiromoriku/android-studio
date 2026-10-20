package com.ecommerce.app.data.remote

import com.ecommerce.app.model.Category
import com.ecommerce.app.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EcommerceApi {

    @GET("api/health")
    suspend fun checkHealth(): Response<HealthResponse>

    @GET("api/categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("api/products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("query") query: String? = null
    ): Response<List<Product>>

    @GET("api/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<Product>

    @POST("api/orders")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): Response<OrderResponse>
}
