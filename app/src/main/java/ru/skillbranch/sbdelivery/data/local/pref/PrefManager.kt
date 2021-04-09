package ru.skillbranch.sbdelivery.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.squareup.moshi.Moshi
import androidx.lifecycle.map
import ru.skillbranch.sbdelivery.data.local.pref.PrefLiveDelegate
import ru.skillbranch.sbdelivery.data.local.pref.PrefLiveObjDelegate
import ru.skillbranch.sbdelivery.data.local.pref.PrefObjDelegate
import ru.skillbranch.sbdelivery.data.remote.models.response.User

class PrefManager(context: Context, moshi: Moshi) {

    internal val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var accessToken by PrefDelegate("")
    var refreshToken by PrefDelegate("")
    var profile: User? by PrefObjDelegate(moshi.adapter(User::class.java))

    val isAuthLive: LiveData<Boolean> by lazy {
        val token by PrefLiveDelegate("accessToken", "", preferences)
        token.map { it.isNotEmpty() }
    }

    val profileLive: LiveData<User?> by PrefLiveObjDelegate(
        "profile",
        moshi.adapter(User::class.java),
        preferences
    )

    fun clearAll() {
        preferences.edit().clear().apply()
    }

}