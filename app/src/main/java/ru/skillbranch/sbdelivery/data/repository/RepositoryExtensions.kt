package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

fun Single<ResponseBody>.saveResponseAsBool(): Single<Boolean> {
    return map {
            it != null
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}