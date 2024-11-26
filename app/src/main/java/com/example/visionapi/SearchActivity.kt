package com.example.visionapi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var adapter: AllergyAdapter

    // 알러지 리스트
    private val allergyList = listOf("우유", "계란", "밀가루", "땅콩", "호두", "갑각류", "대두", "생선", "조개류")
    private val filteredList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // XML 요소 초기화
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val etSearch = findViewById<EditText>(R.id.etSearch)

        // RecyclerView 설정
        adapter = AllergyAdapter(filteredList) { allergy, isChecked ->
            // 체크박스 상태 변경 시 처리
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 초기 데이터 설정
        filteredList.addAll(allergyList)
        adapter.updateList(filteredList)

        // 검색창 텍스트 변경 리스너
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filteredList.clear()
                filteredList.addAll(allergyList.filter { it.contains(query, ignoreCase = true) })
                adapter.updateList(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
