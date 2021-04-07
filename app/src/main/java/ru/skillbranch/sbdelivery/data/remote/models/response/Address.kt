package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city") val city: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("house") val house: String?,
)
