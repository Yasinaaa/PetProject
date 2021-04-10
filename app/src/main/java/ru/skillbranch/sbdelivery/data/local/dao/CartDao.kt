package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity

@Dao
interface CartDao : BaseDao<CartEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItems(obj:List<CartItemEntity>)

}