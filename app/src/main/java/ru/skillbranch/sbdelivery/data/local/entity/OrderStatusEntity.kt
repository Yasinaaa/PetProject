package ru.skillbranch.sbdelivery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_status_table")
data class OrderStatusEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "cancelable") val cancelable: Boolean?,
    @ColumnInfo(name = "active") val active: Boolean?,
    @ColumnInfo(name = "createdAt") val createdAt: String?,
    @ColumnInfo(name = "updatedAt") val updatedAt: String?
)