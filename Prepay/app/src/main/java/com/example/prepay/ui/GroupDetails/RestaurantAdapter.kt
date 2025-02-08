package com.example.prepay.ui.GroupDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.databinding.ItemRestaurantBinding

class RestaurantAdapter(var teamIdStoreResList: List<TeamIdStoreRes>, private val listener: OnRestaurantClickListener) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(private val binding: ItemRestaurantBinding,private val listener: OnRestaurantClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teamIdStoreRes: TeamIdStoreRes) {
            binding.restaurantName.text = teamIdStoreRes.storeName
            binding.restaurantPrepayMoney.text = teamIdStoreRes.balance.toString()+"원"
            binding.restaurantDistance.text = teamIdStoreRes.latitude.toString()
            binding.restaurantDetailBtn.setOnClickListener {
                listener.onRestaurantClick(teamIdStoreRes.storeId)
            }
        }
    }

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding,listener)
    }

    // view에 바인딩
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentRestaurant = teamIdStoreResList[position]
        holder.bind(currentRestaurant)
    }

    // 갯수 반환
    override fun getItemCount(): Int {
        return teamIdStoreResList.size
    }

    interface OnRestaurantClickListener {
        fun onRestaurantClick(teamIdStoreResId: Int)
    }
}