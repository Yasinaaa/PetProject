package ru.skillbranch.sbdelivery.ui.menu

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import coil.ImageLoader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Category
import ru.skillbranch.sbdelivery.data.repository.ICategoryRepository
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import java.util.concurrent.TimeUnit

class MenuViewModel(
    handle: SavedStateHandle,
    private val repository: ICategoryRepository
): BaseViewModel<MenuState>(handle, MenuState()){

    private val categories = MutableLiveData<MutableList<CategoryEntity>>()

    init {
        hideLoading()
    }

    fun getCategories(
        owner: LifecycleOwner,
        onChange: (list: MutableList<CategoryEntity>) -> Unit
    ){
        showLoading()
        categories.observe(owner, { onChange(it) })

        repository.getParentCategories(0, 1000)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    Log.d("MainViewModel", "it.size=" + it.size)
                    categories.value = it
                }
                hideLoading()
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })

    }

}

class MenuState(

): IViewModelState
