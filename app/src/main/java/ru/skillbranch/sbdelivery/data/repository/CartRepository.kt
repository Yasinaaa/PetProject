package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.CartDao
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithItems
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.ICartMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.response.*

interface ICartRepository {
    fun getCart(): Single<CartWithItems>
    fun updateCart(promoCode: String, items: List<CartItemEntity>): Single<CartWithItems>
}

class CartRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: ICartMapper,
    private val cartDao: CartDao
): ICartRepository {

    override fun getCart(): Single<CartWithItems> =
        api.getCart("${PrefManager.BEARER} ${prefs.accessToken}")
            .saveCart()


    override fun updateCart(promoCode: String, items: List<CartItemEntity>): Single<CartWithItems> =
        api.updateCart(promoCode, mapper.mapItemEntityToItemRequestList(items),
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .saveCart()

    private fun Single<Cart>.saveCart(): Single<CartWithItems> {
        return doOnSuccess {
                val cartWithItems = mapper.mapCartToEntity(it)
                cartDao.insert(cartWithItems.cart)
                cartDao.insertCartItems(cartWithItems.items)
            }
            .map {
                mapper.mapCartToEntity(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}