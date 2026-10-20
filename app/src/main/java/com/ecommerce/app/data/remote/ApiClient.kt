package com.ecommerce.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // 10.0.2.2 adalah IP loopback host PC dari dalam Android Emulator
    private const val DEFAULT_EMULATOR_BASE_URL = "http://10.0.2.2:8080/"

    private var currentBaseUrl: String = DEFAULT_EMULATOR_BASE_URL
    private var cachedApi: EcommerceApi? = null

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .retryOnConnectionFailure(true)
            .build()
    }

    val api: EcommerceApi
        get() {
            if (cachedApi == null) {
                cachedApi = Retrofit.Builder()
                    .baseUrl(currentBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(EcommerceApi::class.java)
            }
            return cachedApi!!
        }

    fun setCustomBaseUrl(newBaseUrl: String) {
        val formatted = if (newBaseUrl.endsWith("/")) newBaseUrl else "$newBaseUrl/"
        currentBaseUrl = formatted
        cachedApi = null
    }

    fun getBaseUrl(): String = currentBaseUrl
}
