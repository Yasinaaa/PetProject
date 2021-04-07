package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("refreshToken") val refreshToken: String
)