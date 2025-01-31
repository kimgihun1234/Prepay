package com.example.prepay.ui.RestaurantDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.model.dto.Receipt
import com.example.prepay.databinding.ReceiptListBinding
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "ReceiptHistoryAdapter"
class ReceiptHistoryAdapter(private val countryList: List<Receipt>) : RecyclerView.Adapter<ReceiptHistoryAdapter.ReceiptViewHolder>() {
    inner class ReceiptViewHolder(private val binding: ReceiptListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receipt: Receipt) {
            binding.foodName.text = receipt.product
            binding.quantity.text = receipt.quantity.toString()
            binding.detailPrice.text = NumberFormat.getNumberInstance(Locale.KOREA).format(receipt.detailPrice)
            binding.foodPrice.text = NumberFormat.getNumberInstance(Locale.KOREA).format(receipt.detailPrice * receipt.quantity)
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
