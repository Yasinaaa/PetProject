package ru.skillbranch.sbdelivery.data.paging

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem


class BestDishesDataSource(
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
                var result = mutableListOf<DishEntity>()
                for (item in it){
                    if (item.rating >= 4.8 && result.size < 10){
                        result.add(item)
                    }
                }
                if (result.size < 4){
                    result = mutableListOf()
                }

                if (result.isEmpty()){
                    LoadResult.Page<Int, CardItem>(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null,
                    )
                } else {
                    LoadResult.Page(
                        data = mapper.mapDishToCardItem(result),
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (result.size < limit)
                            null
                        else page + 1,
                    ) as LoadResult<Int, CardItem>
                }

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}