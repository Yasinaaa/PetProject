package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class CartRequest (
    @SerializedName("promocode") val promoCode: String?,
    @SerializedName("items") val items: List<CartItemRequest>
)