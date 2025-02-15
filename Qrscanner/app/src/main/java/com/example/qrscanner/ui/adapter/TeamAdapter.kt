package com.example.qrscanner.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qrscanner.databinding.ItemRowBinding
import com.example.qrscanner.response.TeamRes

private const val TAG = "TeamAdapter"
class TeamAdapter (var teamList: List<TeamRes>, private val onButtonClick: OnButtonClick) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    inner class TeamViewHolder(private val binding : ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teamRes: TeamRes) {
            binding.teamId.text = teamRes.teamName
            binding.money.text = teamRes.storeBalance.toString()
            Log.d(TAG, "bind: ${binding.teamId.text}")

            binding.teamBtn.setOnClickListener {
                onButtonClick.onClick(teamRes.teamId)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int = teamList.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teamList[position])
    }
    interface OnButtonClick {
        fun onClick(teamId : Int)
    }
}