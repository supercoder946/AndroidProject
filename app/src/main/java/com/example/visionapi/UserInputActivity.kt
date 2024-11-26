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

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val viewListButton = findViewById<Button>(R.id.viewListButton)
        val deleteUserButton = findViewById<Button>(R.id.deleteUserButton)

        val userDatabase = UserDatabase.getInstance(this)
        val userDao = userDatabase?.UserDao()

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()

            if (name.isNotBlank()) {
                val usedUids = userDao?.getAllUsers()?.map { it.uid } ?: emptyList()
                val newUid = if (usedUids.isEmpty()) 1 else usedUids.max()!! + 1

                if (newUid <= 100) {
                    val user = User(
                        uid = newUid,
                        name = name,
                        al1 = "", al2 = "", al3 = "", al4 = "", al5 = "",
                        al6 = "", al7 = "", al8 = "", al9 = "", al10 = "",
                        al11 = "", al12 = "", al13 = "", al14 = "", al15 = "",
                        al16 = "", al17 = "", al18 = "", al19 = "", al20 = "", al21 = ""
                    )

                    userDao?.insertUser(user)
                    Log.d("UserInputActivity", "Saved User: $user")

                    Toast.makeText(this, "사용자가 저장되었습니다! UID: $newUid", Toast.LENGTH_SHORT).show()
                    nameInput.text.clear()
                } else {
                    Toast.makeText(this, "UID 범위를 초과했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show()
            }
        }

        viewListButton.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }

        deleteUserButton.setOnClickListener {
            val intent = Intent(this, DeleteUserActivity::class.java)
            startActivity(intent)
        }
    }
}
