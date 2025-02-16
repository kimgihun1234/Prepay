package com.example.prepay.ui.Notification

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentNotificationBinding
import com.example.prepay.ui.MainActivity

private const val TAG = "NotificationFragment_싸피"
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(
    FragmentNotificationBinding::bind,
    R.layout.fragment_notification
) {
    private lateinit var mainActivity: MainActivity
    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initEvent()
    }
    private fun initEvent() {
        Log.d(TAG,"초기화중")
        viewModel.getMyOrderHistoryList()
    }

    private fun initAdapter() {

        notificationAdapter = NotificationAdapter(arrayListOf())
        binding.rvUsageHistory.adapter = notificationAdapter
        binding.rvUsageHistory.layoutManager = LinearLayoutManager(requireContext())
        viewModel.myOrderHistoryList.observe(viewLifecycleOwner) {
            notificationAdapter.orderHistoryList = it
            notificationAdapter.notifyDataSetChanged()
        }
    }


}