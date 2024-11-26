package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainRedirectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UID와 Name 데이터를 전달받음
        val uid = intent.getIntExtra("uid", -1)
        val name = intent.getStringExtra("name") ?: ""

        if (uid != -1) {
            Log.d("MainRedirectActivity", "UID: $uid, Name: $name")
        } else {
            Log.d("MainRedirectActivity", "No UID or Name passed")
        }

        // 기존 MainActivity로 데이터를 전달
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("uid", uid)
        mainIntent.putExtra("name", name)
        startActivity(mainIntent)

        // 현재 Activity 종료
        finish()
    }
}
