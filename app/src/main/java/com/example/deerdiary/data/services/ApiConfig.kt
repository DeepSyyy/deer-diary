package com.example.deerdiary.data.services

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        @Suppress("ktlint:standard:property-naming")
        var base_url = "https://story-api.dicoding.dev/v1/"

        fun getApiService(token: String): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val authInterceptor =
                Interceptor { chain ->
                    val request = chain.request()
                    val requestHeader =
                        request.newBuilder().addHeader("Authorization", "Bearer $token")
                            .build()
                    chain.proceed(requestHeader)
                }
            val client =
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(authInterceptor)
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
