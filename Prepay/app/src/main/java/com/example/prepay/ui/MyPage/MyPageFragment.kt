package com.example.prepay.ui.MyPage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentMyPageBinding
import com.example.prepay.ui.GroupDetails.MyPageFragmentViewModel
import com.example.prepay.ui.MainActivity

private const val TAG = "MyPageFragment"
class MyPageFragment: BaseFragment<FragmentMyPageBinding>(
    FragmentMyPageBinding::bind,
    R.layout.fragment_my_page
){
    private lateinit var mainActivity: MainActivity
    private lateinit var cardAdapter: TeamCardAdapter
    private val viewModel: MyPageFragmentViewModel by viewModels()

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
        cardAdapter = TeamCardAdapter(arrayListOf())
        binding.viewPager.adapter = cardAdapter
        viewModel.teamListInfo.observe(viewLifecycleOwner){ it ->
            cardAdapter.teamList = it
            if (cardAdapter.teamList.isNotEmpty()) {
                binding.viewPager.setCurrentItem(cardAdapter.itemCount - 1, false)
            }
            cardAdapter.notifyDataSetChanged()
        }
        viewModel.getAllTeamList()

        cardAdapter.itemClickListener = object :TeamCardAdapter.ItemClickListener{
            override fun onClick(productId: Int) {
                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT)
            }
        }
        // 스택 효과 추가
        binding.viewPager.setPageTransformer(StackPageTransformer())
        binding.viewPager.offscreenPageLimit = 5
        binding.viewPager.setCurrentItem(0, false)
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