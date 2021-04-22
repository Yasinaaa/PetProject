package ru.skillbranch.sbdelivery.ui.main

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.data.repository.IProfileRepository
import ru.skillbranch.sbdelivery.ui.base.Loading
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem

class MainViewModel(
    handle: SavedStateHandle,
    private val repository: IDishRepository,
): BaseViewModel<MainState>(handle, MainState()){

    private val dishes = MutableLiveData<MutableList<RecyclerItem>>()
//    private val bestDishes = MutableLiveData<MutableList<CardItem>>()
//    private val mostLikedDishes = MutableLiveData<MutableList<CardItem>>()

    fun recommendDishesLists(
        owner: LifecycleOwner,
        //isBookmark: Boolean = false,
        onChange: (list: MutableList<RecyclerItem>) -> Unit
    ) {
        showLoading()
        //updateState { it.copy(isLoading = true) }
        dishes.observe(owner, { onChange(it) })

        Single.zip(
            repository.getRecommended(0, 1000),
            repository.getBestDishes(),
            repository.getMostLikedDishes(), {
                recommend, best, liked ->
                val items = mutableListOf<RecyclerItem>()
                if (recommend.isNotEmpty())
                    items.add(RecyclerItem(R.string.recommend, recommend))
                if (best.isNotEmpty())
                    items.add(RecyclerItem(R.string.promotions, best))
                if (liked.isNotEmpty())
                    items.add(RecyclerItem(R.string.popular, liked))
                items
            }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty())
                    Log.d("MainViewModel", "it.isEmpty()")
                else {
                    Log.d("MainViewModel", "it.size=" + it.size)
                    dishes.value = it
                    hideLoading()
                }
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })

//        repository.getRecommended(0, 1000)
//            .observeOn(AndroidSchedulers.mainThread())

    }

//    fun bestDishesLists(
//        owner: LifecycleOwner,
//        //isBookmark: Boolean = false,
//        onChange: (list: MutableList<CardItem>) -> Unit
//    ) {
//        showLoading()
//        //updateState { it.copy(isBookmark = isBookmark) }
//        bestDishes.observe(owner, { onChange(it) })
//
//        repository.getBestDishes()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                if (it.isEmpty())
//                    Log.d("MainViewModel", "it.isEmpty()")
//                else {
//                    Log.d("MainViewModel", "it.size=" + it.size)
//                }
//                bestDishes.value = it
//            }, {
//                Log.d("MainViewModel", "it.error=" + it.message)
//            })
//    }
//
//    fun mostLikedDishesLists(
//        owner: LifecycleOwner,
//        //isBookmark: Boolean = false,
//        onChange: (list: MutableList<CardItem>) -> Unit
//    ) {
//        showLoading()
//        //updateState { it.copy(isBookmark = isBookmark) }
//        mostLikedDishes.observe(owner, { onChange(it) })
//
//        repository.getMostLikedDishes()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                if (it.isEmpty())
//                    Log.d("MainViewModel", "it.isEmpty()")
//                else {
//                    Log.d("MainViewModel", "it.size=" + it.size)
//
//                }
//                mostLikedDishes.value = it
//            }, {
//                Log.d("MainViewModel", "it.error=" + it.message)
//            })
//    }

}

data class MainState(
    val isAuth: Boolean = false,
    val isLoading: Boolean = false,
): IViewModelState