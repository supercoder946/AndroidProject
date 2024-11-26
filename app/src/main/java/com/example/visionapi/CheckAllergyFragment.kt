package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.visionapi.databinding.FragmentCheckAllergyBinding

class CheckAllergyFragment : Fragment() {
    private lateinit var binding: FragmentCheckAllergyBinding
    private var uid: Int = -1 // UID를 저장할 변수

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckAllergyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UID를 MainActivity 또는 상위 Activity에서 전달받기
        uid = activity?.intent?.getIntExtra("uid", -1) ?: -1

        // UID 확인
        if (uid == -1) {
            Toast.makeText(requireContext(), "유효하지 않은 사용자입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // CheckAllergy Activity 호출
        binding.checkAllergyButton.setOnClickListener {
            val intent = Intent(requireContext(), CheckAllergy::class.java)
            intent.putExtra("uid", uid) // UID 전달
            startActivity(intent)
        }

        // 설명 텍스트 설정
        binding.allergyDescription.text = "알레르기 정보를 설정하고 관리합니다."
    }
}
