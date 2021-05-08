package ru.skillbranch.sbdelivery.ui.orders

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.data.repository.IOrderRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class OrdersViewModel(
    handle: SavedStateHandle,
    private val rep: IOrderRepository
): BaseViewModel<OrderState>(handle, OrderState()) {

    fun getOrders(){
        showLoading()
        val allOrdersObs = rep.getOrders(0, 100)

        allOrdersObs
            .map {
                updateState { state ->
                    state.copy(notCompleted = filterList(it, true))
                }
                updateState { state ->
                    state.copy(completed = filterList(it, false))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                hideLoading()
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })
    }

    private fun filterList(list: List<OrderDto>, condition: Boolean): MutableList<OrderDto>{
        return list.filter { order ->
            order.completed == condition
        }.toMutableList()
    }

}

data class OrderState(
    val completed: MutableList<OrderDto> = mutableListOf(),
    val notCompleted: MutableList<OrderDto> = mutableListOf()
): IViewModelState