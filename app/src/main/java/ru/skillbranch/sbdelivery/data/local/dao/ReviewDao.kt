package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import ru.skillbranch.sbdelivery.data.local.entity.ReviewEntity

@Dao
interface ReviewDao: BaseDao<ReviewEntity> {
}