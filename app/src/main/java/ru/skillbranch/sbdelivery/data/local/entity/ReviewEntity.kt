package ru.skillbranch.sbdelivery.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_table")
data class ReviewEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "rating") val rating: Float?,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "active") val active: Boolean?,
    @ColumnInfo(name = "createdAt") val createdAt: String?,
    @ColumnInfo(name = "updatedAt") val updatedAt: String?
)
