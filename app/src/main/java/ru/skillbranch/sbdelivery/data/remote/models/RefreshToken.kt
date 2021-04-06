package ru.skillbranch.sbdelivery.data.remote.models

import com.google.gson.annotations.SerializedName

data class RefreshToken(
    @SerializedName("refreshToken") val refreshToken: String
)