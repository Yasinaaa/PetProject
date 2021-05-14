package ru.skillbranch.sbdelivery.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.internal.notify
import ru.skillbranch.sbdelivery.data.local.dao.CategoryDao
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager.Companion.REFRESH_TOKEN
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.err.EmptyDishesError
import ru.skillbranch.sbdelivery.data.remote.err.NoNetworkError
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.Category
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.data.remote.models.response.User
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import java.net.ConnectException
import java.util.function.Function

interface IDishRepository {
    fun getCachedSearchHistory(): LiveData<MutableSet<String>>
    fun getFavorite(offset: Int, limit: Int): Single<List<DishEntity>>
    fun changeFavorite(dishId: Int, favorite: Boolean): Single<Boolean>
    fun getRecommended(offset: Int, limit: Int): Single<MutableList<CardItem>>
    //fun getDishes(offset: Int, limit: Int): Single<List<DishEntity>>
    fun getCachedDishes(): Single<List<DishEntity>>
    fun getBestDishes(): Single<MutableList<CardItem>>
    fun getMostLikedDishes(): Single<MutableList<CardItem>>
    fun findDishesByName(searchText: String): Observable<MutableList<CardItem>>
    fun getDishById(id: String): Single<DishEntity?>
}

class DishesRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: IDishesMapper,
    private val dishesDao: DishesDao
) : IDishRepository {

    override fun getCachedSearchHistory(): LiveData<MutableSet<String>> = prefs.searchHistoryLive

//    override fun getDishes(offset: Int, limit: Int): Single<List<DishEntity>> =
//        api.getDishes(offset, limit,
//            "${PrefManager.BEARER} ${prefs.accessToken}")
//            .doOnSuccess { dishes: List<Dish> ->
//                val savePersistDishes: List<DishEntity> = mapper.mapDtoToPersist(dishes)
//                dishesDao.insert(savePersistDishes)
//            }
//            .doOnError {
//
//            }
//            .map { mapper.mapDtoToPersist(it) }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())

    override fun getCachedDishes(): Single<List<DishEntity>> =
        dishesDao.getAllDishes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun findDishesByName(searchText: String): Observable<MutableList<CardItem>> =
        dishesDao.findDishesByName(searchText)
            .map {
                mapper.mapDishToCardItem(it)
            }
            .doOnSuccess {
                prefs.searchHistory.add(searchText)
            }
            .toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getDishById(id: String): Single<DishEntity?> =
        dishesDao.getDishesById(id)
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

    override fun getRecommended(offset: Int, limit: Int): Single<MutableList<CardItem>> {
        return api.getDishes(offset, limit)
            .doOnError {
                throw ConnectException()
            }
            .doOnSuccess { dishes: List<Dish> ->
                val savePersistDishes: List<DishEntity> = mapper.mapDtoToPersist(dishes)
                dishesDao.insert(savePersistDishes)
            }
            .flatMap {
                Single.zip(dishesDao.getAllDishes(), api.getRecommend(), { dish, recom ->
                    mapper.mapDishToCardItem(dish, recom)
                }).onErrorReturn {
                    throw EmptyDishesError()
                }.subscribeOn(Schedulers.io())
            }
            .doOnError {
                throw ConnectException()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getBestDishes(): Single<MutableList<CardItem>>{
        return dishesDao.getAllDishes()
            .onErrorReturn{
                mutableListOf()
            }
            .map {
                var result = mutableListOf<DishEntity>()
                for (item in it){
                    if (item.rating >= 4.8 && result.size < 10){
                        result.add(item)
                    }
                }

                if (result.size < 4){
                    result = mutableListOf()
                }
                mapper.mapDishToCardItem(result)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getMostLikedDishes(): Single<MutableList<CardItem>>{
        return dishesDao.getAllDishes()
            .onErrorReturn{
                mutableListOf()
            }
            .map {
                it.sortedBy { item -> item.likes }
                if(it.size < 4)
                    mutableListOf()
                else
                    mapper.mapDishToCardItem(it).subList(0, 4)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}