package ru.skillbranch.sbdelivery.ui.main

import android.content.Context
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.exceptions.CompositeException
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.remote.err.EmptyDishesError
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem
import java.net.ConnectException
import java.net.UnknownHostException

interface IMainUseCase{
    fun getFields(
        errorHandler: ((String?) -> Unit)
    ): Single<MutableList<RecyclerItem>>
}

class MainUseCase(
    private val repository: IDishRepository,
    private val context: Context,
): IMainUseCase{

    override fun getFields(
        errorHandler: ((String?) -> Unit)
    ): Single<MutableList<RecyclerItem>>{
        return repository.getRecommended(0, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn {
                if (it is UnknownHostException || it is ConnectException || it is CompositeException){
                    errorHandler.invoke(context.getString(R.string.network_is_unavailable))
                }
                mutableListOf()
            }
            .flatMap {
                Single.zip(
                    repository.getBestDishes(),
                    repository.getMostLikedDishes(), {
                            best, liked ->
                            val items = mutableListOf<RecyclerItem>()
                            if (it.isNotEmpty())
                                items.add(RecyclerItem(R.string.recommend, it))
                            if (best.isNotEmpty())
                                items.add(RecyclerItem(R.string.promotions, best))
                            if (liked.isNotEmpty())
                                items.add(RecyclerItem(R.string.popular, liked))
                            items
                    }
                ).onErrorReturn {
                    throw EmptyDishesError()
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
