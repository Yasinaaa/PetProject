package ru.skillbranch.sbdelivery

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.skillbranch.sbdelivery.data.remote.NetworkMonitor
import ru.skillbranch.sbdelivery.di.AppModule

class App : Application(){//, ImageLoaderFactory {

    private val monitor: NetworkMonitor by inject()

//    override fun newImageLoader(): ImageLoader {
//        return ImageLoader.Builder(applicationContext)
//            .crossfade(true)
//            .okHttpClient {
//                OkHttpClient.Builder()
//                    .cache(CoilUtils.createDefaultCache(applicationContext))
//                    .build()
//            }
//            .build()
//    }

    override fun onCreate() {
        super.onCreate()
        initKoin()

        monitor.registerNetworkMonitor()
//        val imageLoader = ImageLoader.Builder(applicationContext)
//            .crossfade(true)
//            .okHttpClient {
//                OkHttpClient.Builder()
//                    .cache(CoilUtils.createDefaultCache(applicationContext))
//                    .build()
//            }
//            .build()
//        Coil.setImageLoader(imageLoader)
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