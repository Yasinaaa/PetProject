package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.entity.OrderEntity
import ru.skillbranch.sbdelivery.data.local.entity.OrderItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.OrderWithItems

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderItems(obj:List<OrderItemEntity>)

    @Query("UPDATE order_table SET statusId=:statusId WHERE id=:orderId")
    fun cancelOrder(statusId: String, orderId: String)

    @Query("""
        SELECT * FROM order_table, order_item_table, order_status_table 
        WHERE order_table.id=:orderId
        """)
    fun getOrder(orderId: String): Single<OrderWithItems?>
}