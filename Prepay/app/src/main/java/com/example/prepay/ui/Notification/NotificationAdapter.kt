package com.example.prepay.ui.Notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prepay.R
import com.example.prepay.data.response.OrderHistoryRes
import com.example.prepay.databinding.ItemMyhistoryBinding

class NotificationAdapter(var orderHistoryList : List<OrderHistoryRes>) : RecyclerView.Adapter<NotificationAdapter.NotiViewHolder>() {
    inner class NotiViewHolder (private val binding: ItemMyhistoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(orderHistoryRes: OrderHistoryRes) {
            binding.orderDate.text = orderHistoryRes.orderDate.toString()
            binding.orderAmount.text = orderHistoryRes.totalPrice.toString()
            binding.orderStoreName.text = orderHistoryRes.storeName
            Glide.with(binding.root.context)
                .load(orderHistoryRes.storeImgUrl)
                // 이미지 로드중 로드 실패시에는 로고 띄워줌
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.orderImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val binding = ItemMyhistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotiViewHolder(binding)
    }

    override fun getItemCount(): Int = orderHistoryList.size

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.bind(orderHistoryList[position])
    }
}