package ru.skillbranch.sbdelivery.data.remote.models.response

import android.content.Context
import com.google.gson.annotations.SerializedName
import ru.skillbranch.sbdelivery.R

class Address{

    @SerializedName("city") var city: String? = ""
    @SerializedName("street") var street: String? = ""
    @SerializedName("house") var house: String? = ""

    fun string(context: Context): String {
        return String.format(context.getString(R.string.address_input), city, street, house)
    }
}
