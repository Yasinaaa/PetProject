package ru.skillbranch.sbdelivery.ui.orders.createdorder

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.data.dto.OrderItemDto
import ru.skillbranch.sbdelivery.data.repository.IOrderRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState


class CreatedOrderViewModel(
    handle: SavedStateHandle,
    private val rep: IOrderRepository
): BaseViewModel<CreatedOrderState>(handle, CreatedOrderState()) {

    var orderId: String = ""

    fun getOrder(orderId: String){
        this.orderId = orderId
        showLoading()
        rep.getOrder(orderId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateState { state -> state.copy(status = it.statusName ?: "") }
                updateState { state -> state.copy(price = it.total ?: 0f) }
                updateState { state -> state.copy(address = it.address ?: "") }
                updateState { state -> state.copy(date = it.createdAt ?: "") }
                updateState { state -> state.copy(items = it.items.toMutableList()) }
                hideLoading()
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })
    }

    fun cancelOrder(){
        rep.cancelOrder(orderId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                updateState { state -> state.copy(returnBack = true) }
            },{
                updateState { state -> state.copy(returnBack = true) }
            })
    }
}

data class CreatedOrderState(
    val status: String = "",
    val price: Float = 0f,
    val address: String = "",
    val date: String = "",
    val items: MutableList<OrderItemDto> = mutableListOf(),
    val returnBack: Boolean = false,
): IViewModelState