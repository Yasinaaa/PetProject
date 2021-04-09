package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class OrderStatus(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("cancelable") val cancelable: Boolean?,
    @SerializedName("active") val active: Boolean?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)
