package com.example.naverapi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.naverapi.databinding.FragmentNaverBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2" // 위이 여러 부분들은 수업들으셔서 아시겠지만 CLASS생성시 자동으로 만들어졌어요

class NaverFragment : Fragment() {

    private var param1: String? = null // 저희는 이제 강제로 메인에서 프래그먼트 요소를 변경할거기때문에
    private var param2: String? = null // 사실 이런 PARAM 변수들은 필요는 없어요

    lateinit var binding: FragmentNaverBinding

    fun updateFragText(text: String) {
        if (this::binding.isInitialized) {
            binding.fragtext.text = text // 네이버 viewpage에 있는 text변경
            Log.d("IISE", "네이버 : $text")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNaverBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 복사하기 버튼 클릭 시 실행되는 코드
        binding.copyNaver.setOnClickListener {
            val selectedText = binding.fragtext.text

            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", selectedText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "텍스트가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = NaverFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
                Log.d("IISE", "param1 + param2 : ${param1 + param2}")
            }
        }
    }
}
