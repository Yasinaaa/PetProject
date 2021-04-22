package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity

@Dao
interface DishesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<DishEntity>)

    @Query("SELECT * FROM dishes_table WHERE id IN (:ids)")
    fun findDishesByIds(ids: List<String>): Single<List<DishEntity>>

    @Query("SELECT * FROM dishes_table WHERE name LIKE '%' || :searchText || '%' ") // ORDER BY name ASC
    fun findDishesByName(searchText: String): Single<List<DishEntity>>

    @Query("UPDATE dishes_table SET favorite=1 WHERE id IN (:ids)")
    fun updateFavorite(ids: List<String>)

    @Query("UPDATE dishes_table SET favorite=0 WHERE id NOT IN (:ids)")
    fun updateNotFavorite(ids: List<String>)

    @Query("SELECT * FROM dishes_table")
    fun getAllDishes(): Single<List<DishEntity>>

    @Query("SELECT * FROM dishes_table WHERE favorite=1")
    fun getFavoriteDishes(): Single<List<DishEntity>>

}