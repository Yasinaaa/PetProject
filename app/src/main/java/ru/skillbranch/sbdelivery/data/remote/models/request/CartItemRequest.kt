package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class CartItemRequest(
    @SerializedName("id") val id: String?,
    @SerializedName("amount") val amount: Int?
)