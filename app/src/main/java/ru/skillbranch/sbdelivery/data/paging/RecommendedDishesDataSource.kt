package ru.skillbranch.sbdelivery.data.paging

import androidx.paging.PagingState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.err.EmptyDishesError
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import java.net.ConnectException


class RecommendedDishesDataSource(
    private val api: RestService,
    private val mapper: IDishesMapper,
    private val dishesDao: DishesDao
) : BaseDataSource() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, CardItem>> {
        page = params.key ?: 1

        return api.getDishes(page, limit)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { dishes: List<Dish> ->
                if (dishes.isNotEmpty()) {
                    val savePersistDishes: List<DishEntity> = mapper.mapDtoToPersist(dishes)
                    dishesDao.insert(savePersistDishes)
                }
            }
            .flatMap { dishesDao.getAllDishes() }
            .zipWith(api.getRecommend(), { dish, recom ->  mapper.mapDishToCardItem(dish, recom)})
            .map {
                LoadResult.Page(
                    data = it,
                    prevKey = if(page == 1) null else page - 1,
                    nextKey = if(it.size < limit)
                        null
                    else page + 1,
                ) as LoadResult<Int, CardItem>
            }
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn{
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                ) as LoadResult<Int, CardItem>
            }
            //
    }
}