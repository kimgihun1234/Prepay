package com.example.prepay.ui.GroupDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.User
import com.example.prepay.databinding.ItemTeamUserBinding

class TeamUserAdapter(private val userList: List<User>,
                      private val actionListener: OnTeamUserActionListener) :
    RecyclerView.Adapter<TeamUserAdapter.TeamUserViewHolder>() {

    class TeamUserViewHolder(private val binding: ItemTeamUserBinding,private val actionListener: OnTeamUserActionListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.teamUserName.text = user.name
            binding.manageBtn.setOnClickListener {
                actionListener.onManageClick(user)
            }

            // 강퇴 버튼 클릭 이벤트
            binding.resignBtn.setOnClickListener {
                actionListener.onResignClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamUserViewHolder {
        val binding = ItemTeamUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamUserViewHolder(binding,actionListener)
    }

    override fun onBindViewHolder(holder: TeamUserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
}