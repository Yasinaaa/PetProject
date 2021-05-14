package ru.skillbranch.sbdelivery.ui.main

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.data.repository.IProfileRepository
import ru.skillbranch.sbdelivery.ui.base.Loading
import ru.skillbranch.sbdelivery.ui.base.Notify
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem
import java.io.File

class MainViewModel(
    handle: SavedStateHandle,
    private val useCase: IMainUseCase
): BaseViewModel<MainState>(handle, MainState()){

    private val dishes = MutableLiveData<MutableList<RecyclerItem>>()

    fun recommendDishesLists(
        owner: LifecycleOwner,
        onChange: (list: MutableList<RecyclerItem>) -> Unit
    ) {
        dishes.observe(owner, { onChange(it) })

        useCase.getFields(){
            notify(Notify.ErrorMessage(it ?: "", null,null))
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        dishes.value = mutableListOf()
                    }else {
                        dishes.value = it
                        hideLoading()
                    }
                }, {
                    dishes.value = mutableListOf()
                    notify(Notify.ErrorMessage(it.message ?: "", null,null))
                }
            )
    }
}

class MainState: IViewModelState