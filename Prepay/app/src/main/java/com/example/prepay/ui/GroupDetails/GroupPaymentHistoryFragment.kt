package com.example.prepay.ui.GroupDetails

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
import com.example.prepay.databinding.FragmentGroupPaymentHistoryBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel

import java.text.NumberFormat
import java.util.Locale

private const val TAG = "GroupPaymentHistoryFrag_클릭"
class GroupPaymentHistoryFragment: BaseFragment<FragmentGroupPaymentHistoryBinding>(
    FragmentGroupPaymentHistoryBinding::bind,
    R.layout.fragment_group_payment_history
),TeamOrderHistoryAdapter.ImageButtonClick{
    private lateinit var mainActivity: MainActivity
    private lateinit var teamOrderHistoryAdapter: TeamOrderHistoryAdapter
    private lateinit var teamOrderHistoryList: List<OrderHistory>
    private lateinit var receiptHistoryAdapter : ReceiptHistoryAdapter

    //뷰모델
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailsFragmentViewModel by viewModels()
    private val receiptViewModel : ReceiptViewModel by viewModels()


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
        Log.d(TAG, "onClick: $orderHistoryId")
        receiptViewModel.getAllReceiptList(orderHistoryId, SharedPreferencesUtil.getAccessToken()!!)

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(itemView.context)
        dialogBinding.useName.text = order.orderHistoryId.toString()
        dialogBinding.restaurantAmount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
        dialogBinding.receiptDate.text = CommonUtils.formatLongToDate(order.orderDate.toLong())
        dialogBinding.orderDate.text = "[주문] ${order.nickname}"
        dialog.show()
    }

}