package com.example.prepay.ui.RestaurantDetails

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.FragmentRestaurantDetailsBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "RestaurantDetailsFragme"
class RestaurantDetailsFragment: BaseFragment<FragmentRestaurantDetailsBinding>(
    FragmentRestaurantDetailsBinding::bind,
    R.layout.fragment_restaurant_details
){
    private lateinit var mainActivity: MainActivity
    private lateinit var receiptbinding : DialogReceiptBinding

    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private val orderHistoryViewModel : OrderHistoryViewModel by viewModels()
    private val receiptViewModel : ReceiptViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var receiptHistoryAdapter: ReceiptHistoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapter()

    }
    private fun initEvent() {

        binding.restaurantNameBootpay.text = activityViewModel.storeName.value

        binding.payBootpay.setOnClickListener {
            val storeId = activityViewModel.storeId
            val storeName = activityViewModel.storeName
            Log.d(TAG, "storeId, storeName : $storeId, $storeName")
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.DETAIL_RESTAURANT_FRAGMENT)
        }
    }

    private fun initAdapter() {

        receiptbinding = DialogReceiptBinding.inflate(LayoutInflater.from(context))

        receiptbinding.restaurantName.text = binding.restaurantNameBootpay.text
        Log.d("receiptbinding.restaurantName.text", "receiptbinding.restaurantName.text: ${receiptbinding.restaurantName.text}")
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        orderHistoryAdapter = OrderHistoryAdapter(arrayListOf(), receiptViewModel, this)
        binding.recyclerView.adapter = orderHistoryAdapter
        orderHistoryViewModel.orderHistoryListInfo.observe(viewLifecycleOwner) { it->
            Log.d("RestaurantDetailsFragment", "orderHistoryListInfo 변경됨: ${it.size} 개")
            orderHistoryAdapter.orderHistoryList = it
            orderHistoryAdapter.notifyDataSetChanged()
        }

        activityViewModel.teamId.value?.let {
            activityViewModel.storeId.value?.let { it1 ->
                Log.d(TAG, "getAllOrderHistoryList: $it, $it1")
                orderHistoryViewModel.getAllOrderHistoryList(1,
                    it, it1.toInt()
                )
            }
        }
        Log.d("RestaurantDetailsFragment", "getAllOrderHistoryList() 호출됨")


        orderHistoryAdapter.imageButtonClick = object : OrderHistoryAdapter.ImageButtonClick {
            override fun onClick(itemView: View, order: OrderHistory, orderHistoryId : Int) {
                val dialogBinding = DialogReceiptBinding.inflate(LayoutInflater.from(itemView.context))
                val dialog = AlertDialog.Builder(itemView.context)
                    .setView(dialogBinding.root)
                    .create()

                dialog.setOnShowListener {
                    val window = dialog.window
                    window?.setLayout(1000, 1600)
                    window?.setBackgroundDrawableResource(R.drawable.receipt_rounded_dialog)
                }

                receiptHistoryAdapter = ReceiptHistoryAdapter(arrayListOf())
                dialogBinding.recyclerView.adapter = receiptHistoryAdapter

                receiptViewModel.receiptListInfo.observe(viewLifecycleOwner) {
                        it -> receiptHistoryAdapter.receiptList = it
                    receiptHistoryAdapter.notifyDataSetChanged()
                }
                Log.d(TAG, "onClick: $orderHistoryId")
                receiptViewModel.getAllReceiptList(orderHistoryId,1)

                dialogBinding.recyclerView.layoutManager = LinearLayoutManager(itemView.context)
                dialogBinding.useName.text = order.orderHistoryId.toString()
                dialogBinding.restaurantAmount.text = NumberFormat.getNumberInstance(Locale.KOREA).format(order.totalPrice)
                dialogBinding.receiptDate.text = order.orderDate
                dialogBinding.orderDate.text = "[주문] ${order.orderDate}"
                dialog.show()
            }
        }
    }
}