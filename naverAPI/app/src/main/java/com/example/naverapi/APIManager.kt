package com.example.naverAPI

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

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

    fun translateEngtoKorea(inputText: String, callback: (String) -> Unit) {
        val url = "https://openapi.naver.com/v1/papago/n2mt"
        val requestBodys = FormBody.Builder()
            .add("source", "en")
            .add("target", "ko")
            .add("text", inputText)
            .build()

        val requests = Request.Builder()
            .addHeader("X-Naver-Client-Id", "") // Insert Client ID
            .addHeader("X-Naver-Client-Secret", "") // Insert Client Secret
            .url(url)
            .post(requestBodys)
            .build()

        client.newCall(requests).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                val results = response.body?.string()
                val translatedTexts = getTranslatedText(results)
                callback(translatedTexts)
            }
        })
    }

    private fun getTranslatedText(json: String?): String {
        if (json == null) {
            return ""
        }
        val gson = Gson()
        val response = gson.fromJson(json, ResponseWrapper::class.java)
        val result = response.message.result.translatedText
        Log.d("IISE", "papago response 가공: $result")
        return result
    }


    fun detect(text: String,callback: (String) -> Unit)  {
        val clientId = "" // 본인의 클라이언트 ID로 변경해주세요.
        val clientSecret = "" // 본인의 클라이언트 Secret으로 변경해주세요.

        val url = "https://openapi.naver.com/v1/papago/detectLangs"
        val requestBody = "query=${text.encodeURIComponent()}".toRequestBody("application/x-www-form-urlencoded".toMediaType())

        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientId)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val jsonObject = JSONObject(responseBody)
                    val langCode = try {
                        val langCodeValue = jsonObject.optString("langCode", "")
                        if (langCodeValue.isEmpty()) {
                            throw RuntimeException("Failed to detect language.")
                        }
                        langCodeValue
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ""
                    }
                    Log.d("iise7","$langCode")
                    callback(langCode)
                } else {
                    Log.d("iise","Failed to detect language.")
                }
            }
        })

    }

    fun String.encodeURIComponent(): String {
        return URLEncoder.encode(this, "UTF-8")
    }



}

data class ResponseWrapper(
    @SerializedName("@type") val type: String,
    @SerializedName("@service") val service: String,
    @SerializedName("@version") val version: String,
    val message: MessageResult
)

data class MessageResult(
    val result: TranslationResult
)

data class TranslationResult(
    val srcLangType: String,
    val tarLangType: String,
    val translatedText: String,
    val engineType: String
)
