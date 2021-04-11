package ru.skillbranch.sbdelivery

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.skillbranch.sbdelivery.data.remote.NetworkMonitor
import ru.skillbranch.sbdelivery.di.AppModule

class App : Application() {

    private val monitor: NetworkMonitor by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin()

        monitor.registerNetworkMonitor()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    AppModule.appModule(),
                    AppModule.databaseModule(),
                    AppModule.networkModule(),
                    AppModule.dataModule(),
                    AppModule.viewModelModule(),
                )
            )
        }
    }

}