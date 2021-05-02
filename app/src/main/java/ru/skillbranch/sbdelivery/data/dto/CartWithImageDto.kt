package ru.skillbranch.sbdelivery.data.dto

import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithImage

data class CartDto(
    val cart: CartEntity,
    val items: MutableList<CartWithImageDto>
)

data class CartWithImageDto (
    val dishId: String,
    var amount: Int?,
    val price: Float?,
    val cartId: Long,
    val image: String,
    val name: String
)