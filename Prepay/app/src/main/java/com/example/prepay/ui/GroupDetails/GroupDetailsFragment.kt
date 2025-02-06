package com.example.prepay.ui.GroupDetails

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.User
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.databinding.DialogAuthoritySettingBinding
import com.example.prepay.databinding.DialogGroupExitBinding
import com.example.prepay.databinding.DialogGroupResignBinding
import com.example.prepay.databinding.DialogInviteCodeBinding
import com.example.prepay.databinding.DialogQrDiningTogetherBinding
import com.example.prepay.databinding.FragmentGroupDetailsBinding
import com.example.prepay.ui.MainActivity
import com.google.android.material.navigation.NavigationView
import javax.sql.DataSource

class GroupDetailsFragment: BaseFragment<FragmentGroupDetailsBinding>(
    FragmentGroupDetailsBinding::bind,
    R.layout.fragment_group_details
), RestaurantAdapter.OnRestaurantClickListener, OnTeamUserActionListener{
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
        teamUserAdapter = TeamUserAdapter(teamUserList,this)
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
        binding.diningTogetherQrBtn.setOnClickListener {
            showQrCodeDialog()
        }

        binding.groupInviteBtn.setOnClickListener {
            showInviteCodeInputDialog()
        }
        binding.groupExitBtn.setOnClickListener {
            showGroupExitDialog()
        }
        binding.addRestaurant.setOnClickListener {
            addRestaurantClick()
        }
    }

    private fun addRestaurantClick() {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.ADD_RESTAURANT_FRAGMENT)
    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
    }

    override fun onManageClick(user: User) {
        showAuthoritySettingDialog()
    }

    override fun onResignClick(user: User) {
        showGroupResignDialog()
    }

    private fun showQrCodeDialog(){
        val binding = DialogQrDiningTogetherBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
    }



    private fun showInviteCodeInputDialog() {
        val binding = DialogInviteCodeBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        binding.inviteCodeConfirmBtn.setOnClickListener {
            val code = binding.etInviteCode.text.toString()
            if (code.isNotEmpty()) {
                Toast.makeText(requireContext(), "코드 입력: $code", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.inviteCodeCancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showGroupExitDialog() {
        val binding = DialogGroupExitBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.groupExitConfirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.groupExitCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showGroupResignDialog() {
        val binding = DialogGroupResignBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.groupResignConfirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.groupResignCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAuthoritySettingDialog() {
        val binding = DialogAuthoritySettingBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.autoritySettingConfirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.autoritySettingCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}

