package com.example.prepay.ui.RestaurantDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.databinding.ReceiptListBinding

class CountryAdapter(private val countryList: List<String>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    inner class CountryViewHolder(private val binding: ReceiptListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(country: String) {
            binding.receiptContent.text = country
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ReceiptListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int {
        return countryList.size
    }
}
