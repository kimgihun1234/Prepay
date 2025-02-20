package com.example.qrscanner.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.MainActivity
import com.example.qrscanner.MainActivityViewModel
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentOrderDetailBinding
import com.example.qrscanner.response.OrderDetailRes
import com.example.qrscanner.ui.adapter.OrderDetailAdapter
import kotlinx.coroutines.launch

private const val TAG = "OrderDetailFragment"

class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(
    FragmentOrderDetailBinding::bind,
    R.layout.fragment_order_detail
) {
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var orderDetailAdapter: OrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapt()
    }

    private fun initAdapt() {
        binding.orderDetailRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.orderIdNumTextView.text = activityViewModel.orderId.value.toString()
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.posService.getDetailHistories(activityViewModel.orderId.value!!)
            }.onSuccess { response ->
                Log.d(TAG,"Response Code : ${response.code()}")
                Log.d(TAG,"Response ErrorBody : ${response.errorBody()}")
                val orderDetails = response.body()
                orderDetailAdapter = OrderDetailAdapter(orderDetails!!)
                binding.orderDetailRecyclerView.adapter = orderDetailAdapter
            }.onFailure { response ->
                response.printStackTrace()
                showToast("OrderDetails 실패")
                Log.d(TAG, "실패")
            }
        }
    }


}