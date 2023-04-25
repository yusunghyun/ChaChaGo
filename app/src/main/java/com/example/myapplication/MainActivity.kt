package com.example.myapplication

import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class MainActivity : AppCompatActivity() {

    val requestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // 수신 하는 곳. "grade"라는 키워드로 스트링 추출하여 존재 시 토스트 띄움.
        val receivedText: String? = it.data?.getStringExtra("phoneNumber")

        if (receivedText != null) {
            Toast.makeText(
                this,
                "You can’t call to $receivedText !!!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val tBtn: Button = findViewById(R.id.buttonSay)
        // textInput id 가져오기
        val tInput: TextInputEditText = findViewById(R.id.phoneInput)

        tBtn.setOnClickListener {
            // 전화앱을 띄워주는 역할을 합니다. 이 람다 밖으로 빼면 tel에 입력이 안되어있어서 안으로 넣었습니다...뭘까요..ㅠㅜ
            val telUri: Uri = Uri.parse("tel:${tInput.text}")
            // chooser와 함께 위 tel을 띄웁니다. 매니페스트.xml에 미리 action.DIAL과 tel을 넣어놓았습니다.
            val chooserIntent = Intent(Intent.ACTION_DIAL, telUri).run {
                createChooser(this, "Which app to use?")
            }
            requestLauncher.launch(chooserIntent)
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
