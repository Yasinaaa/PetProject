package ru.skillbranch.sbdelivery.data.remote.models.request

import com.google.gson.annotations.SerializedName

data class UserEmailRequest(
    @SerializedName("email") val email: String?
)