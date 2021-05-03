package ru.skillbranch.sbdelivery.ui.sign

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.repository.ISignUpRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.sign.SignFragment.Companion.SIGN_IN
import ru.skillbranch.sbdelivery.ui.sign.SignFragment.Companion.SIGN_UP
import java.util.regex.Pattern

class SignViewModel(
    handle: SavedStateHandle,
    private val rep: ISignUpRepository
): BaseViewModel<SignState>(handle, SignState()){

    companion object{
        const val NAME_TYPE = 0
        const val SURNAME_NAME_TYPE = 1
    }

    private var currentType = SIGN_UP
    private val email = MutableLiveData<String>()
    private val password = MutableLiveData<String>()
    private val name = MutableLiveData<String>()
    private val surname = MutableLiveData<String>()

    fun init() {
        currentType = SIGN_IN
        updateState { state -> state.copy(
                isSuccessPassword = false,
                isSuccessEmail = false
            )
        }
    }

    fun observeEmailField(emailObs: Observable<String>?, type: Int){
        emailObs
            ?.obsWatcher()
            ?.filter {
                val condition = if (type == SIGN_UP)
                    Patterns.EMAIL_ADDRESS.matcher(it).matches()
                else
                    it.isNotEmpty()
                updateState { state -> state.copy(isSuccessEmail = condition) }
                condition
            }
            ?.subscribe({
                email.value = it
            }, {
                Log.d("fsfs", it.message!!)
            })
    }

    fun observePasswordField(passwordObs: Observable<String>?){
        passwordObs
            ?.obsWatcher()
            ?.filter {
                val condition = it.isNotEmpty()
                updateState { state -> state.copy(isSuccessPassword = condition) }
                condition
            }
            ?.subscribe({
                password.value = it
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
                if (type == NAME_TYPE){
                    updateState { state -> state.copy(isSuccessName = condition) }
                }else if (type == SURNAME_NAME_TYPE){
                    updateState { state -> state.copy(isSuccessSurname = condition) }
                }
                condition
            }
            ?.subscribe({
                if (type == NAME_TYPE){
                    this.name.value = it

                }else if (type == SURNAME_NAME_TYPE){
                    surname.value = it
                }
            }, {
                Log.d("fsfs", it.message!!)
            })
    }

    private fun Observable<String>?.obsWatcher(): Observable<String>? =
        this
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())

    fun onSignInClick(){
        if (currentType == SIGN_IN) {
            signIn()
        }else{
            signUp()
        }
    }

    private fun signIn(){
        if (email.value!!.isNotBlank() && password.value!!.isNotBlank()) {
            showLoading()
            rep.login(email.value!!, password.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        updateState { state -> state.copy(returnBack = "back") }
                        hideLoading()
                    },
                    {
                        updateState { state -> state.copy(returnBack = it.message!!) }
                        hideLoading()
                    }
                )
        }
    }

    private fun signUp(){
        if (email.value!!.isNotBlank() && password.value!!.isNotBlank()
            && name.value!!.isNotBlank() && surname.value!!.isNotBlank()) {
            showLoading()
            rep.register(name.value!!, surname.value!!, email.value!!, password.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        updateState { state -> state.copy(returnBack = "backb") }
                        hideLoading()
                    },
                    {
                        updateState { state -> state.copy(returnBack = it.message!!) }
                        hideLoading()
                    }
                )
        }
    }

    fun onSignUpClick(){
        if (currentType == SIGN_IN) {
            openSignUpView()
        }else{
            updateState { state -> state.copy(returnBack = "back") }
        }
    }

    private fun openSignUpView(){
        val action = SignFragmentDirections.signUp(SIGN_UP)
        navigate(NavigationCommand.To(action.actionId, action.arguments))
    }

    fun onForgetPasswordClick(){
        navigate(NavigationCommand.To(SignFragmentDirections.forgetPassword().actionId))
    }
}

data class SignState(
    val isInit: Boolean = true,
    val isAuth: Boolean = false,
    val isSuccessEmail: Boolean = true,
    val isSuccessPassword: Boolean = true,
    val isSuccessName: Boolean = true,
    val isSuccessSurname: Boolean = true,
    val returnBack: String = ""
): IViewModelState