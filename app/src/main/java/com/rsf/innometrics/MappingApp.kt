package com.rsf.innometrics

import android.app.usage.UsageStats
import android.graphics.drawable.Drawable

data class MappingApp(
    var usageStats: UsageStats,
    var appIcon: Drawable
)
