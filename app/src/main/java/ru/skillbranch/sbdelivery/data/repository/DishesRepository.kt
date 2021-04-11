package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.response.Category
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish

interface IDishRepository {
    fun getFavorite(offset: Int, limit: Int): Single<List<DishEntity>>
    fun changeFavorite(dishId: Int, favorite: Boolean): Single<Boolean>
    fun getRecommended(): Single<List<DishEntity>>
    fun getCategories(offset: Int, limit: Int): Single<List<Category>>
    fun getDishes(offset: Int, limit: Int): Single<List<DishEntity>>
    fun getCachedDishes(): Single<List<DishEntity>>
    //
    fun findDishesByName(searchText: String): Observable<List<DishEntity>>
}

class DishesRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: IDishesMapper,
    private val dishesDao: DishesDao
) : IDishRepository {

    override fun getDishes(offset: Int, limit: Int): Single<List<DishEntity>> =
        api.getDishes(offset, limit,
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .doOnSuccess { dishes: List<Dish> ->
                val savePersistDishes: List<DishEntity> = mapper.mapDtoToPersist(dishes)
                dishesDao.insert(savePersistDishes)
            }
            .doOnError {

            }
            .map { mapper.mapDtoToPersist(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCachedDishes(): Single<List<DishEntity>> =
        dishesDao.getAllDishes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getCategories(offset: Int, limit: Int): Single<List<Category>> =
        api.getCategories(offset, limit,
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> =
        dishesDao.findDishesByName(searchText)
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getFavorite(offset: Int, limit: Int): Single<List<DishEntity>> =
        api.getFavorite(offset, limit,
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .map {
                dishesDao.updateFavorite(mapper.mapFavoriteToStringIds(it))
                dishesDao.updateNotFavorite(mapper.mapFavoriteToStringIds(it))
            }
            .flatMap {
                dishesDao.getFavoriteDishes()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    override fun changeFavorite(dishId: Int, favorite: Boolean): Single<Boolean> =
        api.changeFavorite(dishId, favorite,
            "${PrefManager.BEARER} ${prefs.accessToken}").saveResponseAsBool()

    override fun getRecommended(): Single<List<DishEntity>> =
        api.getRecommend()
            .flatMap {
                dishesDao.findDishesByIds(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}