package com.example.prepay.ui.Notification

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentNotificationBinding
import com.example.prepay.ui.MainActivity


class NotificationFragment : BaseFragment<FragmentNotificationBinding>(
    FragmentNotificationBinding::bind,
    R.layout.fragment_notification
) {
    private lateinit var mainActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}