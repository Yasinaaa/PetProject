package ru.skillbranch.sbdelivery.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("icon") val icon: String,
    @SerializedName("parent") val parent: Int,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
)