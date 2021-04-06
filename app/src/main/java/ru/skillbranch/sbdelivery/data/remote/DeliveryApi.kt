package ru.skillbranch.sbdelivery.data.remote

import io.reactivex.rxjava3.core.Single
import retrofit2.http.*
import ru.skillbranch.sbdelivery.data.remote.models.Category
import ru.skillbranch.sbdelivery.data.remote.models.Dish
import ru.skillbranch.sbdelivery.data.remote.models.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.Token

interface DeliveryApi {

    @POST("auth/refresh")
    fun refreshToken(@Body refreshToken: RefreshToken): Single<Token>

    @GET("dishes")
    fun getDishes(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): Single<List<Dish>>

    @GET("categories")
    fun getCategories(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): Single<List<Category>>


}