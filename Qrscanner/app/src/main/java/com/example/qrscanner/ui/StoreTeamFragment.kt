package com.example.qrscanner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qrscanner.MainActivity
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentStoreTeamBinding

class StoreTeamFragment : BaseFragment<FragmentStoreTeamBinding>(
    FragmentStoreTeamBinding::bind,
    R.layout.fragment_store_team
) {

    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }
}