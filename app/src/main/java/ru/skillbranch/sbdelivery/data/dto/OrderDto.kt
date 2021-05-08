package ru.skillbranch.sbdelivery.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderDto(
    val id: String,
    val total: Float?,
    val address: String?,
    val statusId: String?,
    val active: Boolean?,
    val completed: Boolean?,
    val createdAt: String?,
    val updatedAt: String?,
    val items: List<OrderItemDto> = listOf(),
    val statusName: String?,
    val statusActive: Boolean?,
): Parcelable

@Parcelize
class OrderItemDto(
    val id: String = "",
    val name: String?,
    val image: String?,
    val amount: Int?,
    val price: Float?,
    val dishId: String?,
    val orderId: String
): Parcelable