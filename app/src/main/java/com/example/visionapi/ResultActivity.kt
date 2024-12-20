package com.example.visionapi

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ResultActivity로 전달된 UID를 가져오기
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val uid = sharedPreferences.getInt("uid", -1) // 저장된 UID 가져오기


        //Camera Activity에서 가져온 String 가져오기
        val text : String = intent.getStringExtra("text").toString()
        dbHelper = ProductDatabaseHelper(this)

        //문자열 유사도 분석으로 최대한 비슷한 제품 검색
        val name : List<String> = dbHelper.getProductsName()
        val jaroWinkler = JaroWinkler()
        //검색 결과가 serch에 들어갈 예정, 유사한 결과가 없으면 그대로
        var search = ""
        for(n in name){
            //유사도 80%이상일 때 검색
            if(jaroWinkler.similarity(n, text) > 0.8){
                search = n
            }
        }

        //TEST용 CODE
        //search = "꽃게랑"

        //제품 결과 출력
        val p : Product = dbHelper.getProduct(search)
        //제품 유무 검사
        val allist = mutableListOf<String>()
        binding.tvAnalize.text = search + "의 검색결과"
        if(p.name != "1"){
            Log.e("Result", "Product loaded")
            //유저 알러지 정보 조회
            val ua = getUserAllergy(uid)  // UID를 기준으로 알러지 정보 조회
            Log.e("Result", "User Allergy loaded")

            //사용자 알러지 비교
            var flag = false
            var targetLists = p.target.split(",")
            for(a in targetLists) {
                for (u in ua) {
                    if (u != "") {
                        if (a == u) {
                            binding.tvResult.text = "알러지가 검출되었습니다!"
                            allist.add(a)
                            flag = true
                        }
                    }
                }
            }
            if(flag){
                binding.tvAnalize.setBackgroundColor(Color.rgb(251, 177, 162))
            }
        }
        else{
            binding.tvResult.text = "검색된 내용이 없습니다..."
        }

        //자세히 보기 Dialog
        binding.btnDetail.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("자세히 보기")
                .setMessage(getMessage(allist))
                .setNegativeButton("닫기") { dialog, _ ->
                    // 아무것도 하지 않고 닫기
                    dialog.dismiss()
                }
                .setPositiveButton("자세한 정보를 원하시면") { _, _ ->
                    // 사용자가 성분을 선택할 수 있는 Dialog 생성
                    if (allist.isNotEmpty()) {
                        val items = allist.toTypedArray() // 성분 목록 배열로 변환
                        AlertDialog.Builder(this)
                            .setTitle("검색할 성분을 선택하세요")
                            .setItems(items) { _, which ->
                                // 사용자가 선택한 성분 가져오기
                                val selectedIngredient = items[which]
                                val query = Uri.encode(selectedIngredient) // 선택한 성분 인코딩

                                // 브라우저로 이동
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
                        // 검색 가능한 성분이 없을 때 경고 메시지 근데 아마 알레르기는 다 있으니 필요는 없을듯
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

        //닫기 버튼
        binding.btnGoBackCamera.setOnClickListener {
            finish()
        }

    }

    // UID 기준으로 유저 알러지 정보 조회
    private fun getUserAllergy(uid: Int): Array<String> {
        val userDao = UserDatabase.getInstance(this)!!.UserDao()
        val user = userDao.getUser(uid)  // UID로 유저 조회
        return if (user != null) {
            arrayOf(
                user.al1, user.al2, user.al3, user.al4, user.al5, user.al6, user.al7, user.al8, user.al9, user.al10,
                user.al11, user.al12, user.al13, user.al14, user.al15, user.al16, user.al17, user.al18, user.al19, user.al20, user.al21
            )
        } else {
            arrayOf()  // 유저가 없으면 빈 배열 반환
        }
    }

    fun getMessage(list: MutableList<String>) : String {
        var rtn: String = ""
        if(list.size == 0) return "인식된 내용이 없거나 DB에 없습니다...\n"
        for(n in list){
            rtn += n + "이/가 식별되었습니다\n"
        }
        return rtn
    }
}