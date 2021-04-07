package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") val id: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("text") val text: String?,
    @SerializedName("active") val active: Boolean?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)
