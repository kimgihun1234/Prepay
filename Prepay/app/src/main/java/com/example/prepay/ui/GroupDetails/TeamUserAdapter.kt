package com.example.prepay.ui.GroupDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.prepay.data.response.TeamUserRes
import com.example.prepay.databinding.ItemTeamUserBinding

class TeamUserAdapter(var teamUserResList: List<TeamUserRes>,
                      private val actionListener: OnTeamUserActionListener, var userposition: Boolean) :
    RecyclerView.Adapter<TeamUserAdapter.TeamUserViewHolder>() {

    private var first = true // 첫 번째 아이템 표시 여부를 추적

    inner class TeamUserViewHolder(private val binding: ItemTeamUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teamUserRes: TeamUserRes, isLeader: Boolean, isFirst: Boolean) {
            binding.teamUserName.text = teamUserRes.nickname
            binding.userEmail.text = teamUserRes.email

            // 반장일 경우만 버튼 표시
            if (isLeader) {
                binding.manageBtn.visibility = View.VISIBLE
                binding.resignBtn.visibility = View.VISIBLE
            } else {
                binding.manageBtn.visibility = View.GONE
                binding.resignBtn.visibility = View.GONE
                binding.imageView7.visibility = View.GONE
            }
            if (isLeader == teamUserRes.position) {
                binding.manageBtn.visibility = View.GONE
                binding.resignBtn.visibility = View.GONE
                binding.imageView7.visibility = View.GONE
            }

            if(teamUserRes.privilege==true){
                binding.manageBtn.visibility = View.GONE
                binding.authority.visibility = View.VISIBLE
            }
            else{
                binding.authority.visibility = View.GONE
            }
            // 첫 번째 아이템만 imageView7을 보이게 설정
            if (isFirst) {
                binding.imageView7.visibility = View.VISIBLE
                binding.authority.visibility = View.GONE
            } else {
                binding.imageView7.visibility = View.GONE
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
        // 첫 번째 항목인지 여부를 전달
        val isFirst = position == 0 // 현재 아이템이 첫 번째 아이템인지 확인
        holder.bind(teamUserResList[position], userposition, isFirst)

        // 첫 번째 아이템을 지나치면 first를 false로 설정
        if (isFirst) {
            first = false // 첫 번째 아이템 지나면 첫 번째 상태 변경
        }
    }

    override fun getItemCount(): Int = teamUserResList.size

    // userposition 변경 시 UI 반영
    fun updateUserPosition(isLeader: Boolean) {
        userposition = isLeader
        notifyDataSetChanged()
    }
}