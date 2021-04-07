package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

data class UserLoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)