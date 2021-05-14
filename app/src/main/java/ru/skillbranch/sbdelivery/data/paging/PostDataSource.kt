package ru.skillbranch.sbdelivery.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.err.EmptyDishesError
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import java.net.ConnectException


class PostDataSource(
    private val api: RestService,
    private val mapper: IDishesMapper,
    private val dishesDao: DishesDao
) : RxPagingSource<Int, CardItem>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, CardItem>> {
        val page = params.key ?: 1
        val limit = 10
        return api.getDishes(page, limit)
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
                }.map {
                    LoadResult.Page(
                        data = it,
                        prevKey = if(page == 1) null else page - 1,
                        nextKey = if(it.size < limit)
                            null
                        else page + 1,
                    ) as LoadResult<Int, CardItem>
                }.onErrorReturn {
                    throw EmptyDishesError() //LoadResult.Error(it)
                }.subscribeOn(Schedulers.io())
            }
            .doOnError {
                throw ConnectException()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getRefreshKey(state: PagingState<Int, CardItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}