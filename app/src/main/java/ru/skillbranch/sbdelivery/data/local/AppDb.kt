package ru.skillbranch.sbdelivery.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.skillbranch.sbdelivery.BuildConfig
import ru.skillbranch.sbdelivery.data.local.dao.*
import ru.skillbranch.sbdelivery.data.local.entity.*

@Database(entities = [DishEntity::class, CategoryEntity::class, OrderEntity::class,
    ReviewEntity::class, CartEntity::class, CartItemEntity::class, OrderItemEntity::class,
    OrderStatusEntity::class],
    views = [CartWithImage::class],
    version = AppDb.DATABASE_VERSION,
    exportSchema = false)
abstract class AppDb : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }
    abstract fun dishesDao(): DishesDao
    abstract fun categoryDao(): CategoryDao
    abstract fun orderDao(): OrderDao
    abstract fun orderStatusDao(): OrderStatusDao
    abstract fun cartDao(): CartDao
    abstract fun reviewDao(): ReviewDao
}
