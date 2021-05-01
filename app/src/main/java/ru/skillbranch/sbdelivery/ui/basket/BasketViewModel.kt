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

    //todo empty state, loading, finish, has sale
    fun getBasket(){
        cartRep.getLocalCart()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                hideLoading()
            }
            .subscribe({
                if (it == null)
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    //dishes.value = it
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