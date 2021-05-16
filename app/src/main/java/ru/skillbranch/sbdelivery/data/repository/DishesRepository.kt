package ru.skillbranch.sbdelivery.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.paging.BestDishesDataSource
import ru.skillbranch.sbdelivery.data.paging.MostLikedDishesDataSource
import ru.skillbranch.sbdelivery.data.paging.RecommendedDishesDataSource
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.err.ApiError
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem

interface IDishRepository {
    fun getCachedSearchHistory(): LiveData<MutableSet<String>>
    fun getFavorite(offset: Int, limit: Int): Single<List<DishEntity>>
    fun changeFavorite(dishId: Int, favorite: Boolean): Single<Boolean>
    fun getAllDishes(): Single<List<Dish>>
    fun getRecommended(): Flowable<PagingData<CardItem>>
    fun getCachedDishes(): Single<List<DishEntity>>
    fun getBestDishes(): Flowable<PagingData<CardItem>>
    fun getMostLikedDishes(): Flowable<PagingData<CardItem>>
    fun findDishesByName(searchText: String): Observable<MutableList<CardItem>>
    fun getDishById(id: String): Single<DishEntity?>
}

class DishesRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: IDishesMapper,
    private val dishesDao: DishesDao,

    private val recommendedDishesDataSource: RecommendedDishesDataSource,
    private val mostLikedDishesDataSource: MostLikedDishesDataSource,
    private val bestDishesDataSource: BestDishesDataSource
) : IDishRepository {

    override fun getCachedSearchHistory(): LiveData<MutableSet<String>> = prefs.searchHistoryLive

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

    override fun getAllDishes(): Single<List<Dish>> {
        return api.getDishes()
            .doOnSuccess { dishes: List<Dish> ->
                if (dishes.isNotEmpty()) {
                    val savePersistDishes: List<DishEntity> = mapper.mapDtoToPersist(dishes)
                    dishesDao.insert(savePersistDishes)
                }
            }
            .onErrorReturn{
                throw ApiError.ConnectionError()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getRecommended(): Flowable<PagingData<CardItem>> {
        return Pager(PagingConfig(pageSize = 20)) {
            recommendedDishesDataSource
        }.flowable
    }

    override fun getBestDishes(): Flowable<PagingData<CardItem>>{
        return Pager(PagingConfig(pageSize = 20)) {
            bestDishesDataSource
        }.flowable
    }

    override fun getMostLikedDishes(): Flowable<PagingData<CardItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { mostLikedDishesDataSource }
        ).flowable
    }
}