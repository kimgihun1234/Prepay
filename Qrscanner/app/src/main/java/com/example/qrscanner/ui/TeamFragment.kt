package com.example.qrscanner.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrscanner.MainActivity
import com.example.qrscanner.MainActivityViewModel
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentTeamBinding
import com.example.qrscanner.response.StoreReq
import com.example.qrscanner.ui.adapter.StoreAdapter
import com.example.qrscanner.ui.viewModel.StoreViewModel

private const val TAG = "TeamFragment"
class TeamFragment : BaseFragment<FragmentTeamBinding>(
    FragmentTeamBinding::bind,
    R.layout.fragment_team
) {
    private lateinit var storeAdapter : StoreAdapter
    private lateinit var mainActivity: MainActivity
    private val mainActivityViewModel : MainActivityViewModel by activityViewModels()
    private val storeViewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapt()
    }

    private fun initEvent() {
        binding.storeId.text = "StoreId " + mainActivityViewModel.storeId.value.toString()
        binding.teamId.text = "TeamId " + mainActivityViewModel.teamId.value.toString()
    }

    private fun initAdapt() {
        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        storeAdapter = StoreAdapter(arrayListOf())
        binding.recyclerView.adapter = storeAdapter
        storeViewModel.storeList.observe(viewLifecycleOwner) {
            storeAdapter.storeList = it
            storeAdapter.notifyDataSetChanged()
            Log.d(TAG, "storeList: $it")
        }
        val storeId = mainActivityViewModel.storeId.value!!
        val teamId = mainActivityViewModel.teamId.value!!
        val storeReq = StoreReq(teamId, storeId)
        storeViewModel.getStoreList(storeReq)
    }
}