package com.example.naverAPI

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.*
import java.net.URLEncoder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object APIManager {
    private val client = OkHttpClient()
    private val clientId = "" // 네이버 클라이언트 ID
    private val clientSecret = "" // 네이버 클라이언트 비밀번호
    private val translationUrl = "https://openapi.naver.com/v1/papago/n2mt"
    private val detectionUrl = "https://openapi.naver.com/v1/papago/detectLangs"

    // 한국어를 영어로 번역하는 함수
    fun translateKoreaToEng(input: String, callback: (String) -> Unit) {
        Log.d("IISE", "받은 한글 : $input")
        runTranslateRequest(input, "ko", "en", callback)
    }

    // 영어를 한국어로 번역하는 함수
    fun translateEngToKorea(input: String, callback: (String) -> Unit) {
        Log.d("IISE", "받은 영어 : $input")
        runTranslateRequest(input, "en", "ko", callback)
    }

    // 번역 요청을 처리하는 함수
    private fun runTranslateRequest(
        input: String,
        sourceLang: String,
        targetLang: String,
        callback: (String) -> Unit
    ) {
        val requestBody = createRequestBody(input, sourceLang, targetLang)
        val request = createRequest(translationUrl, requestBody)
        Log.d("IISE", "받은 원본 : $input")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val translatedText = extractTranslation(responseBody)
                callback(translatedText)
            }
        })
    }

    // 요청 본문을 생성하는 함수
    private fun createRequestBody(text: String, sourceLang: String, targetLang: String): RequestBody {
        return FormBody.Builder()
            .add("source", sourceLang)
            .add("target", targetLang)
            .add("text", text)
            .build()
    }

    // 요청을 생성하는 함수
    private fun createRequest(url: String, body: RequestBody): Request {
        return Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientId)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .post(body)
            .build()
    }

    // 번역된 텍스트를 추출하는 함수
    private fun extractTranslation(json: String?): String {
        if (json == null) {
            return ""
        }
        val response = Gson().fromJson(json, ResponseWrapper::class.java)
        val result = response.message.result.translatedText
        Log.d("IISE", "Processed response extractTranslation: $result")
        return result
    }

    // 언어를 감지하는 함수
    fun detect(text: String, callback: (String) -> Unit) {
        val requestBody = "query=${text.encodeURIComponent()}".toRequestBody(
            "application/x-www-form-urlencoded".toMediaType()
        )
        val request = createRequest(detectionUrl, requestBody)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val langCode = extractLanguageCode(responseBody)
                Log.d("IISE", "langCode: $langCode")
                callback(langCode)
            }
        })
    }

    // 언어 코드를 추출하는 함수
    private fun extractLanguageCode(json: String?): String {
        if (json.isNullOrEmpty()) {
            Log.d("IISE", "Failed to detect language.")
            return ""
        }
        val jsonObject = JSONObject(json)
        val result = jsonObject.optString("langCode", "").also {
            if (it.isEmpty()) Log.d("IISE", "Failed to detect language.")
        }
        Log.d("IISE", "언어 코드 $result")
        return result
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
