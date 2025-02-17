package com.example.prepay.ui.MyPage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.CommonUtils
import com.example.prepay.data.response.Team
import com.example.prepay.databinding.ItemCardBinding

class TeamCardAdapter(var teamList: List<Team>) : RecyclerView.Adapter<TeamCardAdapter.CardViewHolder>() {
    lateinit var itemClickListener: ItemClickListener

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Team) {
            binding.tvTitle.text = card.teamName
            binding.tvSubTitle.text = card.balance.toString()
            binding.tvBalance.text = String.format("%,d", card.balance)
            binding.cardview.setCardBackgroundColor(
                runCatching {
                    val colorCode = card.color?.takeIf { it != "null" } ?: "#FFFFFF" // "null"을 기본값 #FFFFFF로 처리
                    Color.parseColor(colorCode)
                }.getOrElse {
                    Color.WHITE // 오류 발생 시 기본값으로 흰색
                }
            )
            binding.cardview.setOnClickListener {
                itemClickListener.onClick(teamList[layoutPosition].teamId)
            }
            binding.tvSubTitle.text = CommonUtils.formatLongToDate(card.genDate)
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