package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Favorite(
    @SerializedName("dishId") val dishId: String,
    @SerializedName("favorite") val favorite: Boolean,
    @SerializedName("updatedAt") val updatedAt: String
)