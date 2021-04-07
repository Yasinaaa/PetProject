package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

data class RecoveryPasswordRequest(
    @SerializedName("email") val email: String,
    @SerializedName("code") val code: String,
    @SerializedName("password") val password: String
)