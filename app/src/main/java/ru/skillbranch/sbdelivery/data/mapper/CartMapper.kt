package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.*
import ru.skillbranch.sbdelivery.data.remote.models.request.CartItemRequest
import ru.skillbranch.sbdelivery.data.remote.models.response.Cart
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus
import ru.skillbranch.sbdelivery.data.remote.models.response.Review

interface ICartMapper {
    fun mapCartToEntity(cart: Cart): CartWithItems
    fun mapItemEntityToItemRequestList(items: List<CartItemEntity>): List<CartItemRequest>
}

open class CartMapper : ICartMapper {

    override fun mapCartToEntity(cart: Cart): CartWithItems = CartWithItems(
        cart = CartEntity(
            promoCode = cart.promoCode,
            promoText = cart.promoText,
            total = cart.total),
        items = cart.items!!.map {
            CartItemEntity(
                id= it.id,
                amount = it.amount,
                price = it.price,
                cartId = null
            )
        }
    )

    override fun mapItemEntityToItemRequestList(
        items: List<CartItemEntity>
    ): List<CartItemRequest> =
        items.map {
            CartItemRequest(
                it.id,
                it.amount
            )
        }
}