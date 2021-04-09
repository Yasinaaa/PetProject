package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("promocode") val promoCode: String?,
    @SerializedName("promotext") val promoText: String?,
    @SerializedName("total") val total: Float?,
    @SerializedName("items") val items: List<CartItem>?
)

data class CartItem(
    @SerializedName("id") val id: String,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("price") val price: Float?
)