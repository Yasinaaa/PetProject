package ru.skillbranch.sbdelivery.data.remote.interceptors

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.GlobalContext
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.skillbranch.sbdelivery.BuildConfig
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager.Companion.REFRESH_TOKEN
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken

class TokenAuthenticator(
    val prefManager: PrefManager)
: Authenticator {

    //val lazyApi by lazy { GlobalContext.get().get<RestService>() }

    override fun authenticate(route: Route?, response: Response): Request? {
        return if (response.code == 401 || response.code == 402) {

            //TODO
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val api = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(RestService::class.java)

            val res = api.refreshTokenCall(RefreshToken(REFRESH_TOKEN)).execute()
            return if (res.isSuccessful) {
                val bearer = "Bearer ${res.body()!!.accessToken}"
                prefManager.accessToken = bearer
                response.request.newBuilder()
                    .header("Authorization", bearer)
                    .build()

            } else null

        } else null
    }
}