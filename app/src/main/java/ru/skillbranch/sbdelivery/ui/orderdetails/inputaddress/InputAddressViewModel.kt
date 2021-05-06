package ru.skillbranch.sbdelivery.ui.orderdetails.inputaddress

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.repository.IAddressRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import java.util.concurrent.TimeUnit
import ru.skillbranch.sbdelivery.data.remote.models.response.Address

class InputAddressViewModel(
    handle: SavedStateHandle,
    private val rep: IAddressRepository
): BaseViewModel<InputAddressState>(handle, InputAddressState()) {

    fun searchAddress(searchEvent: Observable<String>?) {
        showLoading()
        searchEvent
            ?.delay(2, TimeUnit.SECONDS)
            ?.debounce(500L, TimeUnit.MILLISECONDS)
            ?.filter {
                it.isNotBlank() //&& it.contains(",")
            }
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.flatMap {
                rep.checkAddressInput(it.replace(" ", "").replace(".", "").trim())
            }
            ?.subscribe {
                updateState { state -> state.copy(addresses = mutableListOf(it)) }
                hideLoading()
            }
    }
}

data class InputAddressState(
    val addresses: MutableList<Address> = mutableListOf(),
): IViewModelState