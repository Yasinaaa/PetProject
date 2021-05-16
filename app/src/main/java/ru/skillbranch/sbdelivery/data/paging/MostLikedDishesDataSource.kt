package ru.skillbranch.sbdelivery.data.paging

import androidx.paging.PagingData
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


class MostLikedDishesDataSource(
    private val mapper: IDishesMapper,
    private val dishesDao: DishesDao
) : BaseDataSource() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, CardItem>> {
        page = params.key ?: 1
        return dishesDao.getAllDishes()
            .onErrorReturn{
                mutableListOf()
            }
            .map {
                it.sortedBy { item -> item.likes }
                val list = if(it.size < 4)
                    emptyList()
                else
                    mapper.mapDishToCardItem(it).subList(0, 4)

                if (list.isEmpty())
                    LoadResult.Error(EmptyDishesError())
                else
                    LoadResult.Page(
                        data = list,
                        prevKey = if(page == 1) null else page - 1,
                        nextKey = if(list.size < limit)
                            null
                        else page + 1,
                    ) as LoadResult<Int, CardItem>
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}