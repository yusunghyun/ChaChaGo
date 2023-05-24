package com.example.naverAPI

import java.io.IOException
import okhttp3.*

object APIManager {
    private val client = OkHttpClient()

    fun translate(inputText: String, callback: (String) -> Unit) {
        val url = "https://openapi.naver.com/v1/papago/n2mt"
        val requestBody = FormBody.Builder()
            .add("source", "ko")
            .add("target", "en")
            .add("text", inputText)
            .build()

        val request = Request.Builder()
            .addHeader("X-Naver-Client-Id", "") // Insert Client ID
            .addHeader("X-Naver-Client-Secret", "") // Insert Client Secret
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                val translatedText = getTranslatedText(result)
                callback(translatedText)
            }
        })
    }

    private fun getTranslatedText(json: String?): String {
        if (json == null) {
            return ""
        }
        val startIndex = json.indexOf("\"translatedText\":\"") + 18
        val endIndex = json.indexOf("\"}", startIndex)
        return json.substring(startIndex, endIndex)
    }
}

