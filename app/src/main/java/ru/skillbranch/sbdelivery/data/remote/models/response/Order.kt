package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id") val id: String,
    @SerializedName("total") val total: Float?,
    @SerializedName("address") val address: String?,
    @SerializedName("statusId") val statusId: String?,
    @SerializedName("active") val active: Boolean?,
    @SerializedName("completed") val completed: Boolean?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("items") val items: List<OrderItem>
)

data class OrderItem(
    @SerializedName("name") val name: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("amount") val amount: Int?,
    @SerializedName("price") val price: Float?,
    @SerializedName("dishId") val dishId: String?
)