package ru.skillbranch.sbdelivery.data.repository.contract

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Category

interface DishRepositoryContract {
    fun getDishes(): Single<List<DishEntity>>
    fun getCachedDishes(): Single<List<DishEntity>>
    fun getCategories(): Single<List<Category>>
    fun findDishesByName(searchText: String): Observable<List<DishEntity>>
}