package com.rsf.innometrics.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Stats(
        @PrimaryKey(autoGenerate = true)
        @field:SerializedName("id")
        val id: Int,
        @field:SerializedName("app_name")
        val app_name: String,
        @field:SerializedName("time_begin")
        val time_begin: Long,
        @field:SerializedName("time_end")
        val time_end: Long?
)
