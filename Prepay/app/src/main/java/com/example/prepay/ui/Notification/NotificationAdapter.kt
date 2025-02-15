package com.example.prepay.ui.Notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.response.OrderHistoryRes
import com.example.prepay.databinding.ItemMyhistoryBinding

//class NotificationAdapter(orderHistoryList : List<OrderHistoryRes>) : RecyclerView.Adapter<NotificationAdapter.NotiViewHolder>() {
//    inner class NotiViewHolder (private val binding: ItemMyhistoryBinding) : RecyclerView.ViewHolder(binding.root){
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
//        val binding = ItemMyhistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return NotiViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = orderHistoryList.size
//
//    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
//        holder.bind(orderHistoryList[position])
//    }
//}