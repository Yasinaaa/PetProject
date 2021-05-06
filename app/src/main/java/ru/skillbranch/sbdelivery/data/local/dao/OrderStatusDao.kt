package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.*

@Dao
interface OrderStatusDao : BaseDao<OrderStatusEntity>{

    @Query("SELECT * FROM order_status_table WHERE id=:statusId")
    fun getStatusById(statusId: String): Single<OrderStatusEntity>

}