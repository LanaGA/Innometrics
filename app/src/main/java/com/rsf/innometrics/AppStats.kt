package com.rsf.innometrics

import android.graphics.drawable.Drawable

data class AppStats(
        var appName: String,
        var appIcon: Drawable,
        var lastTimeUsed: Long
        )
//app_name time_start,  time_end