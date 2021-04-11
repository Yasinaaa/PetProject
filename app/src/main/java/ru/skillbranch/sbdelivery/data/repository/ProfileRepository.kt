package ru.skillbranch.sbdelivery.data.repository

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.request.*
import ru.skillbranch.sbdelivery.data.remote.models.response.User

interface IProfileRepository {
    fun getCachedProfile(): LiveData<User?>
    fun getProfile(): Single<User>
    fun putProfile(firstName: String, lastName: String, email: String): Single<Boolean>
    fun changePassword(oldPassword: String, newPassword: String): Single<Boolean>
}

class ProfileRepository(
    private val api: RestService,
    private val prefs: PrefManager,
): IProfileRepository {

    override fun getCachedProfile(): LiveData<User?> = prefs.profileLive

    override fun getProfile(): Single<User> {
        return api.getProfile("${PrefManager.BEARER} ${prefs.accessToken}")
            .doOnSuccess {
                prefs.profile = User(it.id, it.firstName, it.lastName, it.email)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun putProfile(
        firstName: String,
        lastName: String,
        email: String
    ): Single<Boolean> {
        return api.putProfile(firstName, lastName, email,
            "${PrefManager.BEARER} ${prefs.accessToken}").saveResponseAsBool()
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Single<Boolean> {
        return api.changePassword(ChangePasswordRequest(oldPassword, newPassword),
            "${PrefManager.BEARER} ${prefs.accessToken}").saveResponseAsBool()
    }
}