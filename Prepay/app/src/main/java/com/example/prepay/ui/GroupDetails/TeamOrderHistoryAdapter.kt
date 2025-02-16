package com.example.prepay.ui.GroupDetails

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.R
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.RestaurantPayerBinding
import com.example.prepay.ui.RestaurantDetails.ReceiptHistoryAdapter
import com.example.prepay.ui.RestaurantDetails.ReceiptViewModel
import java.text.NumberFormat
import java.util.Locale

class TeamOrderHistoryAdapter(var orderHistoryList: List<OrderHistory>,private val listener: ImageButtonClick
) : RecyclerView.Adapter<TeamOrderHistoryAdapter.orderHistoryViewHolder>() {

    private lateinit var receiptHistoryAdapter: ReceiptHistoryAdapter

    inner class orderHistoryViewHolder(private val binding : RestaurantPayerBinding,private val listener: ImageButtonClick) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderHistory) {
            if (order.companyDinner) {
                binding.root.setBackgroundResource(R.drawable.round_background)
            } else {
                // 다른 색
                binding.root.setBackgroundResource(R.drawable.round_background)
            }
            binding.name.text = order.orderHistoryId.toString()
            binding.amount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
            binding.date.text = order.orderDate

            // 영수증 버튼 클릭 시 다이얼로그 표시
            binding.imageButton.setOnClickListener {
                listener.onClick(itemView, order, order.orderHistoryId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderHistoryViewHolder {
        val binding =
            RestaurantPayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return orderHistoryViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: orderHistoryViewHolder, position: Int) {
        holder.bind(orderHistoryList[position])
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
    interface ImageButtonClick {
        fun onClick(itemView: View, order: OrderHistory, orderHistoryId : Int)
    }

}