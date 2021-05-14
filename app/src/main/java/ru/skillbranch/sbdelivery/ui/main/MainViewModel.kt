package ru.skillbranch.sbdelivery.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Notify
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem

class MainViewModel(
    handle: SavedStateHandle,
    private val useCase: IMainUseCase
    //private val rep: IDishRepository
): BaseViewModel<MainState>(handle, MainState()){


    private val dishes = MutableLiveData<MutableList<RecyclerItem>>()
    val dish = MutableLiveData<PagingData<RecyclerItem>>()

    init {

    }

    fun recommendDishesLists(
        owner: LifecycleOwner,
        onChange: (list: MutableList<RecyclerItem>) -> Unit
    ) {


        dishes.observe(owner, { onChange(it) })
//
        useCase.getFields(dishes){
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