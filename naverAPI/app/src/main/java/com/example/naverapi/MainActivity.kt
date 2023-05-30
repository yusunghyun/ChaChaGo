package com.example.naverapi

import android.os.Bundle
import android.util.Log
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
    }

    private fun setupUI() {
        // UI 초기 설정
        fragmentsHandler = FragmentsHandler(binding, this)

        // 전송 버튼 클릭 이벤트 설정
        binding.sending.setOnClickListener {
            // 사용자 입력 텍스트를 가지고 GPT-3.5-turbo에 요청 보내기
            val userInput = binding.input.text.toString()
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

        // PaPago 에 요청 보내기
        sendRequestToPaPaGo(input)
    }

    // 로딩
    private val loadingDialog by lazy { LoadingDialog(this) }

    // GPT API 호출 로직
    private fun sendRequestToGPT(myPost: MyPost) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = NetworkService.api.createPost(myPost)
            withContext(Dispatchers.Main) {
                loadingDialog.show()

                request.enqueue(object : Callback<MyResponse> {
                    override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                        // 응답 처리
                        loadingDialog.dismiss()
                        setGPTUI(response)
                    }

                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        // 요청 실패 처리
                        loadingDialog.dismiss()
                        showToast("Failure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun setGPTUI(response: Response<MyResponse>) {
        if (response.isSuccessful) {
            val apiResponse = response.body()
            val gptResponseText = "${apiResponse?.choices?.get(0)?.message?.content}"

            // GPT-3.5-turbo로부터 받은 응답 텍스트를 화면에 출력
            Log.d("IISE", "gptResponseText : $gptResponseText")
            fragmentsHandler.getGPTFragment().updateFragText(gptResponseText)
        } else {
            showToast("Error: ${response.errorBody()}")
        }
    }

    // PaPaGo API
        private fun sendRequestToPaPaGo(input: String) {
        APIManager.detect(input) { langCode ->
            if (langCode == "ko") {
                APIManager.translateKoreaToEng(input) { translatedText ->
                    runOnUiThread{fragmentsHandler.getNaverFragment().updateFragText(translatedText)
                    Log.d("iise", "성공")
                }}
            } else if (langCode == "en") {
                APIManager.translateEngToKorea(input) { translatedTexts ->
                    runOnUiThread{fragmentsHandler.getNaverFragment().updateFragText(translatedTexts)
                }}
            } else {
                // 다른 언어인 경우 처리
                Log.d("iise", "실패")
            }
        }
    }

    // 토스트
    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
