package com.example.prepay.ui.GroupDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.prepay.data.response.TeamUserRes
import com.example.prepay.databinding.ItemTeamUserBinding

class TeamUserAdapter(var teamUserResList: List<TeamUserRes>,
                      private val actionListener: OnTeamUserActionListener,var userposition:Boolean) :
    RecyclerView.Adapter<TeamUserAdapter.TeamUserViewHolder>() {
    inner class TeamUserViewHolder(private val binding: ItemTeamUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teamUserRes: TeamUserRes, isLeader: Boolean) {
            binding.teamUserName.text = teamUserRes.nickname

            // 반장일 경우만 버튼 표시
            if (isLeader) {
                binding.manageBtn.visibility = View.VISIBLE
                binding.resignBtn.visibility = View.VISIBLE
            } else {
                binding.manageBtn.visibility = View.GONE
                binding.resignBtn.visibility = View.GONE
            }
            if(isLeader==teamUserRes.position){
                binding.manageBtn.visibility = View.GONE
                binding.resignBtn.visibility = View.GONE
            }

            binding.manageBtn.setOnClickListener {
                actionListener.onManageClick(teamUserRes)
            }

            binding.resignBtn.setOnClickListener {
                actionListener.onResignClick(teamUserRes)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamUserViewHolder {
        val binding = ItemTeamUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamUserViewHolder, position: Int) {
        holder.bind(teamUserResList[position], userposition)
    }

    override fun getItemCount(): Int = teamUserResList.size

    // userposition 변경 시 UI 반영
    fun updateUserPosition(isLeader: Boolean) {
        userposition = isLeader
        notifyDataSetChanged()
    }
}