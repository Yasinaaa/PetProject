package ru.skillbranch.sbdelivery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey @ColumnInfo(name = "id") val categoryId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "icon") val icon: String,
    @ColumnInfo(name = "parent") val parent: Int,
    @ColumnInfo(name = "active") val active: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: String,
    @ColumnInfo(name = "updatedAt") val updatedAt: String,
)