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

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val userListView = findViewById<ListView>(R.id.userListView)
        val emptyMessage = findViewById<TextView>(R.id.emptyMessage)

        val userDatabase = UserDatabase.getInstance(this)
        val userDao = userDatabase?.UserDao()
        val userList = userDao?.getAllUsers() ?: emptyList()

        if (userList.isEmpty()) {
            emptyMessage.text = "등록된 유저가 없습니다"
            emptyMessage.visibility = TextView.VISIBLE
            userListView.visibility = ListView.GONE
        } else {
            emptyMessage.visibility = TextView.GONE
            val userDisplayList = userList.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userDisplayList)
            userListView.adapter = adapter

            userListView.setOnItemClickListener { _, _, position, _ ->
                val selectedUser = userList[position]

                // MainActivity로 이동
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra("uid", selectedUser.uid)
                mainIntent.putExtra("name", selectedUser.name)
                startActivity(mainIntent)
                finish()
            }
        }
    }
}
