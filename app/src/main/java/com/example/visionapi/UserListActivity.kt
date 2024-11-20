package com.example.visionapi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val userListView = findViewById<ListView>(R.id.userListView)
        val emptyMessage = findViewById<TextView>(R.id.emptyMessage)

        // 데이터베이스에서 UID 목록 가져오기
        val userDatabase = UserDatabase.getInstance(this)
        val userDao = userDatabase?.UserDao()
        val uidList = userDao?.getUsers()?.map { it.toString() } ?: emptyList()

        if (uidList.isEmpty()) {
            // UID 데이터가 없을 경우 메시지 표시
            emptyMessage.text = "등록된 유저가 없습니다"
            emptyMessage.visibility = TextView.VISIBLE  // 메시지 보이게 설정
            userListView.visibility = ListView.GONE    // ListView 숨기기
        } else {
            emptyMessage.visibility = TextView.GONE

            // ListView에 UID 목록 표시
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, uidList)
            userListView.adapter = adapter

            // 항목 클릭 이벤트 처리
            // UID 클릭 시, 선택된 UID를 CheckAllergy와 ResultActivity로 전달
            userListView.setOnItemClickListener { _, _, position, _ ->
                val uid = uidList[position].toInt()

                // ResultActivity로 UID 전달
                val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("uid", uid) // UID 저장
                editor.apply()

                // CheckAllergy로 UID 전달
                val checkAllergyIntent = Intent(this, CheckAllergy::class.java)
                checkAllergyIntent.putExtra("uid", uid)  // 클릭한 UID 전달
                startActivity(checkAllergyIntent)


            }

            // 항목 길게 클릭 시 UID 삭제
            userListView.setOnItemLongClickListener { _, _, position, _ ->
                val uid = uidList[position].toInt()

                // 삭제 확인 다이얼로그 표시
                AlertDialog.Builder(this)
                    .setTitle("UID 삭제")
                    .setMessage("정말로 이 UID를 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ ->
                        // UID 삭제
                        userDao?.deleteUserByUid(uid)

                        // 삭제 후 ListView 갱신
                        val updatedUidList = userDao?.getUsers()?.map { it.toString() } ?: emptyList()

                        if (updatedUidList.isEmpty()) {
                            // UID 데이터가 없을 경우 메시지 표시
                            emptyMessage.text = "등록된 유저가 없습니다"  // 메시지 텍스트 설정
                            emptyMessage.visibility = TextView.VISIBLE  // 메시지 보이게 설정
                            userListView.visibility = ListView.GONE    // ListView 숨기기
                        } else {
                            emptyMessage.visibility = TextView.GONE    // 메시지 숨기기
                            val updatedAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, updatedUidList)
                            userListView.adapter = updatedAdapter    // ListView에 새 목록 설정
                            userListView.visibility = ListView.VISIBLE  // ListView 보이게 설정
                        }
                    }
                    .setNegativeButton("취소", null)
                    .show()

                true
            }
        }
    }
}
