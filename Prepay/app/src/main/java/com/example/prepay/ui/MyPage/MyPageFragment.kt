package com.example.prepay.ui.MyPage

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.model.dto.PrePayCard
import com.example.prepay.databinding.DialogVisitCodeBinding
import com.example.prepay.databinding.FragmentMyPageBinding
import com.example.prepay.ui.MainActivity

class MyPageFragment: BaseFragment<FragmentMyPageBinding>(
    FragmentMyPageBinding::bind,
    R.layout.fragment_my_page
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initEvent()
    }

    fun initAdapter(){
        // 카드 데이터 생성
        val cardList = listOf(
            PrePayCard("SSAFY", "Private", "잔액 8,000,000 원", Color.parseColor("#7E57C2")),
            PrePayCard("SSAFY 2", "Public", "잔액 5,000,000 원", Color.parseColor("#FF4081")),
            PrePayCard("SSAFY 3", "Private", "잔액 1,000,000 원", Color.parseColor("#EF5350")),
            PrePayCard("SSAFY", "Private", "잔액 8,000,000 원", Color.parseColor("#7E57C2")),
            PrePayCard("SSAFY 2", "Public", "잔액 5,000,000 원", Color.parseColor("#FF4081")),
            PrePayCard("SSAFY 3", "Private", "잔액 1,000,000 원", Color.parseColor("#EF5350"))
        )
        val cardAdapter = PrePayCardAdapter(cardList)
        binding.viewPager.adapter = cardAdapter

        cardAdapter.itemClickListener = object :PrePayCardAdapter.ItemClickListener{
            override fun onClick(productId: Int) {
                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT)
            }
        }
        // 스택 효과 추가
        binding.viewPager.setPageTransformer(StackPageTransformer())
        binding.viewPager.offscreenPageLimit = 5
        binding.viewPager.setCurrentItem(5, false)
        // 초기 화면에서 첫 번째 카드가 맨 앞으로 보이도록 설정
    }

    fun initEvent(){

        binding.createGroupBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
        }
        binding.enterGroupBtn.setOnClickListener {
            mainActivity.enterDialog()
        }
        //내비게이션 바 생기게
        mainActivity.hideBottomNav(false)
    }

}