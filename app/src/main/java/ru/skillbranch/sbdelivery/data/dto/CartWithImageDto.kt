package ru.skillbranch.sbdelivery.data.dto

data class CartWithImageDto (
    val dishId: String,
    var amount: Int?,
    val price: Float?,
    val cartId: Long,
    val image: String,
    val name: String
)