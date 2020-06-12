package com.rsf.innometrics.vo

import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Stats(
//        @field:SerializedName("id")
//        val id: Int,
        @PrimaryKey
        @field:SerializedName("app_name")
        val app_name: String,
        @field:SerializedName("time_total")
        val time_total: Long
        /*@field:SerializedName("time_end")
        val time_end: Long?*/
)
