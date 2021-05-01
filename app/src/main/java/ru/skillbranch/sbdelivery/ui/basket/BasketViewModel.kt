package ru.skillbranch.sbdelivery.ui.basket

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.data.repository.ICartRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class BasketViewModel(
    handle: SavedStateHandle,
    private val cartRep: ICartRepository
): BaseViewModel<BasketState>(handle, BasketState()) {


}

data class BasketState(
    val isAuth: Boolean = false,
): IViewModelState