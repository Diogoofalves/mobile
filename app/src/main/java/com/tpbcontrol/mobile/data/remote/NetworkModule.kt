package com.tpbcontrol.mobile.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tpbcontrol.mobile.data.local.TokenStore
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking

private const val BASE_URL = "http://10.0.2.2:3001/api/"

object NetworkModule {
    fun provideApi(tokenStore: TokenStore): TPBApi {
        val client = provideHttpClient(tokenStore)
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TPBApi::class.java)
    }

    private fun provideHttpClient(tokenStore: TokenStore): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val token = runBlocking { tokenStore.currentToken() }
            if (!token.isNullOrBlank()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
