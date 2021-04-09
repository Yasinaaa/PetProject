package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.OrderDao
import ru.skillbranch.sbdelivery.data.local.entity.CartItemEntity
import ru.skillbranch.sbdelivery.data.local.entity.CartWithItems
import ru.skillbranch.sbdelivery.data.local.entity.OrderWithItems
import ru.skillbranch.sbdelivery.data.mapper.CartMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.RetrofitProvider
import ru.skillbranch.sbdelivery.data.remote.models.request.OrderRequest
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.Address
import ru.skillbranch.sbdelivery.data.remote.models.response.Order
import ru.skillbranch.sbdelivery.data.remote.models.response.OrderStatus

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