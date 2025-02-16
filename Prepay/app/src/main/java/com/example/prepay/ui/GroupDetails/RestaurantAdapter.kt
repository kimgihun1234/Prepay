package com.example.prepay.ui.GroupDetails

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.CommonUtils
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.databinding.ItemRestaurantBinding
import com.example.prepay.ui.MainActivityViewModel

private const val TAG = "RestaurantAdapter"
class RestaurantAdapter(var teamIdStoreResList: List<TeamIdStoreRes>, private val listener: OnRestaurantClickListener) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(private val binding: ItemRestaurantBinding,private val listener: OnRestaurantClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teamIdStoreRes: TeamIdStoreRes) {
            binding.restaurantName.text = teamIdStoreRes.storeName
            binding.restaurantPrepayMoney.text = CommonUtils.makeComma(teamIdStoreRes.balance)
            binding.restaurantDetailBtn.setOnClickListener {
                Log.d(TAG, "teamIdStoreRes.storeName: ${teamIdStoreRes.storeName}")
                listener.onRestaurantClick(teamIdStoreRes.storeName, teamIdStoreRes.storeId)
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
        holder.bind(teamIdStoreResList[position])
    }

    // 갯수 반환
    override fun getItemCount(): Int {
        return teamIdStoreResList.size
    }

    interface OnRestaurantClickListener {
        fun onRestaurantClick(storeName : String, teamIdStoreResId: Int)
    }
}