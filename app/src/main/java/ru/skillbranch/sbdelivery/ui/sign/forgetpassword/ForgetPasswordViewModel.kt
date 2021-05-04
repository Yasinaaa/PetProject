package ru.skillbranch.sbdelivery.ui.sign.forgetpassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.repository.ISignUpRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.sign.forgetpassword.ForgetPasswordFragment.Companion.INPUT_CODE_TYPE
import ru.skillbranch.sbdelivery.ui.sign.forgetpassword.ForgetPasswordFragment.Companion.INPUT_EMAIL_TYPE
import ru.skillbranch.sbdelivery.ui.sign.forgetpassword.ForgetPasswordFragment.Companion.INPUT_NEW_PASSWORD_TYPE

class ForgetPasswordViewModel(
    handle: SavedStateHandle,
    private val rep: ISignUpRepository
): BaseViewModel<ForgetPasswordState>(handle, ForgetPasswordState()){

    private val emailOrPassword = MutableLiveData<String>()
    private val repeatPassword = MutableLiveData<String>()
    private var currentType = 0
    private var code = ""
    private var email = ""

    init {
        emailOrPassword.value = ""
        repeatPassword.value = ""
    }

    fun init(code: String, email: String){
        this.code = code
        this.email = email
    }

    fun observeFirstField(emailObs: Observable<String>?, type: Int, isFirstPassword: Boolean? = null){
        currentType = type
        emailObs
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.filter {
                updateState { state -> state.copy(isSuccessField =
                        if (currentType == INPUT_EMAIL_TYPE) {
                            it.isNotEmpty()
                        }else{
                            if (isFirstPassword!!){
                                it.isNotEmpty() && (it == repeatPassword.value!!)
                            }else{
                                it.isNotEmpty() && (emailOrPassword.value == it)
                            }
                        }
                    )
                }
                true
            }
            ?.subscribe({
                if (currentType == INPUT_EMAIL_TYPE)
                    emailOrPassword.value = it
                else if (currentType == INPUT_NEW_PASSWORD_TYPE){
                    if (isFirstPassword!!)
                        emailOrPassword.value = it
                    else
                        repeatPassword.value = it
                }
            }, {
                Log.d("fsfs", it.message!!)
            })
    }

    fun onBtnClick(){
        when(currentType){
            INPUT_EMAIL_TYPE -> {
                sendEmailToRestorePassword()
            }
            INPUT_NEW_PASSWORD_TYPE -> {
                sendNewPasswordToRestorePassword()
            }
        }
    }

    private fun sendEmailToRestorePassword(){
        if (emailOrPassword.value!!.isNotBlank()){
            showLoading()
            rep.recoveryEmail(emailOrPassword.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        val action = ForgetPasswordFragmentDirections.updateView(
                            type = INPUT_CODE_TYPE, email = emailOrPassword.value!!)
                        navigate(NavigationCommand.To(action.actionId, action.arguments))
                        hideLoading()
                    },
                    {
                        //show error
                        hideLoading()
                    }
                )
        }
    }

    private fun sendNewPasswordToRestorePassword(){
        if (emailOrPassword.value!!.isNotBlank() && repeatPassword.value!!.isNotBlank()){
            showLoading()
            rep.recoveryPassword(email, code, emailOrPassword.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        hideLoading()
                        updateState { state -> state.copy(returnBack = "backb") }
                    },
                    {
                        //show error
                        hideLoading()
                    }
                )
        }
    }

    fun observeNumbers(email: String,
                        firstObs: Observable<String>?, secondObs: Observable<String>?,
                       thirdObs: Observable<String>?, forthObs: Observable<String>?){
        this.email = email
        firstObs.create(0,1)
        secondObs.create(1,2)
        thirdObs.create(2,3)
        forthObs.create(3)
    }

    private fun Observable<String>?.create(current: Int, next: Int? = null) {
        this?.filter { it.isNotEmpty() }
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                currentState.code[current] = it
                if(next != null)
                    updateState { state -> state.copy(number = next) }
                else{
                    sendCodeToRestorePassword()
                }
            }, {
                //show error
                hideLoading()
            })
    }

    private fun sendCodeToRestorePassword(){
        val code = currentState.code.filter { !it.isNullOrBlank() }
            .joinToString(",").replace(",","")
        if (code.length == 4 && email.isNotEmpty()){
            showLoading()
            rep.recoveryCode(email, code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        val action = ForgetPasswordFragmentDirections.updateView(
                            type = INPUT_NEW_PASSWORD_TYPE,
                            email = emailOrPassword.value!!,
                            code = code)
                        navigate(NavigationCommand.To(action.actionId, action.arguments))
                        hideLoading()
                    },
                    {
                        //show error
                        val action = ForgetPasswordFragmentDirections.updateView(
                            type = INPUT_NEW_PASSWORD_TYPE,
                            email = emailOrPassword.value!!,
                            code = code)
                        navigate(NavigationCommand.To(action.actionId, action.arguments))
                        hideLoading()
                    }
                )
        }
    }
}

data class ForgetPasswordState(
    val code: MutableList<String?> = MutableList(4){ null },
    val isSuccessField: Boolean = false,
    val number: Int = 0,
    val returnBack: String = "",
): IViewModelState