package com.example.visionapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.visionapi.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private var uid: Int = -1
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UID와 이름 가져오기
        uid = intent.getIntExtra("uid", -1)
        name = intent.getStringExtra("name") ?: ""

        // Toolbar 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 현재 사용 중인 이름을 툴바에 표시
        supportActionBar?.title = if (name.isNotEmpty()) {
            "현재 사용자: $name"
        } else {
            "사용자 정보를 불러올 수 없습니다"
        }

        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        // TabLayout 및 ViewPager2 연결
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // ViewPager2 어댑터 설정
        viewPager.adapter = ViewPagerAdapter(this)

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "카메라"
                1 -> "알레르기"
                2 -> "DB확인"
                else -> null
            }
        }.attach()
    }

    // 뒤로가기 버튼 동작 정의
    override fun onSupportNavigateUp(): Boolean {
        finish() // 현재 액티비티 종료
        return true
    }
}
