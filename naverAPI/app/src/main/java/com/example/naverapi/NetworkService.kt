package com.example.naverAPI

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApiService {
    @Headers("Authorization: Bearer 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")
    @POST("v1/chat/completions")
    fun createPost(@Body myPost: MyPost): Call<MyResponse>
}

object NetworkService {
    val api: OpenAIApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OpenAIApiService::class.java)
    }
}
