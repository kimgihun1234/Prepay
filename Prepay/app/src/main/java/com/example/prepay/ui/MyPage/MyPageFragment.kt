package com.example.prepay.ui.MyPage

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentMyPageBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel

private const val TAG = "MyPageFragment"
class MyPageFragment: BaseFragment<FragmentMyPageBinding>(
    FragmentMyPageBinding::bind,
    R.layout.fragment_my_page
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var cardAdapter: TeamCardAdapter
    private val viewModel: MyPageFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initEvent()

        val test_btn = view.findViewById<View>(R.id.test_button)
        test_btn.setOnClickListener {
            val intent = Intent(requireContext(), CreateQRActivity::class.java)
            startActivity(intent)
        }
    }

    fun initAdapter() {
        cardAdapter = TeamCardAdapter(arrayListOf())
        binding.viewPager.adapter = cardAdapter
        viewModel.teamListInfo.observe(viewLifecycleOwner) { it ->
            cardAdapter.teamList = it
            if (cardAdapter.teamList.isNotEmpty()) {
                binding.viewPager.setCurrentItem(cardAdapter.itemCount - 1, false)
            }
            cardAdapter.notifyDataSetChanged()
        }
        viewModel.getAllTeamList()

        cardAdapter.itemClickListener = object : TeamCardAdapter.ItemClickListener {
            override fun onClick(teamId: Int) {
                activityViewModel.setTeamId(teamId.toLong())
                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT)
            }
        }
        // 스택 효과 추가
        binding.viewPager.setPageTransformer(StackPageTransformer())
        binding.viewPager.offscreenPageLimit = 5
        binding.viewPager.setCurrentItem(0, false)
    }
    fun initEvent() {

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