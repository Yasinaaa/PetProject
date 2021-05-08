package ru.skillbranch.sbdelivery.ui.profile

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.repository.IProfileRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.sign.SignFragment
import ru.skillbranch.sbdelivery.ui.sign.SignViewModel
import java.util.regex.Pattern

class ProfileViewModel(
    handle: SavedStateHandle,
    private val rep: IProfileRepository
): BaseViewModel<ProfileState>(handle, ProfileState()) {

    init {
        subscribeOnDataSource(rep.getCachedProfile()) { profile, state ->
            profile ?: return@subscribeOnDataSource null
            state.copy(
                name = profile.firstName,
                surname = profile.lastName,
                email = profile.email
            )
        }
    }

    fun observeEmailField(emailObs: Observable<String>?){
        emailObs
            ?.obsWatcher()
            ?.filter {
                val condition = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                updateState { state -> state.copy(isSuccessEmail = condition) }
                condition
            }
            ?.subscribe({
                updateState { state -> state.copy(email = it) }
            }, {
                Log.d("fsfs", it.message!!)
            })
    }

    fun observeNameField(nameObs: Observable<String>?, type: Int){
        nameObs
            ?.obsWatcher()
            ?.skip(1)
            ?.filter {
                val condition = Pattern.compile("\\p{L}+").matcher(it).matches()
                if (type == SignViewModel.NAME_TYPE){
                    updateState { state -> state.copy(isSuccessName = condition) }
                }else if (type == SignViewModel.SURNAME_NAME_TYPE){
                    updateState { state -> state.copy(isSuccessSurname = condition) }
                }
                condition
            }
            ?.subscribe({
                if (type == SignViewModel.NAME_TYPE){
                    updateState { state -> state.copy(name = it) }
                }else if (type == SignViewModel.SURNAME_NAME_TYPE){
                    updateState { state -> state.copy(surname = it) }
                }
            }, {
                Log.d("fsfs", it.message!!)
            })
    }

    private fun Observable<String>?.obsWatcher(): Observable<String>? =
        this
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())

    fun editProfile(){
        if (currentState.email.isNotBlank() && currentState.name.isNotBlank()
            && currentState.surname.isNotBlank()) {
            showLoading()
            rep.putProfile(currentState.name, currentState.surname, currentState.email)
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

    fun changePassword(){
        navigate(NavigationCommand.To(ProfileFragmentDirections.changePasswordDialog().actionId))
    }
}

data class ProfileState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val isSuccessEmail: Boolean = true,
    val isSuccessName: Boolean = true,
    val isSuccessSurname: Boolean = true,
): IViewModelState