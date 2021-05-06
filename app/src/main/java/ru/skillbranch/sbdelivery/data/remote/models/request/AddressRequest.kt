package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class AddressRequest(
    @SerializedName("address") val address: String?
)