package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("address") val address: String?,
    @SerializedName("entrance") val entrance: Int?,
    @SerializedName("floor") val floor: Int?,
    @SerializedName("apartment") val apartment: String?,
    @SerializedName("intercom") val intercom: String?,
    @SerializedName("comment") val comment: String?,
)