package ru.skillbranch.sbdelivery.ui.orderdetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.data.dto.CartDto
import ru.skillbranch.sbdelivery.data.repository.IOrderRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.basket.BasketFragmentDirections

class OrderDetailsViewModel(
    handle: SavedStateHandle,
    private val rep: IOrderRepository
): BaseViewModel<OrderDetailsState>(handle, OrderDetailsState()) {

    val address = MutableLiveData<String?>()
    val entrance = MutableLiveData<String?>()
    val floor = MutableLiveData<String?>()
    val apartment = MutableLiveData<String?>()
    val intercom = MutableLiveData<String?>()
    val comment = MutableLiveData<String?>()


    fun onWriteOrEditBtnClick(){
        navigate(NavigationCommand.To(OrderDetailsFragmentDirections.inputAddressPage().actionId))
    }

    fun onShowOnMapBtnClick(){
        navigate(NavigationCommand.To(OrderDetailsFragmentDirections.mapPage().actionId))
    }

    fun onCreateOrderBtnClick(){
        if (address.value!!.isNotEmpty()){
            showLoading()
            rep.createOrder(
                address = address.value, entrance = entrance.value?.toInt(),
                floor = floor.value?.toInt(), apartment = apartment.value,
                intercom = intercom.value, comment = comment.value
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    openOrderPage(orderId = it.id)
                    hideLoading()
                }, {
                    Log.d("MainViewModel", "it.error=" + it.message)

                    hideLoading()
                })
        }
    }

    private fun openOrderPage(orderId: String){
        val action = OrderDetailsFragmentDirections.orderPage(orderId)
        navigate(NavigationCommand.To(action.actionId, action.arguments))
    }
}

data class OrderDetailsState(
    val isAddressEmpty: Boolean = false,
): IViewModelState