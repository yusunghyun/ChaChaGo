package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // 텍스트와 버튼 id 불러오기
        val colorText: TextView = findViewById(R.id.colorText)
        val colorButton: Button = findViewById(R.id.setColorButton)

        // 버튼 동작 로직
        colorButton.setOnClickListener {
            // 컬러와 텍스트를 동일시 하기위해 r,g,b 선언.
            val r: Int = (0..255).random()
            val g: Int = (0..255).random()
            val b: Int = (0..255).random()

            // set컬러. setTextColor에 인풋타입이 Color라서 Color임포트
            colorText.setTextColor(Color.rgb(r, g, b))
            // set텍스트
            colorText.text = "COLOR: ${r}r ${g}g ${b}b"
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("IISE", "onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.d("IISE", "onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.d("IISE", "onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.d("IISE", "onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("IISE", "onDestroy")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d("IISE", "onRestart")
    }
}
