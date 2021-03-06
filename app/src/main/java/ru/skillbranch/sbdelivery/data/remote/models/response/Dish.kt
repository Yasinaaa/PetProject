package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("oldPrice") val oldPrice: Float = 0F,
    @SerializedName("price") val price: Float = 0F,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("likes") val likes: Long?,
    @SerializedName("category") val category: String?,
    @SerializedName("commentsCount") val commentsCount: Int?,
    @SerializedName("active") val active: Boolean?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)
