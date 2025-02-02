package com.example.prepay.ui.RestaurantDetails

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.model.dto.Receipt
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.RestaurantPayerBinding
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "OrderHistoryAdapter"
class OrderHistoryAdapter(private val orderHistoryList: List<OrderHistory>) : RecyclerView.Adapter<OrderHistoryAdapter.orderHistoryViewHolder>() {
    inner class orderHistoryViewHolder(private val binding : RestaurantPayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderHistory) {
            binding.name.text = order.name
            binding.amount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.total_price)
            binding.date.text = order.date
            binding.imageButton.setOnClickListener {
                val dialogBinding = DialogReceiptBinding.inflate(LayoutInflater.from(itemView.context))
                val dialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogBinding.root)
                    .create()

                dialog.setOnShowListener {
                    val window = dialog.window
                    window?.setLayout(1100, 1600)
                }


                // 영수증 데이터 추가
                val countryList = listOf(
                    Receipt("망고주스", 2000,5),
                    Receipt("크림파스타", 12000,4),
                    Receipt("김치찌개", 8000,3),
                    Receipt("제육볶음", 9000,1),
                    Receipt("돈까스", 15000,2),
                    Receipt("닭한마리칼국수", 7000,3),
                    Receipt("아메리카노", 1500,1),
                    Receipt("잡채", 3000,3),
                    Receipt("떡국", 6000,2),
                    Receipt("오징어볶음", 13000,4),
                    Receipt("김치", 1000,1),
                )

                // RecyclerView 어댑터 설정
                val receiptHistoryAdapter = ReceiptHistoryAdapter(countryList)
                dialogBinding.recyclerView.layoutManager = LinearLayoutManager(itemView.context)
                dialogBinding.recyclerView.adapter = receiptHistoryAdapter
                dialogBinding.useName.text = order.name
                dialogBinding.restaurantAmount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.total_price)
                dialogBinding.receiptDate.text = order.date
                dialogBinding.orderDate.text = "[주문] ${order.date}"
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderHistoryViewHolder {
        val binding = RestaurantPayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return orderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: orderHistoryViewHolder, position: Int) {
        holder.bind(orderHistoryList[position])
    }

    override fun getItemCount(): Int {
        return orderHistoryList.size
    }
}
