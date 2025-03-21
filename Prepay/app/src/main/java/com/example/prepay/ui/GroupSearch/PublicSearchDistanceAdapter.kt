package com.example.prepay.ui.GroupSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prepay.CommonUtils
import com.example.prepay.DistanceManager
import com.example.prepay.R
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsDisRes
import com.example.prepay.databinding.ItemPublicGroupBinding

class PublicSearchDistanceAdapter(var publicGroupList: List<PublicTeamsDisRes>, private val listener: OnPublicKmLimitClickListener) :
    RecyclerView.Adapter<PublicSearchDistanceAdapter.PublicGroupViewHolder>() {

    class PublicGroupViewHolder(private val binding: ItemPublicGroupBinding, private val listener: OnPublicKmLimitClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLiked = false  // 좋아요 상태 저장
        fun bind(publicgroup: PublicTeamsDisRes) {
            binding.publicName.text = publicgroup.teamName
            binding.publicDistance.text = DistanceManager.formatDistance(publicgroup.distance)
            binding.publicMoneyInfo.text = CommonUtils.makeComma(publicgroup.teamBalance)
            binding.publicAddress.text = publicgroup.address
            // 그룹 이미지 가지고 오기
            Glide.with(binding.root.context)
                .load(publicgroup.imgUrl)
                // 이미지 로드중 로드 실패시에는 로고 띄워줌
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.groupImage)

            val groupImage = binding.groupImage
            groupImage.clipToOutline = true

            // ❤️ 초기 하트 상태 설정 (서버 값 반영)
            isLiked = publicgroup.like
            updateHeartIcon()

            // ❤️ 하트 버튼 클릭 이벤트 (서버 반영 포함)
            binding.publicSearchLikeBtn.setOnClickListener {
                isLiked = !isLiked  // 상태 변경
                updateHeartIcon()  // 아이콘 변경
                val likereq = LikeTeamsReq(publicgroup.teamId.toLong(), isLiked)
                listener.onKmLimitLikeGroupClick(likereq)
            }

            // 부모 레이아웃 터치 이벤트 방지 (하트 버튼 클릭 시 아이템 클릭 방지)
            binding.publicSearchLikeBtn.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }

            // 아이템 클릭 이벤트
            binding.root.setOnClickListener {
                listener.onKmLimitGroupClick(publicgroup)
            }
        }

        private fun updateHeartIcon() {
            if (isLiked) {
                binding.publicSearchLikeBtn.setImageResource(R.drawable.like_heart_fill)  // 채워진 하트 이미지
            } else {
                binding.publicSearchLikeBtn.setImageResource(R.drawable.like_heart_empty)  // 빈 하트 이미지
            }
        }
    }

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicGroupViewHolder {
        val binding = ItemPublicGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PublicGroupViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: PublicGroupViewHolder, position: Int) {
        val currentGroup = publicGroupList[position]
        holder.bind(currentGroup)
    }


    // 갯수 반환
    override fun getItemCount(): Int {
        return publicGroupList.size
    }
}