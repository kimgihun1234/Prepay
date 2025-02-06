package com.example.prepay.ui.RestaurantDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.FragmentRestaurantDetailsBinding
import com.example.prepay.ui.MainActivity

class RestaurantDetailsFragment: BaseFragment<FragmentRestaurantDetailsBinding>(
    FragmentRestaurantDetailsBinding::bind,
    R.layout.fragment_restaurant_details
){
    private lateinit var mainActivity: MainActivity
    private lateinit var receiptbinding : DialogReceiptBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapter()

    }
    private fun initEvent() {
        binding.payBootpay.setOnClickListener {
            val restaurant = ViewModelProvider(requireActivity()).get(RestaurantDetailsViewModel::class.java)
            restaurant.sendRestaurantData(binding.restaurantNameBootpay.text.toString())
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.DETAIL_RESTAURANT_FRAGMENT)
        }
    }

    private fun initAdapter() {
        val orderHistoryList = listOf(
            OrderHistory("2025.01.26","김기훈", 100000),
            OrderHistory("2025.01.13","김성수", 50000),
            OrderHistory("2025.01.22","경이현", 60000),
            OrderHistory("2025.01.25","서현석", 90000),
            OrderHistory("2025.02.26","차현우", 40000),
            OrderHistory("2025.01.16","조성윤", 90000),
            OrderHistory("2025.01.05","김기훈", 100000),
            OrderHistory("2025.01.23","김성수", 50000),
            OrderHistory("2025.01.29","경이현", 60000),
            OrderHistory("2025.01.31","서현석", 90000),
            OrderHistory("2025.02.02","차현우", 40000),
            OrderHistory("2025.01.20","조성윤", 90000,),
        )

        receiptbinding = DialogReceiptBinding.inflate(LayoutInflater.from(context))
        val orderHistoryAdapter = OrderHistoryAdapter(orderHistoryList)
        binding.recyclerView.adapter = orderHistoryAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
    }
}