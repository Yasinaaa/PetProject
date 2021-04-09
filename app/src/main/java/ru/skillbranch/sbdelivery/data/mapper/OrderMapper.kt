package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.*
import ru.skillbranch.sbdelivery.data.remote.models.request.CartItemRequest
import ru.skillbranch.sbdelivery.data.remote.models.response.Cart
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus
import ru.skillbranch.sbdelivery.data.remote.models.response.Review

interface IOrderMapper {
    fun mapOrderToEntity(order: Order, status: OrderStatusEntity): OrderWithItems
    fun mapOrdersWithStatusesToEntityList(
        orders: List<Order>, statuses: List<OrderStatus>): List<OrderWithItems>
    fun mapOrdersStatusesToEntityList(statuses: List<OrderStatus>): List<OrderStatusEntity>
}

open class OrderMapper : IOrderMapper {

    override fun mapOrderToEntity(order: Order, status: OrderStatusEntity): OrderWithItems =
        OrderWithItems(
            order =
            OrderEntity(
                id = order.id,
                address = order.address,
                statusId = order.statusId,
                active = order.active,
                completed = order.completed,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt,
                total = order.total),
            items = order.items.map {
                OrderItemEntity(
                    name = it.name,
                    image = it.image,
                    amount = it.amount,
                    price = it.price,
                    dishId = it.dishId,
                    orderId = order.id
                )
            },
            status = status
    )

    override fun mapOrdersWithStatusesToEntityList(
        orders: List<Order>,
        statuses: List<OrderStatus>
    ): List<OrderWithItems> {
        val list = listOf<OrderWithItems>()
        for (order in orders){
            for (status in statuses){
                if (order.statusId == status.id){
                    list.plus(mapOrderToEntity(order,
                        OrderStatusEntity(
                            id = status.id,
                            cancelable = status.cancelable,
                            name = status.name,
                            active = status.active,
                            createdAt = status.createdAt,
                            updatedAt = status.updatedAt)))
                }
            }
        }
        return list
    }

    override fun mapOrdersStatusesToEntityList(
        statuses: List<OrderStatus>)
    : List<OrderStatusEntity> =
        statuses.map {
            OrderStatusEntity(
                id = it.id,
                cancelable = it.cancelable,
                name = it.name,
                active = it.active,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
}