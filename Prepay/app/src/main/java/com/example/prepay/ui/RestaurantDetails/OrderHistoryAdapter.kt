package com.example.prepay.ui.RestaurantDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.model.dto.Receipt
import com.example.prepay.databinding.DialogCostomBinding
import com.example.prepay.databinding.RestaurantPayerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OrderHistoryAdapter(private val orderHistoryList: List<OrderHistory>) : RecyclerView.Adapter<OrderHistoryAdapter.orderHistoryViewHolder>() {
    inner class orderHistoryViewHolder(private val binding: RestaurantPayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderHistory) {
            binding.name.text = order.name
            binding.amount.text = order.total_price.toString()
            binding.imageButton.setOnClickListener {
                val binding = DialogCostomBinding.inflate(LayoutInflater.from(itemView.context))
                val dialog = MaterialAlertDialogBuilder(itemView.context)
                    .setView(binding.root)
                    .create()
                dialog.setOnShowListener {
                    val window = dialog.window
                    window?.setLayout(1000, 1500)
                }

                val countryList = listOf(
                    Receipt("망고주스", 2000),
                    Receipt("크림파스타", 12000),
                    Receipt("김치찌개", 8000),
                    Receipt("제육볶음", 9000),
                    Receipt("돈까스", 15000),
                    Receipt("닭한마리칼국수", 7000),
                    Receipt("아메리카노", 1500),
                    Receipt("잡채", 3000),
                    Receipt("떡국", 6000),
                    Receipt("오징어볶음", 13000),
                    Receipt("김치", 1000),
                )
                val adapter = ReceiptHistoryAdapter(countryList)

                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(itemView.context)
                    this.adapter = adapter
                }

                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderHistoryViewHolder {
        val binding = RestaurantPayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return orderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: orderHistoryViewHolder, position: Int) {
        holder.bind(orderHistoryList[position])
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
}
