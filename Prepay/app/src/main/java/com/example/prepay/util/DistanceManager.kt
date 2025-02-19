package com.example.prepay

import java.math.RoundingMode
import kotlin.math.*

object DistanceManager {
    fun distance(preLat: Double?, preLng: Double?, postLat: Double?, postLng: Double?): String {
        if (preLat == null || preLng == null || postLat == null || postLng == null) {
            return ""
        }

        val result =  (6371000 * acos(
            cos(compareRadians(preLat))
                    * cos(compareRadians(postLat))
                    * cos(compareRadians(postLng) - compareRadians(preLng))
                    + sin(compareRadians(preLat)) * sin(compareRadians(postLat))
        )).toBigDecimal().setScale(0, RoundingMode.HALF_UP)

        return "$result m"
    }

    private fun compareRadians(degrees: Double): Double {
        return Math.toRadians(degrees)
    }

    fun formatDistance(distance: Double): String {
        return if (distance >= 1.0) {
            String.format("%.0fkm", distance)
        } else {
            String.format("%.0fm", distance * 1000)
        }
    }
}
