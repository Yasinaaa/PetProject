package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithItems
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity

@Dao
interface CartDao : BaseDao<CartEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItems(obj:List<CartItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItems(obj:CartItemEntity): Single<Long>

    @Query("SELECT * FROM cart_table, cart_item_table")
    fun getCart(): Single<CartWithItems>

    @Query("SELECT COUNT(id) FROM cart_table WHERE id=1")
    fun getCartItemCount(): Single<Int>
}