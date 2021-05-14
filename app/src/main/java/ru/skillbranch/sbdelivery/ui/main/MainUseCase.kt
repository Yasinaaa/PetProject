package ru.skillbranch.sbdelivery.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.remote.err.EmptyDishesError
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.data.paging.PostDataSource
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem
import java.net.ConnectException
import java.net.UnknownHostException

interface IMainUseCase{
    fun getFields(
        d: MutableLiveData<MutableList<RecyclerItem>>,//<PagingData<RecyclerItem>>,
        errorHandler: ((String?) -> Unit)
    ):Flowable<MutableList<RecyclerItem>>
}

class MainUseCase(
    private val repository: IDishRepository,
    private val context: Context,
): IMainUseCase{

//    val listData = Pager(PagingConfig(pageSize = 6)) {
//        PostDataSource(repository)
//    }.flow//.cachedIn(viewModelScope)

    private val compositeDisposable = CompositeDisposable()

    init {

    }

    override fun getFields(
        d: MutableLiveData<MutableList<RecyclerItem>>,
        errorHandler: ((String?) -> Unit)
    ): Flowable<MutableList<RecyclerItem>>{

        //compositeDisposable.add(
            return repository.getRecommended()
                //.cachedIn(viewModelScope)

                .onErrorReturn {
                    if (it is UnknownHostException || it is ConnectException || it is CompositeException){
                        errorHandler.invoke(context.getString(R.string.network_is_unavailable))
                    }
                    PagingData.empty<CardItem>()
                }
                .flatMap {
                    Flowable.zip(
                        repository.getBestDishes().toFlowable(),
                        repository.getMostLikedDishes().toFlowable(), {
                                best, liked ->
                                val items = mutableListOf<RecyclerItem>()
                                if (it != PagingData.empty<CardItem>())
                                    items.add(RecyclerItem(R.string.recommend, it))
//                                if (best.isNotEmpty())
//                                    items.add(RecyclerItem(R.string.promotions, best))
//                                if (liked.isNotEmpty())
//                                    items.add(RecyclerItem(R.string.popular, liked))
                                items
                        }
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        //)
//        return repository.getRecommended(0, 10)
//            .observeOn(AndroidSchedulers.mainThread())
//            .onErrorReturn {
//                if (it is UnknownHostException || it is ConnectException || it is CompositeException){
//                    errorHandler.invoke(context.getString(R.string.network_is_unavailable))
//                }
//                mutableListOf()
//            }
//            .flatMap {
//                Single.zip(
//                    repository.getBestDishes(),
//                    repository.getMostLikedDishes(), {
//                            best, liked ->
//                            val items = mutableListOf<RecyclerItem>()
//                            if (it.isNotEmpty())
//                                items.add(RecyclerItem(R.string.recommend, it))
//                            if (best.isNotEmpty())
//                                items.add(RecyclerItem(R.string.promotions, best))
//                            if (liked.isNotEmpty())
//                                items.add(RecyclerItem(R.string.popular, liked))
//                            items
//                    }
//                ).onErrorReturn {
//                    throw EmptyDishesError()
//                }
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
    }
}
