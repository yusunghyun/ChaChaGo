package com.example.naverapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.naverAPI.*
import com.example.naverapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var fragmentsHandler: FragmentsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()

        // 아웃풋 초기화 (데이터가 없는 상태에서 보이지 않도록 설정)
        binding.out1.visibility = View.GONE
        binding.output.visibility = View.GONE
    }

    private fun setupUI() {
        // UI 초기 설정
        fragmentsHandler = FragmentsHandler(binding, this)

        // 전송 버튼 클릭 이벤트 설정
        binding.sending.setOnClickListener {
            // 사용자 입력 텍스트를 가지고 GPT-3.5-turbo에 요청 보내기
            val userInput = binding.input.text.toString()
            translateText(userInput)
            handleUserInput(userInput)
        }
    }

    private fun handleUserInput(input: String) {
        // 사용자 메세지 생성
        val userMessage = Message("user", input)
        Log.d("IISE", "input : $input")
        val myPost = MyPost("gpt-3.5-turbo", listOf(userMessage))

        // GPT-3.5-turbo에 요청 보내기
        sendRequestToGPT(myPost)

        // 사용자 입력 텍스트 번역
        translateText(input)
    }

    private fun sendRequestToGPT(myPost: MyPost) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = NetworkService.api.createPost(myPost)
            withContext(Dispatchers.Main) {
                request.enqueue(object : Callback<MyResponse> {
                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                        // 응답 처리
                        handleGptResponse(response)
                    }

                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        // 요청 실패 처리
                        showToast("Failure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun handleGptResponse(response: Response<MyResponse>) {
        if (response.isSuccessful) {
            val apiResponse = response.body()
            val gptResponseText = "${apiResponse?.choices?.get(0)?.message?.content}"

            // GPT-3.5-turbo로부터 받은 응답 텍스트를 화면에 출력
            binding.out1.text = gptResponseText
            Log.d("IISE", "gptResponseText : $gptResponseText")
            fragmentsHandler.getGPTFragment().updateFragText(gptResponseText)

            // 아웃풋 보이기
            //binding.out1.visibility = View.VISIBLE
        } else {
            showToast("Error: ${response.errorBody()}")
        }
    }

    private fun translateText(input: String) {
        // 텍스트 번역
        APIManager.translate(input) { translatedText ->
            runOnUiThread {
                // 번역된 텍스트를 화면에 출력
                binding.output.text = translatedText
                Log.d("IISE", "translatedText : $translatedText")
                fragmentsHandler.getNaverFragment().updateFragText(translatedText)

                // 아웃풋 보이기
               // binding.output.visibility = View.VISIBLE
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
