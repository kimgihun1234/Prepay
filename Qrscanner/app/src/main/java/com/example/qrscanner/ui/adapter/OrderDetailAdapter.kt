package com.example.qrscanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrscanner.databinding.OrderdetailRowBinding
import com.example.qrscanner.response.OrderDetailRes

class OrderDetailAdapter(var orderDetailList: List<OrderDetailRes>) :
    RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {
    inner class OrderDetailViewHolder(private val binding: OrderdetailRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderDetailRes: OrderDetailRes) {
            binding.detailMoneyTextView.setText(orderDetailRes.detailPrice)
            binding.productNameTextView.setText(orderDetailRes.product)
            binding.detailQuantityTextView.setText(orderDetailRes.quantity)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding =
            OrderdetailRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderDetailList.size
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bind(orderDetailList[position])
    }


}