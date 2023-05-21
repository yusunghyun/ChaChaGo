package com.example.naverapi
import androidx.appcompat.app.AppCompatActivity
import com.example.naverapi.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentsHandler(private val binding: ActivityMainBinding, activity: AppCompatActivity) {
    private val firstFragment = NaverFragment()
    private val secondFragment = GptFragment.newInstance("", "")
    private val myFrags = listOf(firstFragment, secondFragment)

    init {
        val fragAdapter = FragmentAdapter(activity)
        fragAdapter.fragList = myFrags
        binding.vPage.adapter = fragAdapter

        val tabs = listOf("파파고", "Chat GPT")
        TabLayoutMediator(binding.tabLayout, binding.vPage) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    fun getNaverFragment(): NaverFragment {
        return myFrags[0] as NaverFragment
    }

    fun getGPTFragment(): GptFragment {
        return myFrags[1] as GptFragment
    }
}
