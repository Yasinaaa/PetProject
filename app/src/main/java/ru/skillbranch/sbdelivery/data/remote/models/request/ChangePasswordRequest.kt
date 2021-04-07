package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Query

data class ChangePasswordRequest(
    @SerializedName("oldPassword") val oldPassword: String,
    @SerializedName("newPassword") val newPassword: String
)