package com.example.naverapi

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {

            // 스플래시 화면이 보여진 후에 실행될 작업들을 여기에 추가
            // 예: 메인 액티비티를 시작하거나 다른 화면으로 이동하는 등의 작업
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            Log.d("iise","succ")
            finish() // 스플래시 액티비티를 종료하여 뒤로 가기 버튼을 눌렀을 때 스플래시 화면이 다시 표시되지 않도록 함
        }, SPLASH_SCREEN_TIMEOUT)
    }

    companion object {
        private const val SPLASH_SCREEN_TIMEOUT: Long = 3000 // 4초
    }
}
