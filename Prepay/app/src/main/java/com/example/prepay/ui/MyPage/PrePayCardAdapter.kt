package com.example.prepay.ui.MyPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.model.dto.PrePayCard
import com.example.prepay.databinding.ItemCardBinding

class PrePayCardAdapter(private val cardList: List<PrePayCard>) : RecyclerView.Adapter<PrePayCardAdapter.CardViewHolder>() {

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: PrePayCard) {
            binding.tvTitle.text = card.title
            binding.tvSubTitle.text = card.subTitle
            binding.tvBalance.text = card.balance
            binding.cardview.setCardBackgroundColor(card.color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cardList[position])
    }

    override fun getItemCount(): Int = cardList.size
}