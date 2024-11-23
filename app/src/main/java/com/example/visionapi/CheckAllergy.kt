package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visionapi.databinding.ActivityCheckAllergyBinding

class CheckAllergy : AppCompatActivity() {
    lateinit var binding: ActivityCheckAllergyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckAllergyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 유저 UID와 이름 받기
        val uid = intent.getIntExtra("uid", -1)
        val name = intent.getStringExtra("name") ?: "알 수 없는 사용자"

        // 데이터베이스 인스턴스와 UserDao 가져오기
        val userDB = UserDatabase.getInstance(applicationContext)
        val userDao = userDB?.UserDao()

        if (userDao == null) {
            Toast.makeText(this, "UserDao를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (uid != -1) {
            // UID에 맞는 유저 정보 가져오기
            val user = userDao.getUser(uid)
            if (user != null) {
                setAllergyBox(user) // 기존의 알레르기 정보 불러오기
            } else {
                Toast.makeText(this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 저장 버튼 클릭 시
        binding.BtnSaveInfo.setOnClickListener {
            if (uid != -1) {
                val updatedUser = checkAllergyBox(uid) // UID를 포함한 수정된 유저 정보 가져오기
                userDao.updateUser(updatedUser) // 수정된 정보 저장
                Toast.makeText(this, "${name}님의 알러지가 수정되었습니다!", Toast.LENGTH_SHORT).show()
            }
        }

        // 사진 찍기 버튼
        binding.btnChangePhoto.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼
        binding.BtnGoBackMain.setOnClickListener {
            finish()
        }
    }

    private fun checkAllergyBox(uid: Int): User {
        val cbArray = arrayOf(
            binding.cb1, binding.cb2, binding.cb3, binding.cb4, binding.cb5,
            binding.cb6, binding.cb7, binding.cb8, binding.cb9, binding.cb10,
            binding.cb11, binding.cb12, binding.cb13, binding.cb14, binding.cb15,
            binding.cb16, binding.cb17, binding.cb18, binding.cb19, binding.cb20, binding.cb21
        )

        val alArray = Array(21) { "" }

        // 선택된 알레르기 체크박스 값 설정
        cbArray.forEachIndexed { index, cb ->
            if (cb.isChecked) {
                alArray[index] = cb.text.toString()
            }
        }

        // 기존 유저 데이터 가져오기
        val userDao = UserDatabase.getInstance(applicationContext)?.UserDao()
        val existingUser = userDao?.getUser(uid)

        return User(
            uid = uid,
            name = existingUser?.name ?: "", // 기존 이름 보존
            al1 = alArray[0], al2 = alArray[1], al3 = alArray[2], al4 = alArray[3],
            al5 = alArray[4], al6 = alArray[5], al7 = alArray[6], al8 = alArray[7],
            al9 = alArray[8], al10 = alArray[9], al11 = alArray[10], al12 = alArray[11],
            al13 = alArray[12], al14 = alArray[13], al15 = alArray[14], al16 = alArray[15],
            al17 = alArray[16], al18 = alArray[17], al19 = alArray[18], al20 = alArray[19],
            al21 = alArray[20]
        )
    }

    private fun setAllergyBox(user: User) {
        val cbArray = arrayOf(
            binding.cb1, binding.cb2, binding.cb3, binding.cb4, binding.cb5,
            binding.cb6, binding.cb7, binding.cb8, binding.cb9, binding.cb10,
            binding.cb11, binding.cb12, binding.cb13, binding.cb14, binding.cb15,
            binding.cb16, binding.cb17, binding.cb18, binding.cb19, binding.cb20, binding.cb21
        )

        val alArray = arrayOf(
            user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7, user.al8, user.al9, user.al10,
            user.al11, user.al12, user.al13, user.al14, user.al15, user.al16, user.al17, user.al18, user.al19, user.al20, user.al21
        )

        alArray.forEachIndexed { index, allergy ->
            if (allergy.isNotEmpty()) {
                cbArray[index].isChecked = true
            }
        }
    }
}
