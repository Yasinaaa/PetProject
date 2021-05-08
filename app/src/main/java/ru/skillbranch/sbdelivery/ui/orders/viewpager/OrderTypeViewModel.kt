package ru.skillbranch.sbdelivery.ui.orders.viewpager

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.orderdetails.OrderDetailsFragmentDirections
import ru.skillbranch.sbdelivery.ui.orders.OrdersFragment
import ru.skillbranch.sbdelivery.ui.orders.OrdersFragmentDirections

class OrderTypeViewModel(
    handle: SavedStateHandle,
): BaseViewModel<OrderTypeState>(handle, OrderTypeState()) {

    fun openOrderPage(orderId: String){
        val action = OrdersFragmentDirections.orderPage(orderId)
        navigate(NavigationCommand.To(action.actionId, action.arguments))
    }

}

class OrderTypeState: IViewModelState