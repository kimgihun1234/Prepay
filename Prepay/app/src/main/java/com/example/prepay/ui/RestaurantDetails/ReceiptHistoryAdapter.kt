package com.example.prepay.ui.RestaurantDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.model.dto.Receipt
import com.example.prepay.databinding.ReceiptListBinding

class ReceiptHistoryAdapter(private val countryList: List<Receipt>) : RecyclerView.Adapter<ReceiptHistoryAdapter.ReceiptViewHolder>() {
    inner class ReceiptViewHolder(private val binding: ReceiptListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receipt: Receipt) {
            binding.foodName.text = receipt.foodName
            binding.foodPrice.text = receipt.foodPrice.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val binding = ReceiptListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiptViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int {
        return countryList.size
    }
}
