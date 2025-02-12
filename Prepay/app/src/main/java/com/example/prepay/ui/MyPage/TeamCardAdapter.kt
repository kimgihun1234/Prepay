package com.example.prepay.ui.MyPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.response.Team
import com.example.prepay.databinding.ItemCardBinding

class TeamCardAdapter(var teamList: List<Team>) : RecyclerView.Adapter<TeamCardAdapter.CardViewHolder>() {
    lateinit var itemClickListener: ItemClickListener

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Team) {
            binding.tvTitle.text = card.teamName
            binding.tvSubTitle.text = card.balance.toString()
            binding.tvBalance.text = card.balance.toString()
            binding.cardview.setOnClickListener {
                itemClickListener.onClick(teamList[layoutPosition].teamId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(teamList[position])
    }

    override fun getItemCount(): Int = teamList.size

    interface ItemClickListener {
        fun onClick(teamId: Int)
    }
}