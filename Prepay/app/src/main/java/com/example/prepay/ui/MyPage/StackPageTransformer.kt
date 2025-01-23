package com.example.prepay.ui.MyPage

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class StackPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scaleFactor = 1 - 0.05f * kotlin.math.abs(position)
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor

        val translationFactor = when {
            position < 0 -> { // 현재 페이지 또는 왼쪽에 있는 페이지
                -position * (page.height * 0.80f) // 90% 겹치도록 설정
            }
            position > 0 -> { // 오른쪽에 있는 페이지
                -position * (page.height * 0.8f)
            }
            else -> 0f
        }
        page.translationY = translationFactor
        page.translationZ = -kotlin.math.abs(position) * 10f
        page.alpha = if (position <= 1) {
            1f // 현재 위치에 있는 카드들은 완전히 보이게 설정
        } else {
            0f // 그 외의 카드들은 반투명하게 설정
        }
    }
}