package ru.skillbranch.sbdelivery.data.repository

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.dto.DishDto
import ru.skillbranch.sbdelivery.data.local.dao.CartDao
import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithItems
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager.Companion.REFRESH_TOKEN
import ru.skillbranch.sbdelivery.data.mapper.ICartMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.request.CartRequest
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.*

interface ICartRepository {
    //fun getCart(): Observable<CartWithItems?>
    fun updateCart(promoCode: String?, items: List<CartWithImageDto>): Observable<CartWithItems?>
    fun getLocalCart(): Observable<CartWithItems?>
    fun saveLocally(dish: DishEntity, count: Int): Observable<Long>
    fun getCurrentCount(dish: DishEntity): Single<Int>
    fun updateCartItem(dish: CartWithImageDto, count: Int): Observable<Long>
    fun removeCartItem(dish: CartWithImageDto): Observable<Long>
}

class CartRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: ICartMapper,
    private val cartDao: CartDao
): ICartRepository {

    override fun getLocalCart(): Observable<CartWithItems?> {
        return cartDao.getCart()
            .toObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn {
                null
            }
            .subscribeOn(Schedulers.io())
    }

//    override fun getCart(): Observable<CartWithItems?> =
//        api.getCart("${PrefManager.BEARER} ${prefs.accessToken}")
//            .map {
//                cartDao.updateCartItem()
//            }
//            .flatMap {
//                getLocalCart()
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())

    override fun updateCart(promoCode: String?, items: List<CartWithImageDto>): Observable<CartWithItems?> {
        val ite = mapper.mapToItemRequestList(items)
        return api.refreshToken(RefreshToken(REFRESH_TOKEN))
            .toObservable()
            .flatMap {
                prefs.accessToken = it.accessToken
                api.updateCart(
                    CartRequest(promoCode, ite),
                    "${PrefManager.BEARER} ${prefs.accessToken}"
                )
            }
            .flatMap {
                getLocalCart()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveLocally(dish: DishEntity, count: Int): Observable<Long> =
       cartDao.insertCart(CartEntity())
           .toObservable()
           .flatMap {
               cartDao.insertCartItems(
                   mapper.dishDtoToCartItemEntity(
                       cartId = 1,
                       dish = dish,
                       count = count
                   )).toObservable()
           }
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())


    override fun getCurrentCount(dish: DishEntity): Single<Int> =
        cartDao.getCartItemCountById(dishId = dish.id)
            .map { it.amount ?: 0 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun updateCartItem(dish: CartWithImageDto, count: Int): Observable<Long> {
        return cartDao.updateCartItem(dish.dishId, count)
            .toObservable()
            .map { it.toLong() }
            .onErrorReturn { 1 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun removeCartItem(dish: CartWithImageDto): Observable<Long> {
        val car = CartItemEntity(
            dishId = dish.dishId,
            remoteId = null,
            amount = dish.amount,
            price = dish.price,
            cartId = dish.cartId
        )
        return cartDao.deleteItem(car)
            .toObservable()
            .map { it.toLong() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}