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
import ru.skillbranch.sbdelivery.data.mapper.ICartMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.response.*

interface ICartRepository {
//    fun getCart(): Single<CartWithItems>
//    fun updateCart(promoCode: String?, items: List<CartItemEntity>): Single<CartWithItems>
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

    //    override fun getCart(): Single<CartWithItems> =
//        api.getCart("${PrefManager.BEARER} ${prefs.accessToken}")
//            .saveCart()
//
//    override fun updateCart(promoCode: String?, items: List<CartItemEntity>): Single<CartWithItems> =
//        api.updateCart(promoCode, mapper.mapItemEntityToItemRequestList(items),
//            "${PrefManager.BEARER} ${prefs.accessToken}")
//            .saveCart()
//
//    private fun Single<Cart>.saveCart(): Single<CartWithItems> {
//        flatMap {
//            val cartEntity = mapper.mapCartToEntity(it)
//            cartDao.insert(cartEntity)
//        }.map {
//            cartDao.insertCartItems()
//        }
//
//        return doOnSuccess {
//            mapper.mapCartToEntity(it)
//
////                val cartWithItems = mapper.mapCartToEntity(it)
////                cartDao.insert(cartWithItems.cart)
////                cartDao.insertCartItems(cartWithItems.items)
//            }
//            .map {
//                mapper.mapCartToEntity(it)
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }

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
            .map {
                it.amount ?: 0
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun updateCartItem(dish: CartWithImageDto, count: Int): Observable<Long> {
        val car = CartItemEntity(
            dishId = dish.dishId,
            remoteId = null,
            amount = count,
            price = dish.price,
            cartId = dish.cartId
        )
        return cartDao.updateCartItem(dish.dishId, count)
            .toObservable()
            .map {
                it.toLong()
            }
            .onErrorReturn {
                1
            }
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
            .map {
                it.toLong()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}