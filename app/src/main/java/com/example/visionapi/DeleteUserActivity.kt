package com.example.visionapi

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DeleteUserActivity : AppCompatActivity() {

    private lateinit var userListView: ListView
    private lateinit var emptyMessage: TextView
    private lateinit var userDatabase: UserDatabase
    private lateinit var userDao: UserDao
    private lateinit var userList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        userListView = findViewById(R.id.userListView)
        emptyMessage = findViewById(R.id.emptyMessage)

        userDatabase = UserDatabase.getInstance(this)!!
        userDao = userDatabase.UserDao()
        userList = userDao.getAllUsers().toMutableList()

        refreshUserList()

        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = userList[position]
            showDeleteConfirmationDialog(selectedUser)
        }
    }

    private fun refreshUserList() {
        userList = userDao.getAllUsers().toMutableList()
        if (userList.isEmpty()) {
            emptyMessage.text = "등록된 유저가 없습니다"
            emptyMessage.visibility = TextView.VISIBLE
            userListView.visibility = ListView.GONE
        } else {
            emptyMessage.visibility = TextView.GONE
            userListView.visibility = ListView.VISIBLE
            val userDisplayList = userList.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userDisplayList)
            userListView.adapter = adapter
        }
    }

    private fun showDeleteConfirmationDialog(user: User) {
        AlertDialog.Builder(this)
            .setTitle("사용자 삭제")
            .setMessage("정말로 ${user.name} 사용자를 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                userDao.deleteUserByUid(user.uid)
                Toast.makeText(this, "${user.name} 사용자가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                refreshUserList()
            }
            .setNegativeButton("취소", null)
            .show()
    }
}
