package com.rsf.innometrics.vo

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["app_name"])
data class Stats(
    @field:SerializedName("app_name")
    val app_name: String,
    @field:SerializedName("time_begin")
    val time_begin: String,
    @field:SerializedName("time_end")
    val time_end: String
)
