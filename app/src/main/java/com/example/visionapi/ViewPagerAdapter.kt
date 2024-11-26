package com.example.visionapi.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.visionapi.CameraFragment
import com.example.visionapi.CheckAllergyFragment
import com.example.visionapi.DBCheckFragment


class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CameraFragment() // 첫 번째 탭
            1 -> CheckAllergyFragment() // 두 번째 탭
            2 -> DBCheckFragment()

            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
