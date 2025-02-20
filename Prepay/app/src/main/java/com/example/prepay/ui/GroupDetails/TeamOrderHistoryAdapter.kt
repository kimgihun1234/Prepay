package com.example.prepay.ui.GroupDetails

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.RestaurantPayerBinding
import com.example.prepay.ui.RestaurantDetails.ReceiptHistoryAdapter
import com.example.prepay.ui.RestaurantDetails.ReceiptViewModel
import java.text.NumberFormat
import java.util.Locale

class TeamOrderHistoryAdapter(var orderHistoryList: List<OrderHistory>,private val listener: ImageButtonClick
) : RecyclerView.Adapter<TeamOrderHistoryAdapter.orderHistoryViewHolder>() {

    private lateinit var receiptHistoryAdapter: ReceiptHistoryAdapter

    inner class orderHistoryViewHolder(private val binding : RestaurantPayerBinding,private val listener: ImageButtonClick) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderHistory) {
            // 입금이 진행된 경우에는 색상이 파란색으로 변경
            if (!order.withdraw) {
                val formattedAmount = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
                val spannableString = SpannableString(formattedAmount)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#0066CC")), 0, formattedAmount.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.amount.text = spannableString
                binding.imageButton.setVisibility(View.INVISIBLE);
            } else {
                binding.amount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
            }
            binding.name.text = order.nickname
            binding.date.text = CommonUtils.formatLongToDate(order.orderDate.toLong())

            // 영수증 버튼 클릭 시 다이얼로그 표시
            binding.imageButton.setOnClickListener {
                listener.onClick(itemView, order, order.orderHistoryId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderHistoryViewHolder {
        val binding =
            RestaurantPayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return orderHistoryViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: orderHistoryViewHolder, position: Int) {
        holder.bind(orderHistoryList[position])
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
    interface ImageButtonClick {
        fun onClick(itemView: View, order: OrderHistory, orderHistoryId : Int)
    }

}