package ru.skillbranch.sbdelivery.ui.main


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.repository.IDishRepository

class MainViewModel(
    handle: SavedStateHandle,
    private val repository: IDishRepository,
    private val dishesMapper: IDishesMapper,
    //private val notifier: BasketNotifier
): BaseViewModel<MainState>(handle, MainState()){

    fun getFavorite(){
        repository.getCategories(0, 100)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    Log.d("MainViewModel", "it.isEmpty()")
                else
                    Log.d("MainViewModel", "it.size=" + it.size)
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

}

data class MainState(
    val isAuth: Boolean = false
): IViewModelState