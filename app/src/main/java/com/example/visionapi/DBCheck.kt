package com.example.visionapi

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DBCheck : AppCompatActivity() {
    private lateinit var dbHelper: ProductDatabaseHelper
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbcheck)

        dbHelper = ProductDatabaseHelper(this)
        textView = findViewById(R.id.textView)

        // 데이터 삽입
        insertSampleProducts()

        // 데이터 삭제 및 화면에 출력
        //deleteSampleProduct()
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
            total = ".."
        )

    }

    private fun deleteSampleProduct() {
        dbHelper.deleteProduct("호두마루")
        dbHelper.deleteProduct("꽃게랑")
        dbHelper.deleteProduct("계란과자")
        dbHelper.deleteProduct("버터링")
        dbHelper.deleteProduct("크런키")

    }

    private fun displayProducts() {
        val products = dbHelper.getAllProducts()
        val productListString = StringBuilder()

        products.forEach { product ->
            productListString.append("Name: ${product.name}\n Kcal: ${product.kcal}\nTarget: ${product.target}\nType: ${product.type}\nTotal: ${product.total}\n\n")
        }

        textView.text = productListString.toString()

    }
}