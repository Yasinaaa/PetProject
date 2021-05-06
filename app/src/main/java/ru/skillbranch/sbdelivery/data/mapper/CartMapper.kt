package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.dto.DishDto
import ru.skillbranch.sbdelivery.data.local.entity.*
import ru.skillbranch.sbdelivery.data.remote.models.request.CartItemRequest
import ru.skillbranch.sbdelivery.data.remote.models.response.Cart

interface ICartMapper {
    //fun mapCartToEntity(cart: Cart): CartWithItems
    fun mapItemEntityToItemRequestList(items: List<CartItemEntity>): List<CartItemRequest>
    fun mapToItemRequestList(items: List<CartWithImageDto>): List<CartItemRequest>
    fun dishDtoToCartItemEntity(cartId: Long, dish: DishEntity, count: Int): CartItemEntity
    fun mapCartToEntity(
        dish: DishEntity, count: Int,
        cartEntityId: Long
    ): CartWithItems
    fun mapEntityToDtoList(items: List<CartWithImage>): MutableList<CartWithImageDto>
}

open class CartMapper : ICartMapper {

//    override fun mapCartToEntity(cart: Cart): CartWithItems{
//        val cartItemEntity = CartWithItems(
//            cart = CartEntity(
//
//            ),
//            items = cart.items?.map {
//                CartWithImage(
//                    dishId = it.id,
//                    amount = it.amount,
//                    price = it.price,
//                    cartId = 1,
//                    image = it.,
//                    name = it.name
//                )
//            }  ?: listOf()
//        )
//        return cartItemEntity
//    }
//        CartItemEntity(
//            dishId = "",
//            amount = cart.,
//            price = dish.price,
//            cartId = cartId
//        )
//        CartEntity(
//            promoCode = cart.promoCode,
//            promoText = cart.promoText,
//            total = cart.total
//        )

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
        cartEntityId: Long
    ): CartWithItems = CartWithItems(
        cart = CartEntity(
            id = cartEntityId,
            total = dish.price * count
        ),
        items = listOf(

        )
    //CartItemEntity(
        //                dishId = dish.id,
        //                amount = count,
        //                price = dish.price,
        //                cartId = cartEntityId
        //            )
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

    override fun mapToItemRequestList(items: List<CartWithImageDto>): List<CartItemRequest> =
        items.map {
            CartItemRequest(
                it.dishId,
                it.amount
            )
        }

    override fun dishDtoToCartItemEntity(
        cartId: Long,
        dish: DishEntity,
        count: Int
    ): CartItemEntity = CartItemEntity(
            dishId = dish.id,
            amount = count,
            price = dish.price,
            cartId = cartId
        )

    override fun mapEntityToDtoList(items: List<CartWithImage>): MutableList<CartWithImageDto> =
        items.map {
            CartWithImageDto(
                dishId = it.dishId,
                amount = it.amount,
                price = it.price,
                cartId = it.cartId,
                image = it.image,
                name = it.name
            )
        }.toMutableList()

}