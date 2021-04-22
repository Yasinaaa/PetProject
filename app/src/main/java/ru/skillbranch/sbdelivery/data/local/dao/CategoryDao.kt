package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity

@Dao
interface CategoryDao: BaseDao<CategoryEntity> {

    @Query("SELECT * FROM category_table WHERE name LIKE '%' || :searchText || '%' ORDER BY name ASC")
    fun findCategoriesByName(searchText: String): Observable<MutableList<CategoryEntity>>

}