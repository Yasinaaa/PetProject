package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.CartDao
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithItems
import ru.skillbranch.sbdelivery.data.mapper.CartMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.RetrofitProvider
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.*

interface ICartRepository {
    fun getCart(): Single<CartWithItems>
    fun updateCart(promoCode: String, items: List<CartItemEntity>): Single<CartWithItems>
}

class CartRepository(
    private val api: RestService,
    private val mapper: CartMapper,
    private val cartDao: CartDao
): ICartRepository {

    override fun getCart(): Single<CartWithItems> =
        api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
            .flatMap {
                api.getCart("${RetrofitProvider.BEARER} ${it.accessToken}")
            }
            .saveCart()


    override fun updateCart(promoCode: String, items: List<CartItemEntity>): Single<CartWithItems> =
        api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
            .flatMap {
                api.updateCart(promoCode, mapper.mapItemEntityToItemRequestList(items),
                    "${RetrofitProvider.BEARER} ${it.accessToken}")
            }
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