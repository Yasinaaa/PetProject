package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class OrderCancelRequest (
    @SerializedName("orderId") val orderId: String?
)