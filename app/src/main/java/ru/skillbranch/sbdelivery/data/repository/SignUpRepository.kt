package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.request.*
import ru.skillbranch.sbdelivery.data.remote.models.response.User
import ru.skillbranch.sbdelivery.data.remote.models.response.UserWithTokens

interface ISignUpRepository {
    fun login(email: String, password: String): Single<UserWithTokens>
    fun register(firstName: String, lastName: String,
                 email: String, password: String): Single<UserWithTokens>
    fun recoveryEmail(email: String): Single<Boolean>
    fun recoveryCode(email: String, code: String): Single<Boolean>
    fun recoveryPassword(email: String, code: String, password: String): Single<Boolean>
}

class SignUpRepository(
    private val api: RestService,
    private val prefs: PrefManager,
): ISignUpRepository {

    override fun login(email: String, password: String): Single<UserWithTokens> {
        return api.login(UserLoginRequest(email, password)).saveTokensAndUser()
    }

    override fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Single<UserWithTokens> {
        return api.register(UserRegisterRequest(firstName, lastName, email, password))
            .saveTokensAndUser()
    }

    override fun recoveryEmail(email: String): Single<Boolean> {
        return api.recoveryEmail(UserEmailRequest(email)).saveResponseAsBool()
    }

    override fun recoveryCode(email: String, code: String): Single<Boolean> {
        return api.recoveryCode(RecoveryCodeRequest(email, code)).saveResponseAsBool()
    }

    override fun recoveryPassword(email: String, code: String, password: String): Single<Boolean> {
        return api.recoveryPassword(RecoveryPasswordRequest(email, code, password))
            .saveResponseAsBool()
    }

    private fun Single<UserWithTokens>.saveTokensAndUser(): Single<UserWithTokens> {
        return doOnSuccess {
                prefs.accessToken = it.accessToken
                prefs.refreshToken = it.refreshToken
                prefs.profile = User(it.id, it.firstName, it.lastName, it.email)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}