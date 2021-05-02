package ru.skillbranch.sbdelivery.ui.basket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.dto.CartDto
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.mapper.ICartMapper
import ru.skillbranch.sbdelivery.data.repository.ICartRepository
import ru.skillbranch.sbdelivery.data.repository.IProfileRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.basket.BasketFragmentDirections
import java.util.concurrent.TimeUnit

class BasketViewModel(
    handle: SavedStateHandle,
    profRep: IProfileRepository,
    private val cartRep: ICartRepository,
    private val mapper: ICartMapper
): BaseViewModel<BasketState>(handle, BasketState()) {

    val cart = MutableLiveData<CartDto>()

    init {
        subscribeOnDataSource(profRep.isAuth()) { isAuth, state ->
            state.copy(isAuth = isAuth)
        }
    }

    fun getBasket(){
        cartRep.getLocalCart()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == null)
                    updateState { state -> state.copy(isEmptyCart = true) }
                else {
                    cart.value = CartDto(it.cart, mapper.mapEntityToDtoList(it.items))
                }
                hideLoading()
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                updateState { state -> state.copy(isEmptyCart = true) }
                hideLoading()
            })
    }

    fun updateItem(dishId: CartWithImageDto, numChanger: Observable<Int>?){
        numChanger
            ?.delay(2, TimeUnit.SECONDS)
            ?.debounce(200L, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.flatMap {
                if (it <= 0){
                    cartRep.removeCartItem(dishId)
                }else{
                    cartRep.updateCartItem(dishId, it)
                }
            }
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                Log.d("MainViewModel", "it=")
            },{
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

    fun createOrder(){
        if (currentState.isAuth){

        }else{
            openLogInPage()
        }
    }

    private fun openLogInPage(){
        navigate(NavigationCommand.To(BasketFragmentDirections.signPage().actionId))
    }
}

data class BasketState(
    val isAuth: Boolean = false,
    val isEmptyCart: Boolean = false,
): IViewModelState