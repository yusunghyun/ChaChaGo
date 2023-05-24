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

        binding.out1.visibility = View.GONE
        binding.output.visibility = View.GONE

    }

    private fun setupUI() {
        fragmentsHandler = FragmentsHandler(binding, this)

        binding.sending.setOnClickListener { // 버튼 클릭시 이벤트 발생
            val inputToPapago = binding.input.text.toString()
            val userMessage = Message("user", inputToPapago)
            val myPost = MyPost("gpt-3.5-turbo", listOf(userMessage))
            sendRequest(myPost)

            translateText(inputToPapago)
        }
    }

    private fun sendRequest(myPost: MyPost) {
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
                            val gptText = "Response: ${apiResponse?.choices?.get(0)?.message?.content}"
                            Log.d("IISE", gptText)
                            Log.d("IISE", apiResponse?.choices.toString())
                            binding.out1.text = gptText
                            fragmentsHandler.getGPTFragment().updateFragText(gptText)
                        } else {
                            showToast("Error: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        showToast("Failure: ${t.message}")
                    }
                })
            }
        }
    }

    private fun translateText(input: String) {
        APIManager.translate(input) { translatedText ->
            runOnUiThread {
                binding.output.text = translatedText
                fragmentsHandler.getNaverFragment().updateFragText(translatedText) }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
