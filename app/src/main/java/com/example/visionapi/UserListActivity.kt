package com.example.visionapi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.visionapi.CheckAllergy
import com.example.visionapi.R
import com.example.visionapi.UserDatabase

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val userListView = findViewById<ListView>(R.id.userListView)
        val emptyMessage = findViewById<TextView>(R.id.emptyMessage)

        // 데이터베이스에서 사용자 목록 가져오기
        val userDatabase = UserDatabase.getInstance(this)
        val userDao = userDatabase?.UserDao()
        val userList = userDao?.getAllUsers() ?: emptyList()
        Log.d("UserListActivity", "Fetched Users: $userList") // 조회 결과 확인

        if (userList.isEmpty()) {
            emptyMessage.text = "등록된 유저가 없습니다"
            emptyMessage.visibility = TextView.VISIBLE
            userListView.visibility = ListView.GONE
        } else {
            emptyMessage.visibility = TextView.GONE
            // 이름만 표시
            val userDisplayList = userList.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userDisplayList)
            userListView.adapter = adapter

            userListView.setOnItemClickListener { _, _, position, _ ->
                val selectedUser = userList[position]

                // SharedPreferences에 UID와 이름 저장
                val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("uid", selectedUser.uid)
                editor.putString("name", selectedUser.name)
                editor.apply()

                // CheckAllergy로 UID와 이름 전달
                val checkAllergyIntent = Intent(this, CheckAllergy::class.java)
                checkAllergyIntent.putExtra("uid", selectedUser.uid)
                checkAllergyIntent.putExtra("name", selectedUser.name)
                startActivity(checkAllergyIntent)
            }

            userListView.setOnItemLongClickListener { _, _, position, _ ->
                val selectedUser = userList[position]
                AlertDialog.Builder(this)
                    .setTitle("사용자 삭제")
                    .setMessage("정말로 이 사용자를 삭제하시겠습니까?\nUID: ${selectedUser.uid}, Name: ${selectedUser.name}")
                    .setPositiveButton("삭제") { _, _ ->
                        userDao?.deleteUserByUid(selectedUser.uid)
                        val updatedUserList = userDao?.getAllUsers() ?: emptyList()

                        if (updatedUserList.isEmpty()) {
                            emptyMessage.text = "등록된 유저가 없습니다"
                            emptyMessage.visibility = TextView.VISIBLE
                            userListView.visibility = ListView.GONE
                        } else {
                            emptyMessage.visibility = TextView.GONE
                            val updatedDisplayList = updatedUserList.map { it.name }
                            val updatedAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, updatedDisplayList)
                            userListView.adapter = updatedAdapter
                            userListView.visibility = ListView.VISIBLE
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()

                true
            }
        }
    }
}

