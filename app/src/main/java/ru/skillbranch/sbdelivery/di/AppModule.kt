package ru.skillbranch.sbdelivery.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.sbdelivery.BuildConfig.BASE_URL
import ru.skillbranch.sbdelivery.core.ResourceManager
import ru.skillbranch.sbdelivery.data.local.AppDb
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.CategoriesMapper
import ru.skillbranch.sbdelivery.data.mapper.DishesMapper
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.remote.NetworkMonitor
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.interceptors.ErrorStatusInterceptor
import ru.skillbranch.sbdelivery.data.remote.interceptors.NetworkStatusInterceptor
import ru.skillbranch.sbdelivery.data.remote.interceptors.TokenAuthenticator
import ru.skillbranch.sbdelivery.data.repository.DishesRepository
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.main.MainViewModel
import java.util.concurrent.TimeUnit

object AppModule {

    fun prefModule() = module {
        single { PrefManager(context = get(), moshi = get()) }
    }

    fun networkModule() = module {
        single {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory()) //convert json to class by reflection
                .build()
        }
        single {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
        single { NetworkMonitor(get()) }
        single { NetworkStatusInterceptor(get()) }
        single { ErrorStatusInterceptor(get()) }
        single { TokenAuthenticator(get()) }

        single {
            OkHttpClient().newBuilder()
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .authenticator(get<TokenAuthenticator>())
                .addInterceptor(get<ErrorStatusInterceptor>())
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addInterceptor(get<NetworkStatusInterceptor>())
                .build()

        }
        single {
            Retrofit.Builder()
                .client(get())
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }
        single { get<Retrofit>().create(RestService::class.java)}
    }

    fun appModule() = module {
        single<IDishesMapper> { DishesMapper() }
        single<IDishRepository> {
            DishesRepository(
                prefs = get(),
                api = get(),
                mapper = get(),
                dishesDao = get())
        }
        single { ResourceManager(context = get()) }
    }

    fun databaseModule() = module {
        single {
            Room.databaseBuilder(
                get(),
                AppDb::class.java,
                AppDb.DATABASE_NAME
            ).build()
        }
        single { get<AppDb>().dishesDao() }
        single { get<AppDb>().categoryDao() }
        single { get<AppDb>().reviewDao() }
        single { get<AppDb>().orderDao() }
        single { get<AppDb>().orderStatusDao() }
        single { get<AppDb>().cartDao() }
    }

    fun viewModelModule() = module {
        viewModel { MainViewModel(handle = get(), repository = get(), dishesMapper = get())}
    }

}