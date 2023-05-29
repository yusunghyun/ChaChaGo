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
import com.example.naverAPI.APIManager
import com.example.naverapi.databinding.FragmentGptBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
// gpt 프래그먼트만 생성해나서 여기에는 암것도없습니다
class GptFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentGptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun updateFragText(text: String) {
        if (this::binding.isInitialized) {
            binding.fragtext2.text = text // gpt viewpage에 있는 text변경
            Log.d("IISE", "gpt : $text")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGptBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 복사하기 버튼 클릭 시 실행되는 코드
        binding.copyGPT.setOnClickListener {
            val selectedText = binding.fragtext2.text

            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", selectedText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "텍스트가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.transGPT.setOnClickListener {
            translateGPT(binding.fragtext2.text.toString())
        }
    }

    fun translateGPT(text: String) {
        APIManager.detect(text) { langCode ->
            if (langCode == "ko") {
                APIManager.translate(text) { translatedText ->
                    updateFragText(translatedText)
                    Log.d("iise", "성공")
                }
            } else if (langCode == "en") {
                APIManager.translateEngtoKorea(text) { translatedTexts ->
                    updateFragText(translatedTexts)

                }
            } else {
                // 다른 언어인 경우 처리
                Log.d("iise", "실패")
            }

        }
    }

    companion object {
        @JvmStatic fun newInstance(param1: String, param2: String) =
            GptFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
