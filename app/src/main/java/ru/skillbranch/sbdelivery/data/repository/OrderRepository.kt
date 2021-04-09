package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.OrderDao
import ru.skillbranch.sbdelivery.data.local.dao.OrderStatusDao
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithItems
import ru.skillbranch.sbdelivery.data.local.entity.OrderWithItems
import ru.skillbranch.sbdelivery.data.mapper.CartMapper
import ru.skillbranch.sbdelivery.data.mapper.OrderMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.RetrofitProvider
import ru.skillbranch.sbdelivery.data.remote.models.request.OrderRequest
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.Address
import ru.skillbranch.sbdelivery.data.remote.models.response.Cart
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus

interface IOrderRepository {
    fun createOrder(address: String?, entrance: Int?, floor: Int?,
                    apartment: String?, intercom: String?, comment: String?): Single<OrderWithItems>
    fun getOrders(offset: Int, limit: Int): Single<List<OrderWithItems>>
    fun cancelOrder(orderId: String): Single<OrderWithItems>
}

class OrderRepository(
    private val api: RestService,
    private val mapper: OrderMapper,
    private val orderDao: OrderDao,
    private val orderStatusDao: OrderStatusDao
): IOrderRepository {

    override fun createOrder(
        address: String?,
        entrance: Int?,
        floor: Int?,
        apartment: String?,
        intercom: String?,
        comment: String?
    ): Single<OrderWithItems> {
        val token = api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
        val order = token.flatMap {
            api.createOrder(
                OrderRequest(address, entrance, floor, apartment, intercom, comment),
                "${RetrofitProvider.BEARER} ${it.accessToken}"
            )
        }
        val status = order.flatMap {
            orderStatusDao.getStatusById(it.statusId)
        }

        return Single.zip(order, status, { ord, sta ->
                mapper.mapOrderToEntity(ord, sta)
            })
            .doOnSuccess {
                saveOrder(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getOrders(offset: Int, limit: Int): Single<List<OrderWithItems>> {
        val token = api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
        val orders: Single<List<Order>> =
            token.flatMap {
                api.getOrders(offset, limit,
                    "${RetrofitProvider.BEARER} ${it.accessToken}")
            }
        val statuses: Single<List<OrderStatus>> = token.flatMap {
                api.getStatuses("${RetrofitProvider.BEARER} ${it.accessToken}")
            }.doOnSuccess {
                orderStatusDao.insert(mapper.mapOrdersStatusesToEntityList(it))
            }

        return Single.zip(orders, statuses, { ord, sta ->
                mapper.mapOrdersWithStatusesToEntityList(ord, sta)
            })
            .doOnSuccess {
                for (order in it) {
                    saveOrder(order)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun cancelOrder(orderId: String): Single<OrderWithItems> {
        val order = api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
            .flatMap {
                api.cancelOrder(orderId, "${RetrofitProvider.BEARER} ${it.accessToken}")
            }
            .doOnSuccess {
                orderDao.cancelOrder(orderId = orderId, statusId = it.statusId)
            }
        val status = order.flatMap {
            orderStatusDao.getStatusById(it.statusId)
        }
        return Single.zip(order, status, { ord, sta ->
                mapper.mapOrderToEntity(ord, sta)
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun saveOrder(orderWithItems: OrderWithItems){
        orderDao.insert(orderWithItems.order)
        orderDao.insertOrderItems(orderWithItems.items)
    }
}