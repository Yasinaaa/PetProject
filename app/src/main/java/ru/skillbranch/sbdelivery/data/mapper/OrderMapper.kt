package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.data.dto.OrderItemDto
import ru.skillbranch.sbdelivery.data.local.entity.*
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus

interface IOrderMapper {
    fun mapOrderToDto(order: Order, statusName: String? = null, statusActive: Boolean? = null): OrderDto
    fun mapOrderToDto(order: OrderWithItems): OrderDto
    fun mapOrdersWithStatusesToEntityList(
        orders: List<Order>, statuses: List<OrderStatus>): List<OrderDto>
    fun mapOrdersStatusesToEntityList(statuses: List<OrderStatus>): List<OrderStatusEntity>
    fun mapOrderDtoToEntity(order: OrderDto): OrderWithItems
}

open class OrderMapper : IOrderMapper {

    override fun mapOrderToDto(order: OrderWithItems): OrderDto =
        OrderDto(
            id = order.order.id,
            address = order.order.address,
            statusId = order.order.statusId,
            active = order.order.active,
            completed = order.order.completed,
            createdAt = order.order.createdAt,
            updatedAt = order.order.updatedAt,
            total = order.order.total,
            items = order.items.map {
                OrderItemDto(
                    name = it.name,
                    image = it.image,
                    amount = it.amount,
                    price = it.price,
                    dishId = it.dishId,
                    orderId = order.order.id
                )
            },
            statusName = order.status.name,
            statusActive = order.status.active
        )

    override fun mapOrderDtoToEntity(order: OrderDto): OrderWithItems =
        OrderWithItems(
            order = OrderEntity(
                id = order.id,
                address = order.address,
                statusId = order.statusId,
                active = order.active,
                completed = order.completed,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt,
                total = order.total
            ),
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
            status = OrderStatusEntity(
                id = order.statusId ?: "",
                name = order.statusName,
                active = order.active,
                cancelable = false,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt,
            )
        )

    override fun mapOrderToDto(order: Order, statusName: String?, statusActive: Boolean?): OrderDto =
        OrderDto(
            id = order.id,
            address = order.address,
            statusId = order.statusId,
            active = order.active,
            completed = order.completed,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt,
            total = order.total,
            items = order.items.map {
                OrderItemDto(
                    name = it.name,
                    image = it.image,
                    amount = it.amount,
                    price = it.price,
                    dishId = it.dishId,
                    orderId = order.id
                )
            },
            statusName = statusName,
            statusActive = statusActive
        )

    override fun mapOrdersWithStatusesToEntityList(
        orders: List<Order>,
        statuses: List<OrderStatus>
    ): List<OrderDto> {
        val list = mutableListOf<OrderDto>()
        for (order in orders){
            for (status in statuses){
                if (order.statusId == status.id){
                    val orderDto = mapOrderToDto(order, status.name, status.active)
                    list.add(orderDto)
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