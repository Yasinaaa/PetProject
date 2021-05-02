package ru.skillbranch.sbdelivery.ui.basket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.mapper.ICartMapper
import ru.skillbranch.sbdelivery.data.repository.ICartRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import java.util.concurrent.TimeUnit

class BasketViewModel(
    handle: SavedStateHandle,
    private val cartRep: ICartRepository,
    private val mapper: ICartMapper
): BaseViewModel<BasketState>(handle, BasketState()) {

    val items = MutableLiveData<MutableList<CartWithImageDto>>()
    val cart = MutableLiveData<CartEntity>()

    fun getBasket(){
        cartRep.getLocalCart()
            .observeOn(AndroidSchedulers.mainThread())
//            .doFinally {
//                hideLoading()
//            }
            .subscribe({
                if (it == null)
                    updateState { state -> state.copy(isEmptyCart = true) }
                else {
                    cart.value = it.cart
                    items.value = mapper.mapEntityToDtoList(it.items)
                    hideLoading()
                }
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                updateState { state -> state.copy(isEmptyCart = true) }
            })
    }

    fun updateItem(dishId: CartWithImageDto, numChanger: Observable<Int>?){
        numChanger
            ?.delay(2, TimeUnit.SECONDS)
            ?.debounce(200L, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.flatMap {
                //5ed8da011f071c00465b2027
                if (it <= 0){
                    cartRep.removeCartItem(dishId)
                }else{
                    cartRep.updateCartItem(dishId, it)
                    //cartRep.updateCartItem(dishId, it)
                }
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("MainViewModel", "it=")
            },{
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

    fun addToBasket(dishId: String, count: Int){
        //cartRep.updateCart()
    }

}

data class BasketState(
    val isAuth: Boolean = false,
    val isEmptyCart: Boolean = false,
): IViewModelState