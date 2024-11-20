package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_input)

        // 사용자 입력을 받기 위한 EditText
        val uidInput = findViewById<EditText>(R.id.uidInput)

        val saveButton = findViewById<Button>(R.id.saveButton)

        // 데이터베이스 인스턴스 가져오기
        val userDatabase = UserDatabase.getInstance(this)
        val userDao = userDatabase?.UserDao()

        saveButton.setOnClickListener {
            // 사용자가 입력한 UID 값을 변수에 저장
            val uid = uidInput.text.toString().toIntOrNull()

            // UID가 null이 아닌 경우에만 처리
            if (uid != null) {
                // User 객체 생성 (UID만 저장, 나머지 필드는 기본값으로 처리)
                val user = User(
                    uid = uid,
                    al1 = "",
                    al2 = "",
                    al3 = "",
                    al4 = "",
                    al5 = "",
                    al6 = "",
                    al7 = "",
                    al8 = "",
                    al9 = "",
                    al10 = "",
                    al11 = "",
                    al12 = "",
                    al13 = "",
                    al14 = "",
                    al15 = "",
                    al16 = "",
                    al17 = "",
                    al18 = "",
                    al19 = "",
                    al20 = "",
                    al21 = ""
                )

                // 데이터베이스에 UID 저장
                userDao?.insertUser(user)
                Toast.makeText(this, "UID가 저장되었습니다!", Toast.LENGTH_SHORT).show()

                // 저장 후 입력 필드 초기화
                uidInput.text.clear()
            } else {
                // UID가 유효하지 않은 경우 메시지 표시
                Toast.makeText(this, "UID를 입력하세요!", Toast.LENGTH_SHORT).show()
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
