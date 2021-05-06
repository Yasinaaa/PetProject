package ru.skillbranch.sbdelivery.ui.orderdetails.map

import android.Manifest
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.remote.models.response.Address
import ru.skillbranch.sbdelivery.data.repository.IAddressRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Notify
import java.util.concurrent.TimeUnit

class MapAddressViewModel(
    handle: SavedStateHandle,
    private val rep: IAddressRepository
): BaseViewModel<MapAddressState>(handle, MapAddressState()) {

    private val storagePermissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun handlePermission(permissionsResult: Map<String, Pair<Boolean, Boolean>>) {

        val isAllGrantad = !permissionsResult.values.map { it.first }.contains(false)
        val isAllMayBeShown = !permissionsResult.values.map { it.second }.contains(false)

        when {
            //if all permissions granted execute action
            isAllGrantad ->
                updateState { state -> state.copy(currentLocation = true) }
            //if request permission not may be shown(dont ask again check) show app settings for manual permission

            !isAllMayBeShown -> Log.d("fd", "sdsd")//executeOpenSettings()

            //else retry request permission
            else -> {
                val msg = Notify.ErrorMessage(
                    "Need permissions for storage",
                    "Retry"
                ) { requestPermissions(storagePermissions) }
                notify(msg)
            }
        }
    }

    fun checkLocationPermission(){
        hideLoading()
        requestPermissions(storagePermissions)
    }

    //make hot observer
    fun searchAddress(location: LatLng) {
        Observable.just(location)
            .delay(2, TimeUnit.SECONDS)
            .debounce(500L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                rep.checkAddressCoordinates(it.latitude, it.longitude)
            }
            .subscribe({
                updateState { state ->
                    state.copy(currentAddress = it)
                }
                hideLoading()
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })
    }

    fun onBtnClick(){
        updateState { it.copy(returnBack = true) }
    }
}
data class MapAddressState(
    val currentLocation: Boolean = false,
    val currentAddress: Address = Address(),
    val returnBack: Boolean = false
): IViewModelState