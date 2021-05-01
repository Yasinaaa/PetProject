package ru.skillbranch.sbdelivery.data.local.dao

import androidx.room.*
import io.reactivex.rxjava3.core.Single

interface BaseDao<T : Any> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj:T): Single<Long>

    @Update
    fun update(list:List<T>)

    @Update
    fun update(obj:T)

    @Delete
    fun delete(obj:T)
}