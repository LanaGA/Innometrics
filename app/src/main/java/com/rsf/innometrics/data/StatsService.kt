package com.rsf.innometrics.data

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface StatsService {
    @Headers("Content-Type: application/json")
    @POST("users/login/")
    fun login(@Body credentials: RequestBody): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("users/")
    fun register(@Body credentials: RequestBody): Call<RegistrationResponse>

}