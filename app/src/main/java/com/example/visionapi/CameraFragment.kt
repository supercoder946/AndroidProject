package com.example.visionapi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.visionapi.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = activity?.intent?.getIntExtra("uid", -1) ?: -1

        // CameraActivity 호출
        binding.btnPhoto.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            intent.putExtra("uid", uid)
            startActivity(intent)
        }

        // 설명 텍스트 설정
        binding.cameraDescription.text = "카메라를 열어 이미지 스캔을 시작합니다."
    }
}
