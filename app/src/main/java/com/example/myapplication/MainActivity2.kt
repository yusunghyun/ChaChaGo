package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // null 방어, 해당 UI는 안보여줘도 되고 여기서 데이터를 반환해줘야해서 진입 시 바로 로직 돌도록 플랫하게 작성했습니다.
        if (intent?.action == Intent.ACTION_DIAL && intent.data != null) {
            val phoneNumber = intent.data?.schemeSpecificPart
            setResult(RESULT_OK, intent)
            intent.putExtra("phoneNumber", "$phoneNumber")
            finish()
        }
    }
}
