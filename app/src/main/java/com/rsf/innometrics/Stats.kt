package com.rsf.innometrics

import android.app.usage.UsageStats
import android.graphics.drawable.Drawable

data class Stats(
    var usageStats: UsageStats,
    var appIcon: Drawable
)

data class User(
    var user: String
)
//app_name time_start,  time_end