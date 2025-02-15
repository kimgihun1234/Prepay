package com.example.prepay.ui.RestaurantDetails

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.R
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.RestaurantPayerBinding
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "OrderHistoryAdapter"
class OrderHistoryAdapter(var orderHistoryList: List<OrderHistory>,
                          private val viewModel: ReceiptViewModel,
                          private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<OrderHistoryAdapter.orderHistoryViewHolder>() {

    private lateinit var receiptHistoryAdapter: ReceiptHistoryAdapter
    lateinit var imageButtonClick : ImageButtonClick

    inner class orderHistoryViewHolder(private val binding : RestaurantPayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: OrderHistory) {
            Log.d(TAG, "bind: ")
            if (order.companyDinner) {
                binding.root.setBackgroundResource(R.drawable.round_background)
            } else {
                // 다른 색
                binding.root.setBackgroundResource(R.drawable.round_background)
            }
            binding.name.text = order.orderHistoryId.toString()
            binding.amount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
            binding.date.text = order.orderDate

            // 영수증 버튼 클릭 시 다이얼로그 표시
            binding.imageButton.setOnClickListener {
                imageButtonClick.onClick(itemView, order, order.orderHistoryId)

//                val dialogBinding = DialogReceiptBinding.inflate(LayoutInflater.from(itemView.context))
//                val dialog = AlertDialog.Builder(itemView.context)
//                    .setView(dialogBinding.root)
//                    .create()
//
//                dialog.setOnShowListener {
//                    val window = dialog.window
//                    window?.setLayout(1000, 1600)
//                    window?.setBackgroundDrawableResource(R.drawable.receipt_rounded_dialog)
//                }
//
//                receiptHistoryAdapter = ReceiptHistoryAdapter(arrayListOf())
//                dialogBinding.recyclerView.adapter = receiptHistoryAdapter
//
//                viewModel.receiptListInfo.observe(lifecycleOwner) {
//                    it -> receiptHistoryAdapter.receiptList = it
//                    receiptHistoryAdapter.notifyDataSetChanged()
//                }
//
//                viewModel.getAllReceiptList(1,1)
//
//                dialogBinding.recyclerView.layoutManager = LinearLayoutManager(itemView.context)
//                dialogBinding.useName.text = order.orderHistoryId.toString()
//                dialogBinding.restaurantAmount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
//                dialogBinding.receiptDate.text = order.orderDate
//                dialogBinding.orderDate.text = "[주문] ${order.orderDate}"
//                dialog.show()
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
    interface ImageButtonClick {
        fun onClick(itemView: View, order: OrderHistory, orderHistoryId : Int)
    }

}