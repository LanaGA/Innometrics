package com.rsf.innometrics.data

import com.google.gson.annotations.SerializedName
import com.rsf.innometrics.vo.Stats
import java.io.Serializable

data class LoginResponse(
    @SerializedName("success")
    val successCode: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("token")
    val token: String
)

data class RegistrationResponse(
    @SerializedName("success")
    val successCode: Int,
    @SerializedName("token")
    val token: String
)

data class StatsSearchResponse(
    @SerializedName("success")
    val successCode: Int,

    @SerializedName("data")
    val Stats: List<StatsResponse>?
)

data class StatsResponse(
    @SerializedName("stats")
    val stats: Stats
) : Serializable
