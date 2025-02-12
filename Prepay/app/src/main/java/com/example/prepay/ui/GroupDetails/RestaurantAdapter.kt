package com.example.prepay.ui.GroupDetails

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.databinding.ItemRestaurantBinding
import com.example.prepay.ui.MainActivityViewModel

private const val TAG = "RestaurantAdapter"
class RestaurantAdapter(var teamIdStoreResList: List<TeamIdStoreRes>, private val listener: OnRestaurantClickListener,var userLocation: Location) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(private val binding: ItemRestaurantBinding,private val listener: OnRestaurantClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teamIdStoreRes: TeamIdStoreRes, userLocation: Location) {
            binding.restaurantName.text = teamIdStoreRes.storeName
            binding.restaurantPrepayMoney.text = teamIdStoreRes.balance.toString()+"원"
            // 거리 계산
            val distance = calculateDistance(
                userLocation.latitude, userLocation.longitude,
                teamIdStoreRes.latitude, teamIdStoreRes.longitude
            )
            binding.restaurantDistance.text = "%.1f km".format(distance)
            binding.restaurantDetailBtn.setOnClickListener {
                Log.d(TAG, "teamIdStoreRes.storeName: ${teamIdStoreRes.storeName}")
                listener.onRestaurantClick(teamIdStoreRes.storeName, teamIdStoreRes.storeId)
            }
        }
        private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val R = 6371.0 // 지구 반경 (단위: km)
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)

            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

            return R * c // 두 지점 간의 거리 (단위: km)
        }
    }

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding,listener)
    }

    // view에 바인딩
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(teamIdStoreResList[position],userLocation)
    }

    // 갯수 반환
    override fun getItemCount(): Int {
        return teamIdStoreResList.size
    }

    interface OnRestaurantClickListener {
        fun onRestaurantClick(storeName : String, teamIdStoreResId: Int)
    }
}