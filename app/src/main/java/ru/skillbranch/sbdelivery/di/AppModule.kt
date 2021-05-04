package ru.skillbranch.sbdelivery.di

import androidx.room.Room
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.skillbranch.sbdelivery.BuildConfig.BASE_URL
import ru.skillbranch.sbdelivery.utils.ResourceManager
import ru.skillbranch.sbdelivery.data.local.AppDb
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.*
import ru.skillbranch.sbdelivery.data.remote.NetworkMonitor
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.interceptors.ErrorStatusInterceptor
import ru.skillbranch.sbdelivery.data.remote.interceptors.NetworkStatusInterceptor
import ru.skillbranch.sbdelivery.data.remote.interceptors.TokenAuthenticator
import ru.skillbranch.sbdelivery.data.repository.*
import ru.skillbranch.sbdelivery.ui.basket.BasketViewModel
import ru.skillbranch.sbdelivery.ui.dish.DishViewModel
import ru.skillbranch.sbdelivery.ui.dish.review.ReviewDialogViewModel
import ru.skillbranch.sbdelivery.ui.root.RootViewModel
import ru.skillbranch.sbdelivery.ui.main.MainViewModel
import ru.skillbranch.sbdelivery.ui.menu.MenuViewModel
import ru.skillbranch.sbdelivery.ui.search.SearchViewModel
import ru.skillbranch.sbdelivery.ui.sign.SignViewModel
import ru.skillbranch.sbdelivery.ui.sign.forgetpassword.ForgetPasswordViewModel
import java.util.concurrent.TimeUnit

object AppModule {

    fun appModule() = module {
        single { PrefManager(context = get(), moshi = get()) }
        single { ResourceManager(context = get()) }
        single {
            ImageLoader.Builder(context= get())
                .componentRegistry { add(SvgDecoder(context= get())) }
                .build()
        }
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

    fun dataModule() = module {
        single<IProfileRepository> { ProfileRepository(api = get(), prefs = get()) }

        single<IDishesMapper> { DishesMapper(context = get()) }
        single<IDishRepository> {
            DishesRepository(prefs = get(), api = get(), mapper = get(), dishesDao = get())
        }

        single<IReviewsMapper> { ReviewsMapper() }
        single<IReviewsRepository> {
            ReviewsRepository(prefs = get(), api = get(), mapper = get(), reviewDao = get())
        }

        single<ICategoriesMapper> { CategoriesMapper() }
        single<ICategoryRepository> {
            CategoriesRepository(prefs = get(), api = get(), mapper = get(), categoryDao = get())
        }

        single<ICartMapper> { CartMapper() }
        single<ICartRepository> {
            CartRepository(prefs = get(), api = get(), mapper = get(), cartDao = get())
        }

        single<ISignUpRepository> {
            SignUpRepository(prefs = get(), api = get())
        }
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
        viewModel{ RootViewModel(handle = get(), repository = get()) }
        viewModel { MainViewModel(handle = get(), repository = get()) }
        viewModel { SearchViewModel(handle = get(), categoryRepo = get(), dishRepo = get()) }
        viewModel { MenuViewModel(handle = get(), repository = get()) }
        viewModel { DishViewModel(handle = get(),
            profRep = get(), dishRep = get(), basketRep = get(),
            reviewRep = get(), dishesMapper = get(), reviewsMapper = get()) }
        viewModel {
            ReviewDialogViewModel(handle = get(), reviewsMapper = get(), reviewRep = get())
        }
        viewModel {
            BasketViewModel(profRep = get(), handle = get(), cartRep = get(), mapper = get())
        }
        viewModel { SignViewModel(handle = get(), rep = get()) }
        viewModel { ForgetPasswordViewModel(handle = get(), rep = get()) }
//        scope<RootActivity> {
//            scoped { Session() }
//            viewModel<RootViewModel>(named("vm3"))
//        }
    }


//    scope<MVVMActivity> {
//        scoped { Session() }
//        fragment { MVVMFragment(get()) }
//        viewModel { ExtSimpleViewModel(get()) }
//        viewModel<ExtSimpleViewModel>(named("ext"))
//        viewModel<SavedStateViewModel>(named("vm3")) // graph injected usage + builder API
//    }

}