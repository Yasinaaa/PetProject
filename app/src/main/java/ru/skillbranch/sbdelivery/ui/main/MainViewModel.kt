package ru.skillbranch.sbdelivery.ui.main

import android.content.Context
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
    //private val useCase: IMainUseCase
    private val rep: IDishRepository,
): BaseViewModel<MainState>(handle, MainState()){

    val recommend = MutableLiveData<PagingData<CardItem>>()
    val best = MutableLiveData<PagingData<CardItem>>()
    val popular = MutableLiveData<PagingData<CardItem>>()

    fun getJobs() {
        rep.getRecommended()
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                recommend.value = it
            }
            .flatMap {
                rep.getBestDishes()
            }
            .doOnNext {
                this.best.value = it
            }
            .flatMap {
                rep.getMostLikedDishes()
            }
            .subscribe({
                this.popular.value = it
            }, {
                notify(Notify.ErrorMessage(it.message ?: "", null,null))
            })
    }

}

class MainState: IViewModelState