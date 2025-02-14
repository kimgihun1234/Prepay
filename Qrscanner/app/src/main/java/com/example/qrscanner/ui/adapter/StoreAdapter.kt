package com.example.qrscanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrscanner.databinding.StoreRowBinding
import com.example.qrscanner.response.StoreRes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoreAdapter  (var storeList: List<StoreRes>) : RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    inner class StoreViewHolder(private val binding: StoreRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storeRes: StoreRes) {
            val timestamp: Long = storeRes.orderDate.toLong()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateString: String = dateFormat.format(Date(timestamp))

            binding.date.text = dateString
            binding.price.text = storeRes.totalPrice.toString()
            binding.refund.text = storeRes.refundRequested.toString()
            binding.withdraw.text = storeRes.withdraw.toString()
            binding.companyDinner.text = storeRes.companyDinner.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = StoreRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun getItemCount(): Int = storeList.size

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(storeList[position])
    }
}