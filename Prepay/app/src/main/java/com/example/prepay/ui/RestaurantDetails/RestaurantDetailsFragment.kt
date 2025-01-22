package com.example.prepay.ui.RestaurantDetails

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentRestaurantDetailsBinding
import com.example.prepay.ui.MainActivity

class RestaurantDetailsFragment: BaseFragment<FragmentRestaurantDetailsBinding>(
    FragmentRestaurantDetailsBinding::bind,
    R.layout.fragment_restaurant_details
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            edit()
        }
    }

    fun edit() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Test")
            .setMessage("Real Test")
            .setPositiveButton("확인", DialogInterface.OnClickListener{dialog, id ->  })
            .setNegativeButton("취소", DialogInterface.OnClickListener{dialog, id ->  })
        builder.show()

    }
}