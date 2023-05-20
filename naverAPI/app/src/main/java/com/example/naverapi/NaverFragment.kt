package com.example.naverapi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.naverapi.databinding.FragmentNaverBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2" // 위이 여러 부분들은 수업들으셔서 아시겠지만 CLASS생성시 자동으로 만들어졌어요

/**
 * A simple [Fragment] subclass.
 * Use the [NaverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class NaverFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null // 저희는 이제 강제로 메인에서 프래그먼트 요소를 변경할거기때문에
    private var param2: String? = null // 사실 이런 PARAM 변수들은 필요는 없어요

    lateinit var binding: FragmentNaverBinding

    fun updateFragText(text: String) {
        binding.fragtext.text = text // 네이버 viewpage에 있는 text변경
        Log.d("IISE", "DHO DKSHLSE")
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
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNaverBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NaverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) {
            NaverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    Log.d("IISE", param1 + param2)
                }
            }
        }
    }
}
