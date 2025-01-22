package com.example.prepay.ui.MyPage

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class StackPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scaleFactor = 1 - 0.05f * kotlin.math.abs(position)
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor

        val translationFactor = if (position < 0) {
            -position * (page.height * 0.8f) // 80% 겹치게 설정
        } else {
            position * (page.height * 0.8f)
        }

        page.translationY = translationFactor
        page.translationZ = -kotlin.math.abs(position) * 10f

    }
}