package ru.skillbranch.sbdelivery.data.remote.models

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("name") val name: String
)