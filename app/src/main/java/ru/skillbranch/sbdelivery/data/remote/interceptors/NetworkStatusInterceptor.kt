package ru.skillbranch.sbdelivery.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.skillbranch.sbdelivery.data.remote.NetworkMonitor
import ru.skillbranch.sbdelivery.data.remote.err.NoNetworkError

class NetworkStatusInterceptor(val monitor: NetworkMonitor) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //return response or throw error
        if(!monitor.isConnected)
            throw NoNetworkError()

        return chain.proceed(chain.request())
    }
}