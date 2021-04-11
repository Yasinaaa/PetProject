package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.models.response.Address

interface IAddressRepository {
    fun checkAddressInput(address: String): Single<List<Address>>
    fun checkAddressCoordinates(lat: Float, lon: Float): Single<Address>
}

class AddressRepository(
    private val api: RestService
): IAddressRepository {

    override fun checkAddressInput(address: String): Single<List<Address>> =
        api.checkAddressInput(address)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun checkAddressCoordinates(lat: Float, lon: Float): Single<Address> =
        api.checkAddressCoordinates(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}