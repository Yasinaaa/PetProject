package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity

@Dao
interface CategoryDao: BaseDao<CategoryEntity> {
}