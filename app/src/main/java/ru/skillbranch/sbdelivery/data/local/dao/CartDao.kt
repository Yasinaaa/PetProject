package ru.skillbranch.sbdelivery.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.data.local.entity.*

@Dao
interface CartDao : BaseDao<CartEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(obj: CartEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItems(obj:List<CartItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItems(obj:CartItemEntity): Single<Long>

    @Query("SELECT * FROM cart_table, CartWithImage")
    fun getCart(): Single<CartWithItems>

    @Query("SELECT COUNT(id) FROM cart_table WHERE id=1")
    fun getCartItemCount(): Single<Int>

    @Query("SELECT * FROM cart_item_table WHERE dish_id=:dishId")
    fun getCartItemCountById(dishId: String): Single<CartItemEntity>

    @Query("""
        SELECT * FROM CartWithImage
    """)
    fun getCartItems(): Single<List<CartWithImage>>

    @Update
    fun update(obj: CartItemEntity): Single<Int>

    @Delete
    fun deleteItem(obj: CartItemEntity): Single<Int>

    @Query("UPDATE cart_item_table SET amount=:count WHERE dish_id=:dishId")
    fun updateCartItem(dishId: String, count: Int): Single<Int>

    @Query("DELETE FROM cart_item_table WHERE dish_id = :dishId")
    fun removeCartItemByDishId(dishId: String): Single<Int>


}