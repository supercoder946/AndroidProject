package com.example.visionapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.visionapi.databinding.ActivityDbcheckBinding

class DBCheck : AppCompatActivity() {
    private lateinit var dbHelper: ProductDatabaseHelper
    private lateinit var binding: ActivityDbcheckBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDbcheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화

        dbHelper = ProductDatabaseHelper(this)

        // 데이터 삽입 (샘플 데이터)
        insertSampleProducts()

        // 데이터 표시 (정적 레이아웃에 데이터 바인딩)
        displayProducts()
    }

    private fun insertSampleProducts() {
        dbHelper.insertProduct(
            name = "호두마루",
            kcal = "825Kcal",
            target = listOf("우유", "호두"),
            type = "아이스크림",
            total = "660mL"
        )
        dbHelper.insertProduct(
            name = "꽃게랑",
            kcal = "385kcal",
            target = listOf("밀", "대두", "토마토", "게", "새우", "닭고기", "소고기", "우유"),
            type = "과자",
            total = "70g"
        )
        dbHelper.insertProduct(
            name = "계란과자",
            kcal = "205Kcal",
            target = listOf("계란", "밀", "우유", "대두"),
            type = "과자",
            total = "45g"
        )
        dbHelper.insertProduct(
            name = "버터링",
            kcal = "470Kcal",
            target = listOf("밀", "계란", "대두", "우유"),
            type = "과자",
            total = "86g"
        )
        dbHelper.insertProduct(
            name = "크런키",
            kcal = "185Kcal",
            target = listOf("우유", "밀", "대두"),
            type = "과자",
            total = "34g"
        )
        dbHelper.insertProduct(
            name = "타이레놀",
            kcal = "..",
            target = listOf("아세트아미노펜"),
            type = "진통제",
            total = ".."
        )
        dbHelper.insertProduct(
            name = "이부프로펜",
            kcal = "..",
            target = listOf("나프록센"),
            type = "소염제",
            total = "..")
    }

    private fun displayProducts() {
        val products = dbHelper.getAllProducts()

        // 첫 번째 카드에 데이터를 설정합니다.
        if (products.size > 0) {
            val firstProduct = products[0]
            binding.cardText1.text = "Name: ${firstProduct.name}\n" +
                    "Kcal: ${firstProduct.kcal}\n" +
                    "Target: ${firstProduct.target}\n" +
                    "Type: ${firstProduct.type}\n" +
                    "Total: ${firstProduct.total}"
        }

        // 두 번째 카드에 데이터를 설정합니다.
        if (products.size > 1) {
            val secondProduct = products[1]
            binding.cardText2.text = "Name: ${secondProduct.name}\n" +
                    "Kcal: ${secondProduct.kcal}\n" +
                    "Target: ${secondProduct.target}\n" +
                    "Type: ${secondProduct.type}\n" +
                    "Total: ${secondProduct.total}"
        }

        // 세 번째 카드에 데이터를 설정합니다.
        if (products.size > 2) {
            val thirdProduct = products[2]
            binding.cardText3.text = "Name: ${thirdProduct.name}\n" +
                    "Kcal: ${thirdProduct.kcal}\n" +
                    "Target: ${thirdProduct.target}\n" +
                    "Type: ${thirdProduct.type}\n" +
                    "Total: ${thirdProduct.total}"
        }
        if (products.size > 3) {
            val thirdProduct = products[3]
            binding.cardText4.text = "Name: ${thirdProduct.name}\n" +
                    "Kcal: ${thirdProduct.kcal}\n" +
                    "Target: ${thirdProduct.target}\n" +
                    "Type: ${thirdProduct.type}\n" +
                    "Total: ${thirdProduct.total}"
        }
        if (products.size > 4) {
            val thirdProduct = products[4]
            binding.cardText5.text = "Name: ${thirdProduct.name}\n" +
                    "Kcal: ${thirdProduct.kcal}\n" +
                    "Target: ${thirdProduct.target}\n" +
                    "Type: ${thirdProduct.type}\n" +
                    "Total: ${thirdProduct.total}"
        }
        if (products.size > 5) {
            val thirdProduct = products[5]
            binding.cardText6.text = "Name: ${thirdProduct.name}\n" +
                    "Kcal: ${thirdProduct.kcal}\n" +
                    "Target: ${thirdProduct.target}\n" +
                    "Type: ${thirdProduct.type}\n" +
                    "Total: ${thirdProduct.total}"
        }
        if (products.size > 6) {
            val thirdProduct = products[6]
            binding.cardText7.text = "Name: ${thirdProduct.name}\n" +
                    "Kcal: ${thirdProduct.kcal}\n" +
                    "Target: ${thirdProduct.target}\n" +
                    "Type: ${thirdProduct.type}\n" +
                    "Total: ${thirdProduct.total}"
        }
        if (products.size > 7) {
            val thirdProduct = products[7]
            binding.cardText3.text = "Name: ${thirdProduct.name}\n" +
                    "Kcal: ${thirdProduct.kcal}\n" +
                    "Target: ${thirdProduct.target}\n" +
                    "Type: ${thirdProduct.type}\n" +
                    "Total: ${thirdProduct.total}"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // 뒤로가기 버튼 누를 시 종료
        return true
    }
}
