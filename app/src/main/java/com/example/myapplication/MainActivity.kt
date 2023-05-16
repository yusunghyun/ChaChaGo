package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                                Log.d("IISE", "$apiResponse")
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
    }
}
