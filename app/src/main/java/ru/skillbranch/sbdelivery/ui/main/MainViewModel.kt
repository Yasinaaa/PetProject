package ru.skillbranch.sbdelivery.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava3.cachedIn
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Notify
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class MainViewModel(
    handle: SavedStateHandle,
    private val rep: IDishRepository,
): BaseViewModel<MainState>(handle, MainState()){

    val dish = MutableLiveData<RecyclerItem>()

    fun getJobs() {
        rep.getAllDishes()
            .toFlowable()
            .onErrorReturn {
                notify(Notify.Error(error = R.string.network_is_unavailable))
                emptyList()
            }
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                rep.getRecommended()
            }
            .cachedIn(viewModelScope)
            .map {
                dish.value = RecyclerItem(R.string.recommend, it)
            }
            .flatMap {
                rep.getBestDishes()
            }
            .doOnNext {
                dish.value = RecyclerItem(R.string.best, it)
            }
            .flatMap {
                rep.getMostLikedDishes()
            }
            .subscribe ({
                dish.value = RecyclerItem(R.string.popular, it)
            }, {
                notify(Notify.ErrorMessage(it.message ?: "", null,null))
            })
    }

}

class MainState: IViewModelState