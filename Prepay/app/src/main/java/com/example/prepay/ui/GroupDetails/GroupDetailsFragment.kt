package com.example.prepay.ui.GroupDetails

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.User
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.databinding.DialogInviteCodeBinding
import com.example.prepay.databinding.FragmentGroupDetailsBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsFragment
import com.google.android.material.navigation.NavigationView

class GroupDetailsFragment: BaseFragment<FragmentGroupDetailsBinding>(
    FragmentGroupDetailsBinding::bind,
    R.layout.fragment_group_details
), RestaurantAdapter.OnRestaurantClickListener{
    private lateinit var mainActivity: MainActivity
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var teamUserAdapter: TeamUserAdapter
    private lateinit var restaurantList: List<Restaurant>
    private lateinit var teamUserList: List<User>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapter()
        initDrawerLayout()
    }

    private fun initAdapter(){
        restaurantList = listOf(
            Restaurant("꿀맛 식당", 10000),
            Restaurant("싸피 식당", 20000),
            Restaurant("삼성 식당", 4000)
        )
        teamUserList = listOf(
            User("김싸피","ㅇㅇㅇㄹ","ㅇㅇㅇ"),
            User("김ㄷㄷㄷ","ㄹㄹㄹ","ㅇㅇㅇ"),
            User("김ㄹㄹ","ㄱㄱㄱ","ㅇㅇㅇ")
        )
        restaurantAdapter = RestaurantAdapter(restaurantList,this)
        teamUserAdapter = TeamUserAdapter(teamUserList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = restaurantAdapter
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemberList.adapter = teamUserAdapter
    }

    private fun initDrawerLayout(){
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        drawerLayout.openDrawer(GravityCompat.END)
        binding.drawerLayout.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }


    private fun initEvent() {
        binding.groupInviteBtn.setOnClickListener {
            showInviteCodeInputDialog()
        }
    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
    }

    private fun showInviteCodeInputDialog() {
        val binding = DialogInviteCodeBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        binding.confirmBtn.setOnClickListener {
            val code = binding.etInviteCode.text.toString()
            if (code.isNotEmpty()) {
                Toast.makeText(requireContext(), "코드 입력: $code", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}

