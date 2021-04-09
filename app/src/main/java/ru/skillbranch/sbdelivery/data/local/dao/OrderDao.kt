package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import ru.skillbranch.sbdelivery.data.local.entity.OrderEntity

@Dao
interface OrderDao : BaseDao<OrderEntity> {
}