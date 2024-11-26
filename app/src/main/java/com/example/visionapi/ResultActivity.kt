package com.example.visionapi

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.visionapi.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityResultBinding
    lateinit var dbHelper: ProductDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Camera Activity에서 가져온 String 가져오기
        val text: String = intent.getStringExtra("text").toString()
        dbHelper = ProductDatabaseHelper(this)

        // 문자열 유사도 분석으로 최대한 비슷한 제품 검색
        val name: List<String> = dbHelper.getProductsName()
        val jaroWinkler = JaroWinkler()
        var search = ""
        for (n in name) {
            if (jaroWinkler.similarity(n, text) > 0.8) {
                search = n
            }
        }

        // 제품 결과 출력
        val p: Product = dbHelper.getProduct(search)
        val allist = mutableListOf<String>()
        binding.tvAnalize.text = "$search 의 검색결과"

        if (p.name != "1") {
            Log.e("Result", "Product loaded")
            val ua = getUserAllergy()
            Log.e("Result", "User Allergy loaded")

            var flag = false
            val targetLists = p.target.split(",")
            for (a in targetLists) {
                for (u in ua) {
                    if (u.isNotEmpty() && a == u) {
                        binding.tvResult.text = "알러지가 검출되었습니다!"
                        allist.add(a)
                        flag = true
                    }
                }
            }

            if (flag) {
                binding.warningImage.visibility = View.VISIBLE // 알러지 검출 시 경고 이미지 표시
                binding.tvAnalize.setBackgroundColor(Color.rgb(251, 177, 162))
            } else {
                binding.warningImage.visibility = View.GONE // 알러지 미검출 시 경고 이미지 숨김
            }
        } else {
            binding.tvResult.text = "검색된 내용이 없습니다..."
            binding.warningImage.visibility = View.GONE // 검색된 제품이 없을 시 경고 이미지 숨김
        }

        // 자세히 보기 Dialog
        binding.btnDetail.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("자세히 보기")
                .setMessage(getMessage(allist))
                .setNegativeButton("닫기") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("자세한 정보를 원하시면") { _, _ ->
                    if (allist.isNotEmpty()) {
                        val items = allist.toTypedArray()
                        AlertDialog.Builder(this)
                            .setTitle("검색할 성분을 선택하세요")
                            .setItems(items) { _, which ->
                                val selectedIngredient = items[which]
                                val query = Uri.encode(selectedIngredient)

                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("https://www.amc.seoul.kr/asan/search/search.do?kwd=$query")
                                startActivity(intent)
                            }
                            .setNegativeButton("닫기") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("알림")
                            .setMessage("검색 가능한 성분이 없습니다.")
                            .setNegativeButton("닫기") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
                .create()
                .show()
        }
    }

    // 유저 알러지 정보 조회
    private fun getUserAllergy(): Array<String> {
        val user = UserDatabase.getInstance(this)!!.UserDao().getUser(1)
        return arrayOf(
            user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7, user.al8, user.al9, user.al10,
            user.al11, user.al12, user.al13, user.al14, user.al15, user.al16, user.al17, user.al18, user.al19, user.al20, user.al21
        )
    }

    // 상세 메시지 생성
    private fun getMessage(list: MutableList<String>): String {
        var rtn: String = ""
        if (list.isEmpty()) return "인식된 내용이 없거나 DB에 없습니다...\n"
        for (n in list) {
            rtn += "$n 이/가 식별되었습니다\n"
        }
        return rtn
    }
}
