package com.example.prepay.ui.GroupDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.R
import com.example.prepay.data.model.dto.Restaurant

class RestaurantSearchAdapter(
    private val onItemClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantSearchAdapter.ViewHolder>(RestaurantDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = getItem(position) // getItem()을 사용해 현재 아이템을 가져옵니다
        holder.bind(restaurant)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.search_result_text)
//        private val nameTextView: EditText = itemView.findViewById(R.id.restaurant_name)
        fun bind(restaurant: Restaurant) {
            nameTextView.setText(restaurant.name ?: "")
            itemView.setOnClickListener {
                onItemClick(restaurant) // 아이템 클릭 시 콜백 호출
            }
        }
    }

    // DiffUtil을 사용해 리스트의 변경 사항을 효율적으로 처리합니다
    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.name == newItem.name // name이 같으면 같은 아이템으로 취급
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }
}

