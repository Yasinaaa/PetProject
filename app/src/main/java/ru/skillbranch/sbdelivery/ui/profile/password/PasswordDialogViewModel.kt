package ru.skillbranch.sbdelivery.ui.profile.password

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.repository.IProfileRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class PasswordDialogViewModel(
    handle: SavedStateHandle,
    private val rep: IProfileRepository
): BaseViewModel<PasswordDialogState>(handle, PasswordDialogState()) {

    companion object{
        const val OLD_PASSWORD = 0
        const val NEW_PASSWORD = 1
    }

    fun observePasswordField(passwordObs: Observable<String>?, type: Int){
        passwordObs
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.filter {
                val condition = it.isNotEmpty()
                if (type == OLD_PASSWORD) {
                    updateState { state -> state.copy(isSuccessOldPassword = condition) }
                }else if (type == NEW_PASSWORD) {
                    updateState { state -> state.copy(isSuccessNewPassword = condition) }
                }
                condition
            }
            ?.subscribe({
                if (type == OLD_PASSWORD) {
                    updateState { state -> state.copy(oldPassword = it) }
                }else if (type == NEW_PASSWORD) {
                    updateState { state -> state.copy(newPassword = it) }
                }
            }, {
                Log.d("fsfs", it.message!!)
            })
    }

    fun changePassword(){
        if (currentState.newPassword.isNotBlank() && currentState.oldPassword.isNotBlank()) {
            showLoading()
            rep.changePassword(newPassword = currentState.newPassword,
                oldPassword = currentState.oldPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        hideLoading()
                    },
                    {
                        hideLoading()
                    }
                )
        }
    }

}

data class PasswordDialogState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val isSuccessOldPassword: Boolean = false,
    val isSuccessNewPassword: Boolean = false,
): IViewModelState