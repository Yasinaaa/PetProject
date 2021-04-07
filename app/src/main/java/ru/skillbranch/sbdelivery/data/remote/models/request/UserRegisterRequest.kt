package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class UserRegisterRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)