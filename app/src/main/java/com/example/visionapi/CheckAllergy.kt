package com.example.visionapi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.visionapi.databinding.ActivityCheckAllergyBinding

class CheckAllergy : AppCompatActivity() {
    private lateinit var binding: ActivityCheckAllergyBinding
    private val allergyList = listOf(
        "대두", "새우", "계란", "소고기", "돼지고기", "닭고기", "게", "오징어",
        "고등어", "조개", "우유", "땅콩", "호두", "잣", "복숭아", "토마토",
        "밀", "메밀", "아황산류", "아세트아미노펜", "나프록센"
    )
    private var uid: Int =-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UID 전달 확인
        uid = intent.getIntExtra("uid", -1)
        Log.d("CheckAllergy", "Received UID: $uid") // UID 디버깅
        if (uid == -1) {
            Toast.makeText(this, "유효하지 않은 사용자입니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Toolbar 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 데이터베이스 초기화
        val userDB = UserDatabase.getInstance(applicationContext)
        val user = userDB?.UserDao()?.getUser(uid)
        Log.d("CheckAllergy", "Fetched User: $user") // 데이터 조회 디버깅
        if (user == null) {
            Toast.makeText(this, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 알레르기 정보 로드
        setAllergyBox(user)

        // 저장 버튼 클릭 이벤트
        binding.BtnSaveInfo.setOnClickListener {
            val updatedUser = checkAllergyBox(user)
            userDB.UserDao().updateUser(updatedUser)
            Toast.makeText(this, "알러지가 수정되었습니다!", Toast.LENGTH_SHORT).show()
        }

        // 검색창 텍스트 변경 리스너
        binding.searchAllergy.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCheckBoxes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun checkAllergyBox(user: User): User {
        val alArray = Array(21) { "" }
        val checkBoxes = binding.checkBoxContainer.childCount

        for (i in 0 until checkBoxes) {
            val checkBox = binding.checkBoxContainer.getChildAt(i) as? CheckBox
            if (checkBox?.isChecked == true) {
                alArray[i] = checkBox.text.toString()
            }
        }

        return user.copy(
            al1 = alArray[0], al2 = alArray[1], al3 = alArray[2], al4 = alArray[3],
            al5 = alArray[4], al6 = alArray[5], al7 = alArray[6], al8 = alArray[7],
            al9 = alArray[8], al10 = alArray[9], al11 = alArray[10], al12 = alArray[11],
            al13 = alArray[12], al14 = alArray[13], al15 = alArray[14], al16 = alArray[15],
            al17 = alArray[16], al18 = alArray[17], al19 = alArray[18], al20 = alArray[19],
            al21 = alArray[20]
        )
    }

    private fun setAllergyBox(user: User) {
        val alArray = listOf(
            user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7,
            user.al8, user.al9, user.al10, user.al11, user.al12, user.al13, user.al14,
            user.al15, user.al16, user.al17, user.al18, user.al19, user.al20, user.al21
        )

        val checkBoxes = binding.checkBoxContainer.childCount
        for (i in 0 until checkBoxes) {
            val checkBox = binding.checkBoxContainer.getChildAt(i) as? CheckBox
            if (checkBox != null && i < alArray.size) {
                checkBox.text = allergyList[i]
                checkBox.isChecked = alArray[i].isNotEmpty()
            }
        }
    }

    private fun filterCheckBoxes(query: String) {
        val checkBoxes = binding.checkBoxContainer.childCount
        for (i in 0 until checkBoxes) {
            val checkBox = binding.checkBoxContainer.getChildAt(i) as? CheckBox
            if (checkBox != null) {
                checkBox.visibility = if (checkBox.text.contains(query, true)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}
