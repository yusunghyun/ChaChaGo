package com.example.naverAPI

import android.util.Log
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApiService {
    @Headers("Authorization: Bearer ")
    @POST("v1/chat/completions")
    fun createPost(@Body myPost: MyPost): Call<MyResponse>
}

object NetworkService {
    val api: OpenAIApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        // Custom Interceptor to log events
        val customInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()

            val response = chain.proceed(originalRequest)

            response
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(customInterceptor) // Add our custom interceptor
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OpenAIApiService::class.java)
    }
}
