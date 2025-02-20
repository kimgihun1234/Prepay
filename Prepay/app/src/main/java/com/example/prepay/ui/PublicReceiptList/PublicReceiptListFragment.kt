package com.example.prepay.ui.PublicReceiptList

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.response.OrderHistoryReq
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.FragmentPublicReceiptListBinding
import com.example.prepay.ui.GroupDetails.GroupDetailsFragmentViewModel
import com.example.prepay.ui.GroupDetails.ReceiptHistoryAdapter
import com.example.prepay.ui.GroupDetails.ReceiptViewModel
import com.example.prepay.ui.GroupDetails.TeamOrderHistoryAdapter
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import java.text.NumberFormat
import java.util.Locale

class PublicReceiptListFragment : BaseFragment<FragmentPublicReceiptListBinding>(
    FragmentPublicReceiptListBinding::bind,
    R.layout.fragment_public_receipt_list
),TeamOrderHistoryAdapter.ImageButtonClick{
    private lateinit var mainActivity: MainActivity
    private lateinit var teamOrderHistoryList: List<OrderHistory>
    private lateinit var teamOrderHistoryAdapter: TeamOrderHistoryAdapter
    private lateinit var receiptHistoryAdapter : ReceiptHistoryAdapter

    //뷰모델
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailsFragmentViewModel by viewModels()
    private val receiptViewModel : ReceiptViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initViewModel()
        initEvent()
    }

    fun initEvent(){
        val rq = OrderHistoryReq(activityViewModel.storeId.value!!.toLong(),0)
        viewModel.getMyTeamOrderHistory(SharedPreferencesUtil.getAccessToken()!!,rq)
    }

    fun initAdapter(){
        teamOrderHistoryList = emptyList()
        binding.receiptHistory.layoutManager = LinearLayoutManager(requireContext())
        teamOrderHistoryAdapter= TeamOrderHistoryAdapter(teamOrderHistoryList,this)
        binding.receiptHistory.adapter = teamOrderHistoryAdapter
    }

    private fun initViewModel(){
        viewModel.teamOrderListInfo.observe(viewLifecycleOwner){it->
            teamOrderHistoryAdapter.orderHistoryList = it
            teamOrderHistoryAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(itemView: View, order: OrderHistory, orderHistoryId: Int) {
        val dialogBinding = DialogReceiptBinding.inflate(LayoutInflater.from(itemView.context))
        val dialog = AlertDialog.Builder(itemView.context)
            .setView(dialogBinding.root)
            .create()

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setLayout(1000, 1600)
            window?.setBackgroundDrawableResource(R.drawable.receipt_rounded_dialog)
        }

        receiptHistoryAdapter = ReceiptHistoryAdapter(arrayListOf())
        dialogBinding.recyclerView.adapter = receiptHistoryAdapter

        receiptViewModel.receiptListInfo.observe(viewLifecycleOwner) {
                it -> receiptHistoryAdapter.receiptList = it
            receiptHistoryAdapter.notifyDataSetChanged()
        }
        receiptViewModel.getAllReceiptList(orderHistoryId, SharedPreferencesUtil.getAccessToken()!!)

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(itemView.context)
        dialogBinding.useName.text = order.orderHistoryId.toString()
        dialogBinding.restaurantAmount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
        dialogBinding.receiptDate.text = CommonUtils.formatLongToDate(order.orderDate.toLong())
        dialogBinding.orderDate.text = "[주문] ${order.nickname}"
        dialog.show()
    }
}