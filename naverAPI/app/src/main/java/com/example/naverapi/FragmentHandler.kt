package com.example.naverapi
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.naverapi.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentsHandler(private val binding: ActivityMainBinding, activity: AppCompatActivity) {
    private val firstFragment = NaverFragment()
    private val secondFragment = GptFragment.newInstance("", "")
    private val myFrags = listOf(firstFragment, secondFragment)
    private var currentPosition: Int = 0

    init {
        val fragAdapter = FragmentAdapter(activity)
        fragAdapter.fragList = myFrags
        binding.vPage.adapter = fragAdapter

        val tabs = listOf("파파고", "Chat GPT")
        TabLayoutMediator(binding.tabLayout, binding.vPage) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        // 페이지 변경 사항을 추적
        binding.vPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPosition = position
            }
        })
    }

    fun getCurrentFragment(): String {
        Log.d("IISE", "currentPosition : $currentPosition")
        return when (currentPosition) {
            0 -> "PaPaGo"
            1 -> "GPT"
            else -> "Unknown"
        }
    }

    fun getNaverFragment(): NaverFragment {
        return myFrags[0] as NaverFragment
    }

    fun getGPTFragment(): GptFragment {
        return myFrags[1] as GptFragment
    }
}
