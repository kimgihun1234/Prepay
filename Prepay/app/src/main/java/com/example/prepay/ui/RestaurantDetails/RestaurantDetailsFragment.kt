package com.example.prepay.ui.RestaurantDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.FragmentRestaurantDetailsBinding
import com.example.prepay.ui.MainActivity

class RestaurantDetailsFragment: BaseFragment<FragmentRestaurantDetailsBinding>(
    FragmentRestaurantDetailsBinding::bind,
    R.layout.fragment_restaurant_details
){
    private lateinit var mainActivity: MainActivity
    private lateinit var receiptbinding : DialogReceiptBinding

    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private val viewModel : OrderHistoryViewModel by viewModels()
    private val ReceiptViewModel : ReceiptViewModel by viewModels()

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

        receiptbinding = DialogReceiptBinding.inflate(LayoutInflater.from(context))

        receiptbinding.restaurantName.text = binding.restaurantNameBootpay.text
        Log.d("receiptbinding.restaurantName.text", "receiptbinding.restaurantName.text: ${receiptbinding.restaurantName.text}")
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        orderHistoryAdapter = OrderHistoryAdapter(arrayListOf(), ReceiptViewModel, this)
        binding.recyclerView.adapter = orderHistoryAdapter
        viewModel.orderHistoryListInfo.observe(viewLifecycleOwner) { it->
            Log.d("RestaurantDetailsFragment", "orderHistoryListInfo 변경됨: ${it.size} 개")
            orderHistoryAdapter.orderHistoryList = it
            orderHistoryAdapter.notifyDataSetChanged()
        }
        viewModel.getAllOrderHistoryList(1,0,1)
        Log.d("RestaurantDetailsFragment", "getAllOrderHistoryList() 호출됨")
    }
}