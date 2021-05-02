package ru.skillbranch.sbdelivery.ui.basket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.data.mapper.ICartMapper
import ru.skillbranch.sbdelivery.data.repository.ICartRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class BasketViewModel(
    handle: SavedStateHandle,
    private val cartRep: ICartRepository,
    private val mapper: ICartMapper
): BaseViewModel<BasketState>(handle, BasketState()) {

    val items = MutableLiveData<MutableList<CartWithImageDto>>()
    val cart = MutableLiveData<CartEntity>()

    //todo empty state, loading, finish, has sale
    fun getBasket(){
        showLoading()
        cartRep.getLocalCart()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                hideLoading()
            }
            .subscribe({
                if (it == null)
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    cart.value = it.cart
                    items.value = mapper.mapEntityToDtoList(it.items)
                    hideLoading()
                }
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

    fun addToBasket(dishId: String, count: Int){
        //cartRep.updateCart()
    }

}

data class BasketState(
    val isAuth: Boolean = false,
): IViewModelState