package com.example.qrscanner.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.MainActivity
import com.example.qrscanner.MainActivityViewModel
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentTeamBinding
import com.example.qrscanner.response.StoreReq
import com.example.qrscanner.ui.adapter.StoreAdapter
import com.example.qrscanner.ui.viewModel.StoreViewModel
import com.example.qrscanner.util.CommonUtils
import kotlinx.coroutines.launch

private const val TAG = "TeamFragment"

class TeamFragment : BaseFragment<FragmentTeamBinding>(
    FragmentTeamBinding::bind,
    R.layout.fragment_team
), StoreAdapter.OnStoreBtnClick {
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var mainActivity: MainActivity
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val storeViewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapt()
    }

    private fun initEvent() {
//        binding.storeNameTextView.text = "상호명 : " + mainActivityViewModel.storeName.value
        binding.teamNameTextView.text = "그룹명 :  " + mainActivityViewModel.teamName.value
//        viewModel에서 호출하면 textview는 변화 감지가 안되서 null 나와서 여기서 api 또 호출해서 상호명을 넣어줌
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.posService.getStoreList(
                    StoreReq(
                        mainActivityViewModel.teamId.value!!.toLong(),
                        mainActivityViewModel.storeId.value!!.toLong()
                    )
                )
            }.onSuccess {
                binding.storeNameTextView.text = "상호명 :  " + it[0].storeName
//                mainActivityViewModel.setStoreName(it[0].storeName)
            }.onFailure { response ->
                response.printStackTrace()
                showToast("OrderDetails 실패")
                Log.d(TAG, "실패")
            }
        }
    }

    private fun initAdapt() {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        storeAdapter = StoreAdapter(arrayListOf(), onBtnClicked = this)
        binding.recyclerView.adapter = storeAdapter
        storeViewModel.storeList.observe(viewLifecycleOwner) {
            storeAdapter.storeList = it
            storeAdapter.notifyDataSetChanged()
            Log.d(TAG, "storeList: $it")
            // storeName을 받는 api가 여기 밖에 없어서 일단 여기서 viewModel에 넣었음 가게의 팀이 없으면 null 에러, 취소 그냥 teamFragment initEvent 할 때 api 또 호출해서 받음
//            mainActivityViewModel.setStoreName(it[0].storeName)
//            Log.d(TAG, storeViewModel.storeList.value!![0].storeName)
        }

        val storeId = mainActivityViewModel.storeId.value!!
        val teamId = mainActivityViewModel.teamId.value!!
        val storeReq = StoreReq(teamId.toLong(), storeId.toLong())
        storeViewModel.getStoreList(storeReq)
    }

    override fun onStoreBtnClicked(orderId: Long) {
        mainActivityViewModel.setOrderId(orderId)
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.ORDER_DETAIL_FRAGMENT)
    }
}