package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.data.local.dao.OrderDao
import ru.skillbranch.sbdelivery.data.local.dao.OrderStatusDao
import ru.skillbranch.sbdelivery.data.local.entity.OrderWithItems
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.IOrderMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.request.CartRequest
import ru.skillbranch.sbdelivery.data.remote.models.request.OrderCancelRequest
import ru.skillbranch.sbdelivery.data.remote.models.request.OrderRequest
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus

interface IOrderRepository {
    fun createOrder(address: String?, entrance: Int?, floor: Int?,
                    apartment: String?, intercom: String?, comment: String?): Single<OrderDto>
    fun getOrders(offset: Int, limit: Int): Single<List<OrderDto>>
    fun getOrder(orderId: String): Single<OrderDto>
    fun cancelOrder(orderId: String): Single<OrderDto>
}

class OrderRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: IOrderMapper,
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
    ): Single<OrderDto> {
        return api.refreshToken(RefreshToken(PrefManager.REFRESH_TOKEN))
            .flatMap {
                prefs.accessToken = it.accessToken
                api.createOrder(
                    OrderRequest(address, entrance, floor, apartment, intercom, comment),
                    "${PrefManager.BEARER} ${prefs.accessToken}"
                )
            }
            .map {
                mapper.mapOrderToDto(it)
            }
            .doOnSuccess {
                saveOrder(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getOrders(offset: Int, limit: Int): Single<List<OrderDto>> {
        val orders: Single<List<Order>> =
            api.refreshToken(RefreshToken(PrefManager.REFRESH_TOKEN))
                .flatMap {
                    prefs.accessToken = it.accessToken
                    api.getOrders(offset, limit,
                        "${PrefManager.BEARER} ${prefs.accessToken}")
                }

        val statuses: Single<List<OrderStatus>> =
            api.getStatuses("${PrefManager.BEARER} ${prefs.accessToken}")
                .doOnSuccess {
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

    override fun getOrder(orderId: String): Single<OrderDto> {
        val orders: Single<List<Order>> =
            api.refreshToken(RefreshToken(PrefManager.REFRESH_TOKEN))
                .flatMap {
                    prefs.accessToken = it.accessToken
                    api.getOrders(0, 100,
                        "${PrefManager.BEARER} ${prefs.accessToken}")
                }

        val statuses: Single<List<OrderStatus>> =
            api.getStatuses("${PrefManager.BEARER} ${prefs.accessToken}")
                .doOnSuccess {
                    orderStatusDao.insert(mapper.mapOrdersStatusesToEntityList(it))
                }

        return Single.zip(orders, statuses, { ord, sta ->
            mapper.mapOrdersWithStatusesToEntityList(ord, sta)
        })
//            .flatMap {
//                orderDao.getOrder(orderId)
//            }TODO
            .map {
                it.forEach {
                    if (it.id == orderId){
                        return@map it
                    }
                }
                it.first()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun cancelOrder(orderId: String): Single<OrderDto> {
        return api.refreshToken(RefreshToken(PrefManager.REFRESH_TOKEN))
            .flatMap {
                prefs.accessToken = it.accessToken
                api.cancelOrder(OrderCancelRequest(orderId), "${PrefManager.BEARER} ${prefs.accessToken}")
            }
            .map {
                orderDao.cancelOrder(orderId = orderId, statusId = it.statusId!!)
                mapper.mapOrderToDto(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun saveOrder(order: OrderDto){
        val entity = mapper.mapOrderDtoToEntity(order)
        orderDao.insert(entity.order)
        orderDao.insertOrderItems(entity.items)
    }
}