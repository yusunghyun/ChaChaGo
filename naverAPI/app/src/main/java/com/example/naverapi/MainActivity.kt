package com.example.naverapi

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.naverapi.databinding.ActivityMainBinding
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.translatebtn.setOnClickListener{
            translate()

        }

    }

    private fun translate() {
        val client = OkHttpClient()

        val url = "https://openapi.naver.com/v1/papago/n2mt"
        val requestBody = FormBody.Builder()
            .add("source", "ko")
            .add("target", "en")
            .add("text", binding.input.text.toString())
            .build()

        val request = Request.Builder()
            .addHeader("X-Naver-Client-Id", "jBcll4iM5Oi6E83S5dK0")
            .addHeader("X-Naver-Client-Secret", "JgFVxhhsQj")
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // API 호출 실패 시 처리하는 코드
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // API 호출 성공 시 처리하는 코드
                val result = response.body?.string()
                val translatedText = getTranslatedText(result)
                runOnUiThread {
                    binding.output.text = translatedText.toString()
                }
            }
        })
    }

    private fun getTranslatedText(json: String?): String {
        // 파파고 API 호출 결과에서 번역된 텍스트를 추출하는 함수
        // 추출하는 방법은 JSON 파싱 라이브러리를 사용하거나, 정규식을 이용할 수 있습니다.
        // 여기서는 간단하게 문자열을 추출하는 방법을 사용합니다.
        if (json == null) {
            return ""
        }

        val startIndex = json.indexOf("\"translatedText\":\"") + 18
        val endIndex = json.indexOf("\"}", startIndex)
        return json.substring(startIndex, endIndex)
    }
}

