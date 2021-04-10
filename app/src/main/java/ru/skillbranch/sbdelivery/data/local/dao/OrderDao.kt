package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entity.OrderEntity
import ru.skillbranch.sbdelivery.data.local.entity.OrderItemEntity

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderItems(obj:List<OrderItemEntity>)

    @Query("UPDATE order_table SET statusId=:statusId WHERE id=:orderId")
    fun cancelOrder(statusId: Int, orderId: String)

}