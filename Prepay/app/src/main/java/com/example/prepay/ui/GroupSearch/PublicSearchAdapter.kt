package com.example.prepay.ui.GroupSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.databinding.ItemPublicGroupBinding
import com.bumptech.glide.Glide
import com.example.prepay.R
import com.example.prepay.data.response.PublicTeamsRes

class PublicSearchAdapter(var publicGroupList: List<PublicTeamsRes>, private val listener: OnPublicClickListener) :
    RecyclerView.Adapter<PublicSearchAdapter.PublicGroupViewHolder>() {

    class PublicGroupViewHolder(private val binding: ItemPublicGroupBinding,private val listener: OnPublicClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(publicgroup: PublicTeamsRes) {
            binding.publicName.text = publicgroup.teamName
//            binding.content.text = publicgroup.address
//            binding.publicDistance.text = publicgroup.distance.toString()+"km"
            binding.publicMoneyInfo.text = publicgroup.teamBalance.toString()

            // 그룹 이미지 가지고 오기
            Glide.with(binding.root.context)
                .load(publicgroup.imageURL)
                // 이미지 로드중 로드 실패시에는 로고 띄워줌
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.groupImage)

            binding.root.setOnClickListener {
                listener.onGroupClick(publicgroup)
            }
        }
    }

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicGroupViewHolder {
        val binding = ItemPublicGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicGroupViewHolder(binding,listener)
    }

    // view에 바인딩
    override fun onBindViewHolder(holder: PublicGroupViewHolder, position: Int) {
        val currentGroup = publicGroupList[position]
        holder.bind(currentGroup)
    }

    // 갯수 반환
    override fun getItemCount(): Int {
        return publicGroupList.size
    }

    interface OnPublicClickListener {
        fun onGroupClick(publicgroup: PublicTeamsRes)
    }
}