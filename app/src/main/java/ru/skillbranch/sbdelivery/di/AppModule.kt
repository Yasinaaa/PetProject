package ru.skillbranch.sbdelivery.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.skillbranch.sbdelivery.core.ResourceManager
import ru.skillbranch.sbdelivery.data.local.AppDb
import ru.skillbranch.sbdelivery.data.mapper.CategoriesMapper
import ru.skillbranch.sbdelivery.data.mapper.DishesMapper
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.remote.RetrofitProvider
import ru.skillbranch.sbdelivery.data.repository.DishesRepository
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.main.MainViewModel
import ru.skillbranch.sbdelivery.ui.search.SearchViewModel

object AppModule {
    fun appModule() = module {
        single { RetrofitProvider.createRetrofit() }

        single<IDishRepository> { DishesRepository(api = get(), mapper = get(), dishesDao = get()) }
        single<IDishesMapper> { DishesMapper() }
        single { CategoriesMapper() }


        single { ResourceManager(context = get()) }
//        single<SearchUseCase> { SearchUseCaseImpl(get()) }
//        single<BasketNotifier> { BasketNotifierImpl() }
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
        viewModel { MainViewModel(repository = get(), dishesMapper = get())}
            //, categoriesMapper = get(), notifier = get()) }
//        viewModel { SearchViewModel(useCase = get(), mapper = get()) }
    }
}