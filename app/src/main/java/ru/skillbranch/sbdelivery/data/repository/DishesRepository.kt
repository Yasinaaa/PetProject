package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.mapper.DishesMapper
import ru.skillbranch.sbdelivery.data.remote.DeliveryApi
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.Category
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.data.repository.contract.DishRepositoryContract

class DishesRepository(
    private val api: DeliveryApi,
    private val mapper: DishesMapper,
    private val dishesDao: DishesDao
) : DishRepositoryContract {

    override fun getDishes(): Single<List<DishEntity>> {
        TODO("Not yet implemented")
    }

    override fun getCachedDishes(): Single<List<DishEntity>> {
        TODO("Not yet implemented")
    }

    override fun getCategories(): Single<List<Category>> {
        TODO("Not yet implemented")
    }

    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> {
        TODO("Not yet implemented")
    }

    //    override fun getDishes(): Single<List<DishEntity>> =
//        api.refreshToken(RefreshToken(DeliveryRetrofitProvider.REFRESH_TOKEN))
//            .flatMap { api.getDishes(0, 1000, "${DeliveryRetrofitProvider.BEARER} ${it.accessToken}") }
//            .doOnSuccess { dishes: List<Dish> ->
//                val savePersistDishes: List<DishPersistEntity> = mapper.mapDtoToPersist(dishes)
//                dishesDao.insert(savePersistDishes)
//            }
//            .doOnError {
//
//            }
//            .map { mapper.mapDtoToEntity(it) }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//
//
//    override fun getCachedDishes(): Single<List<DishEntity>> {
//        return dishesDao.getAllDishes()
//            .map {
//                mapper.mapPersistToEntity(it)
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    override fun getCategories(): Single<List<Category>> {
//        return api.refreshToken(RefreshToken(DeliveryRetrofitProvider.REFRESH_TOKEN))
//            .flatMap {
//                api.getCategories(0, 1000, "${DeliveryRetrofitProvider.BEARER} ${it.accessToken}")
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }
//
//    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> {
//        return dishesDao.findDishesByName(searchText)
//            .toObservable()
//            .map {
//                mapper.mapPersistToEntity(it)
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }
}