package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.dto.DishDto
import ru.skillbranch.sbdelivery.data.local.entity.*
import ru.skillbranch.sbdelivery.data.remote.models.request.CartItemRequest
import ru.skillbranch.sbdelivery.data.remote.models.response.Cart

interface ICartMapper {
    fun mapCartToEntity(cart: Cart): CartEntity
    fun mapItemEntityToItemRequestList(items: List<CartItemEntity>): List<CartItemRequest>
    fun dishDtoPlusCartToCartItemsEntity(cartId: Long, dish: DishEntity, count: Int): CartItemEntity
    fun mapCartToEntity(
        dish: DishEntity, count: Int,
        cartEntityId: Long, cartItemId: Long
    ): CartWithItems
}

open class CartMapper : ICartMapper {

    override fun mapCartToEntity(cart: Cart): CartEntity =
        CartEntity(
            promoCode = cart.promoCode,
            promoText = cart.promoText,
            total = cart.total
        )

//    override fun mapCartToEntity(cart: Cart, cartEntityId: Int): CartWithItems = CartWithItems(
//        cart = CartEntity(
//            id = cartEntityId,
//            promoCode = cart.promoCode,
//            promoText = cart.promoText,
//            total = cart.total),
//        items = cart.items!!.map {
//            CartItemEntity(
//                remoteId= it.id,
//                amount = it.amount,
//                price = it.price,
//                cartId = cartEntityId
//            )
//        }
//    )

    override fun mapCartToEntity(
        dish: DishEntity, count: Int,
        cartEntityId: Long, cartItemId: Long
    ): CartWithItems = CartWithItems(
        cart = CartEntity(
            id = cartEntityId,
            total = dish.price * count
        ),
        items = listOf(
            CartItemEntity(
                id = cartItemId,
                amount = count,
                price = dish.price,
                cartId = cartEntityId
            )
        )
    )

    override fun mapItemEntityToItemRequestList(
        items: List<CartItemEntity>
    ): List<CartItemRequest> =
        items.map {
            CartItemRequest(
                it.remoteId,
                it.amount
            )
        }

    override fun dishDtoPlusCartToCartItemsEntity(
        cartId: Long,
        dish: DishEntity,
        count: Int
    ): CartItemEntity = CartItemEntity(
            amount = count,
            price = dish.price,
            cartId = cartId
        )
}