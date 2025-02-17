package com.example.prepay.ui.GroupDetails

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
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.response.OrderHistoryReq
import com.example.prepay.data.response.OrderHistoryRes
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.databinding.FragmentGroupPaymentHistoryBinding
import com.example.prepay.databinding.FragmentLookGroupBinding
import com.example.prepay.ui.LoginActivity
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsViewModel
import kotlinx.coroutines.launch

private const val TAG = "GroupPaymentHistoryFrag_클릭"
class GroupPaymentHistoryFragment: BaseFragment<FragmentGroupPaymentHistoryBinding>(
    FragmentGroupPaymentHistoryBinding::bind,
    R.layout.fragment_group_payment_history
),TeamOrderHistoryAdapter.ImageButtonClick{
    private lateinit var mainActivity: MainActivity
    private lateinit var teamOrderHistoryAdapter: TeamOrderHistoryAdapter
    private lateinit var teamOrderHistoryList: List<OrderHistory>

    //뷰모델
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailsFragmentViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()
        initEvent()
    }

    fun initEvent(){
        viewModel.getTeamDetail(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
        val rq = OrderHistoryReq(activityViewModel.teamId.value!!,0)
        viewModel.getMyTeamOrderHistory(SharedPreferencesUtil.getAccessToken()!!,rq)
    }

    fun initAdapter(){
        teamOrderHistoryList = emptyList()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        teamOrderHistoryAdapter= TeamOrderHistoryAdapter(teamOrderHistoryList,this)
        binding.recyclerView.adapter = teamOrderHistoryAdapter
    }

    private fun initViewModel(){
        viewModel.teamOrderListInfo.observe(viewLifecycleOwner){it->
            teamOrderHistoryAdapter.orderHistoryList = it
            teamOrderHistoryAdapter.notifyDataSetChanged()
        }

        viewModel.teamDetail.observe(viewLifecycleOwner){it->
            Log.d(TAG,"감지된 데이터입니다"+it.toString())
            binding.totalAmount.text = CommonUtils.makeComma(it.teamBalance)
        }
    }

    override fun onClick(itemView: View, order: OrderHistory, orderHistoryId: Int) {
        Log.d(TAG,"클릭")
    }

}