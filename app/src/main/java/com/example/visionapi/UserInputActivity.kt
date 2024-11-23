package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_input)

        // 사용자 이름을 받기 위한 EditText
        val nameInput = findViewById<EditText>(R.id.nameInput) // name 입력 필드

        val saveButton = findViewById<Button>(R.id.saveButton)

        // 데이터베이스 인스턴스 가져오기
        val userDatabase = UserDatabase.getInstance(this)
        val userDao = userDatabase?.UserDao()

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()

            if (name.isNotBlank()) {
                // UID를 자동으로 생성하는 로직
                val usedUids = userDao?.getAllUsers()?.map { it.uid } ?: emptyList()
                val availableUids = (1..100).filter { it !in usedUids }

                if (availableUids.isNotEmpty()) {
                    val newUid = availableUids.random() // 사용 가능한 UID 중 랜덤 선택

                    // 생성된 UID와 이름을 포함한 사용자 객체 생성
                    val user = User(
                        uid = newUid,
                        name = name,
                        al1 = "", al2 = "", al3 = "", al4 = "", al5 = "",
                        al6 = "", al7 = "", al8 = "", al9 = "", al10 = "",
                        al11 = "", al12 = "", al13 = "", al14 = "", al15 = "",
                        al16 = "", al17 = "", al18 = "", al19 = "", al20 = "", al21 = ""
                    )

                    // 사용자 객체를 데이터베이스에 저장
                    userDao?.insertUser(user)
                    Log.d("UserInputActivity", "Saved User: $user") // 저장 로그

                    // 저장된 데이터 확인
                    val allUsers = userDao?.getAllUsers()
                    Log.d("UserInputActivity", "All Users: $allUsers")

                    // 저장 완료 메시지
                    Toast.makeText(this, "정보가 저장되었습니다! UID: $newUid", Toast.LENGTH_SHORT).show()
                    nameInput.text.clear()
                } else {
                    Toast.makeText(this, "UID 범위가 모두 사용 중입니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Name을 올바르게 입력하세요!", Toast.LENGTH_SHORT).show()
            }
        }

        // UID 목록 보기 버튼
        val viewListButton = findViewById<Button>(R.id.viewListButton)
        viewListButton.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
    }
}
