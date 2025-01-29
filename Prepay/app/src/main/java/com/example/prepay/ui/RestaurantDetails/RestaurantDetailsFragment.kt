package com.example.prepay.ui.RestaurantDetails

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.FragmentRestaurantDetailsBinding
import com.example.prepay.ui.MainActivity

class RestaurantDetailsFragment: BaseFragment<FragmentRestaurantDetailsBinding>(
    FragmentRestaurantDetailsBinding::bind,
    R.layout.fragment_restaurant_details
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

    }

    private fun initAdapter() {
        val orderHistoryList = listOf(
            OrderHistory("김기훈", 100000),
            OrderHistory("김성수", 50000),
            OrderHistory("경이현", 60000),
            OrderHistory("서현석", 90000),
            OrderHistory("차현우", 40000),
            OrderHistory("조성윤", 90000),
            OrderHistory("김기훈", 100000),
            OrderHistory("김성수", 50000),
            OrderHistory("경이현", 60000),
            OrderHistory("서현석", 90000),
            OrderHistory("차현우", 40000),
            OrderHistory("조성윤", 90000),
        )
        val orderHistoryAdapter = OrderHistoryAdapter(orderHistoryList)
        binding.recyclerView.adapter = orderHistoryAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
    }
}

//    fun customReceipt() {
//        val binding = DialogCostomBinding.inflate(LayoutInflater.from(requireContext()))
//        val dialog = MaterialAlertDialogBuilder(requireContext())
//            .setView(binding.root)
//            .create()
//        dialog.setOnShowListener {
//            val window = dialog.window
//            window?.setLayout(1000, 1500)
//        }
//
//        val countryList = listOf("South Korea", "USA", "Japan", "China", "Germany", "France","South Korea", "USA", "Japan", "China", "Germany", "France")
//        val adapter = OrderHistoryAdapter(countryList)
//
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            this.adapter = adapter
//        }
//
//        dialog.show()
//    }