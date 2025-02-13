package com.example.prepay.ui.CreateGroup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prepay.databinding.ItemColorListBinding

class ColorAdapter(var colorList: List<String>, val colorClickListener: (String) -> Unit) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(private val binding : ItemColorListBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(color : String) {
            val colorInt = android.graphics.Color.parseColor(color)
            Log.d("colorInt", "bind: $colorInt")
            binding.itemPaletteColorCv.setCardBackgroundColor(colorInt)
            binding.itemPaletteColorCv.setOnClickListener {
                colorClickListener(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorList[position])
    }
}