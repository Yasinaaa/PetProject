package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.OrderDao
import ru.skillbranch.sbdelivery.data.local.dao.OrderStatusDao
import ru.skillbranch.sbdelivery.data.local.entity.OrderWithItems
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.IOrderMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.request.OrderRequest
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus

interface IOrderRepository {
    fun createOrder(address: String?, entrance: Int?, floor: Int?,
                    apartment: String?, intercom: String?, comment: String?): Single<OrderWithItems>
    fun getOrders(offset: Int, limit: Int): Single<List<OrderWithItems>>
    fun cancelOrder(orderId: String): Single<OrderWithItems>
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
    ): Single<OrderWithItems> {
        val order = api.createOrder(
                OrderRequest(address, entrance, floor, apartment, intercom, comment),
                "${PrefManager.BEARER} ${prefs.accessToken}"
            )
        val status = order.flatMap {
            orderStatusDao.getStatusById(it.statusId!!)
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
        val orders: Single<List<Order>> = api.getOrders(offset, limit,
                "${PrefManager.BEARER} ${prefs.accessToken}")
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

    override fun cancelOrder(orderId: String): Single<OrderWithItems> {
        val order =
            api.cancelOrder(orderId, "${PrefManager.BEARER} ${prefs.accessToken}")
                .doOnSuccess {
                    orderDao.cancelOrder(orderId = orderId, statusId = it.statusId!!)
                }
        val status = order.flatMap {
            orderStatusDao.getStatusById(it.statusId!!)
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