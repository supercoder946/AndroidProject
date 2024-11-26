package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionapi.databinding.FragmentDbCheckBinding

class DBCheckFragment : Fragment() {
    private lateinit var binding: FragmentDbCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDbCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 버튼 클릭 시 DBCheck Activity 호출
        binding.btnOpenDbCheck.setOnClickListener {
            val intent = Intent(requireContext(), DBCheck::class.java)
            startActivity(intent)
        }

        // 설명 텍스트 설정
        binding.DBDescription.text = "알레르기 정보를 설정하고 관리합니다."
    }
}
