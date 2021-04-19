package ru.skillbranch.sbdelivery.ui.main

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.data.repository.IProfileRepository
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem

class MainViewModel(
    handle: SavedStateHandle,
    private val repository: IDishRepository,
): BaseViewModel<MainState>(handle, MainState()){

    private val recommendDishes = MutableLiveData<MutableList<CardItem>>()
    private val bestDishes = MutableLiveData<MutableList<CardItem>>()
    private val mostLikedDishes = MutableLiveData<MutableList<CardItem>>()

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

    fun recommendDishesLists(
        owner: LifecycleOwner,
        //isBookmark: Boolean = false,
        onChange: (list: MutableList<CardItem>) -> Unit
    ) {
        showLoading()
        //updateState { it.copy(isBookmark = isBookmark) }
        recommendDishes.observe(owner, { onChange(it) })

        repository.getRecommended(0, 1000)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    Log.d("MainViewModel", "it.size=" + it.size)
                    recommendDishes.value = it
                }
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

    fun bestDishesLists(
        owner: LifecycleOwner,
        //isBookmark: Boolean = false,
        onChange: (list: MutableList<CardItem>) -> Unit
    ) {
        showLoading()
        //updateState { it.copy(isBookmark = isBookmark) }
        bestDishes.observe(owner, { onChange(it) })

        repository.getBestDishes()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    Log.d("MainViewModel", "it.size=" + it.size)
                }
                bestDishes.value = it
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

    fun mostLikedDishesLists(
        owner: LifecycleOwner,
        //isBookmark: Boolean = false,
        onChange: (list: MutableList<CardItem>) -> Unit
    ) {
        showLoading()
        //updateState { it.copy(isBookmark = isBookmark) }
        mostLikedDishes.observe(owner, { onChange(it) })

        repository.getMostLikedDishes()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    Log.d("MainViewModel", "it.size=" + it.size)
                    mostLikedDishes.value = it
                }
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
            })
    }

}

data class MainState(
    val isAuth: Boolean = false
): IViewModelState