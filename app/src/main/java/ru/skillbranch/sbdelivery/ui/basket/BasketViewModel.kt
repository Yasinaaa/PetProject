package ru.skillbranch.sbdelivery.ui.basket

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.data.repository.ICartRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class BasketViewModel(
    handle: SavedStateHandle,
    private val cartRep: ICartRepository
): BaseViewModel<BasketState>(handle, BasketState()) {

    fun getBasket(){
        cartRep.getLocalCart()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == null)
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    //Log.d("MainViewModel", "it.size=" + it.size)
                    //dishes.value = it
                    hideLoading()
                }
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })
    }

    fun addToBasket(dishId: String, count: Int){
        //cartRep.updateCart()
    }

}

data class BasketState(
    val isAuth: Boolean = false,
): IViewModelState