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
    class TeamUserViewHolder(private val binding: ItemTeamUserBinding,private val actionListener: OnTeamUserActionListener,var userposition: Boolean) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teamUserRes: TeamUserRes) {
            binding.teamUserName.text = teamUserRes.nickname
            if(userposition==false||teamUserRes.position==userposition){
                binding.manageBtn.visibility = View.GONE
                binding.resignBtn.visibility = View.GONE
            }

            binding.manageBtn.setOnClickListener {
                actionListener.onManageClick(teamUserRes)
            }
            // 강퇴 버튼 클릭 이벤트
            binding.resignBtn.setOnClickListener {
                actionListener.onResignClick(teamUserRes)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamUserViewHolder {
        val binding = ItemTeamUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamUserViewHolder(binding,actionListener,userposition)
    }

    override fun onBindViewHolder(holder: TeamUserViewHolder, position: Int) {
        holder.bind(teamUserResList[position])
    }

    override fun getItemCount(): Int = teamUserResList.size
}