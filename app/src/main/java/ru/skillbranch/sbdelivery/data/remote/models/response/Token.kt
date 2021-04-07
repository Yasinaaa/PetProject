package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("accessToken") val accessToken: String
)