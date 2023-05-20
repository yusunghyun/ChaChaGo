package com.example.naverapi

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.naverapi.databinding.ActivityMainBinding
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.*
import java.io.IOException

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //gpt
        val userInput = findViewById<EditText>(R.id.userInput)
        val responseText = findViewById<TextView>(R.id.responseText)
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {
            val userMessage = Message("user", userInput.text.toString())
            val myPost = MyPost("gpt-3.5-turbo", listOf(userMessage))
            CoroutineScope(Dispatchers.IO).launch {
                val request = NetworkService.api.createPost(myPost)
                withContext(Dispatchers.Main) {
                    request.enqueue(object : Callback<MyResponse> {
                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            if (response.isSuccessful) {
                                val apiResponse = response.body()
                                responseText.text = "Response: ${apiResponse?.choices?.get(0)?.message?.content}"
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Error: ${response.errorBody()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Toast.makeText(
                                this@MainActivity,
                                "Failure: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                }
            }
        }

        var firstFragment = NaverFragment()                                                 // Naver fragment 생성
        var secondFragment = GptFragment.newInstance("","")                   // GPT fragment 생성 -> 명목상 프래그먼티라고 하는데 실질은 view page

        val myFrags = listOf(firstFragment, secondFragment)                                 //다음은 view page생성
        val fragAdapter = FragmentAdapter(this)
        fragAdapter.fragList = myFrags
        binding.vPage.adapter = fragAdapter

        val tabs = listOf("파파고","Chat GPT")                                                // view page 위에 있는 파파고, gpt 버튼
        TabLayoutMediator(binding.tabLayout, binding.vPage) {tab, postition ->
            tab.text = tabs.get(postition)
        }.attach()


        binding.translatebtn.setOnClickListener{                                       // 버튼 클릭시 이벤트 발생
            translate()                                                                       // 파파고 api 호출 통한 번역
            val inputToPapago = binding.input.text.toString()                                 // 입력값 받아옴
            Log.d("IISE", inputToPapago)
            val naverFrag = fragAdapter.fragList[0] as NaverFragment                          // 입력값 강제로 네이버 프래그먼티에 던짐
            naverFrag.updateFragText(inputToPapago)                                           // 네이버 viewpage에 출력 , updateFragText는 네이버 fragment에 입력되어있음

        }

    }


    private fun translate() {                                                                 // 아래는 다 네이버 파파고 api 호출
        val client = OkHttpClient()

        val url = "https://openapi.naver.com/v1/papago/n2mt"
        val requestBody = FormBody.Builder()
            .add("source", "ko")
            .add("target", "en")
            .add("text", binding.input.text.toString())
            .build()

        val request = Request.Builder()
            .addHeader("X-Naver-Client-Id", "")             // 여기는 지우라고 했으니까 지워서 다시 올려요
            .addHeader("X-Naver-Client-Secret", "")                   // 근데 없어서 실행은 안될듯?
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

