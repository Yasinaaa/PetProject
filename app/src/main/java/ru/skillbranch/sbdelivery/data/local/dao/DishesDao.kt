package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity

@Dao
interface DishesDao : BaseDao<DishEntity> {

//    @Query("SELECT * FROM dishes_table WHERE name LIKE :searchText ORDER BY name ASC")
//    fun findDishesByName(searchText: String): Single<List<DishEntity>>
//
//    @Query("SELECT * FROM dishes_table")
//    fun getAllDishes(): Single<List<DishEntity>>

}