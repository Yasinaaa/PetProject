package ru.skillbranch.sbdelivery.data.remote

import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.*
import ru.skillbranch.sbdelivery.data.remote.models.request.*
import ru.skillbranch.sbdelivery.data.remote.models.response.*

interface DeliveryApi {

    @POST("auth/login")
    @Headers("Content-Type:application/json")
    fun login(@Body userLoginRequest: UserLoginRequest): Single<UserWithTokens>

    @POST("auth/register")
    @Headers("Content-Type:application/json")
    fun register(@Body userRegisterRequest: UserRegisterRequest): Single<UserWithTokens>

    @POST("auth/recovery/email")
    @Headers("Content-Type:application/json")
    fun recoveryEmail(@Query("email") email: String): Single<ResponseBody>

    @POST("auth/recovery/code")
    @Headers("Content-Type:application/json")
    fun recoveryCode(@Body request: RecoveryCodeRequest): Single<ResponseBody>

    @POST("auth/recovery/password")
    @Headers("Content-Type:application/json")
    fun recoveryPassword(@Body request: RecoveryPasswordRequest): Single<ResponseBody>

    @POST("auth/refresh")
    @Headers("Content-Type:application/json")
    fun refreshToken(@Body refreshToken: RefreshToken): Single<Token>

    @GET("profile")
    @Headers("Content-Type:application/json")
    fun getProfile(@Header("Authorization") token: String): Single<User>

    @PUT("profile")
    @Headers("Content-Type:application/json")
    fun putProfile(
        @Query("firstName") firstName: String,
        @Query("lastName") lastName: String,
        @Query("email") email: String,
        @Header("Authorization") token: String): Single<ResponseBody>

    @PUT("profile/password")
    @Headers("Content-Type:application/json")
    fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest,
        @Header("Authorization") token: String): Single<ResponseBody>

    @GET("favorite")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getFavorite(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): Single<List<Favorite>>

    @PUT("favorite")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun changeFavorite(
        @Query("dishId") dishId: Int,
        @Query("favorite") favorite: Boolean,
        @Header("Authorization") token: String
    ): Single<ResponseBody>

    @GET("recommend")
    fun getRecommend(): Single<List<String>>

    @GET("categories")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getCategories(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): Single<List<Category>>

    @GET("dishes")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getDishes(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Header("Authorization") token: String
    ): Single<List<Dish>>

    @GET("reviews/{dishId}")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getReviews(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Path("dishId") dishId: String,
        @Header("Authorization") token: String
    ): Single<List<Review>>

    @POST("reviews/{dishId}")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun addReview(
        @Query("rating") rating: Int,
        @Path("text") text: String,
        @Header("Authorization") token: String
    ): Single<ResponseBody>

    @GET("cart")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getCart(@Header("Authorization") token: String): Single<Cart>

    @PUT("cart")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun updateCart(@Query("promocode") promoCode: String,
                   @Query("items") items: List<CartItemRequest>,
                   @Header("Authorization") token: String): Single<Cart>

    @POST("address/input")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun checkAddressInput(
        @Query("address") address: String
    ): Single<List<Address>>

    @POST("address/coordinates")
    @Headers("If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun checkAddressCoordinates(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): Single<Address>

    @POST("orders/new")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun createOrder(@Body request: OrderRequest,
                    @Header("Authorization") token: String): Single<Order>

    @GET("orders")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getOrders(@Query("offset") offset: Int,
                  @Query("limit") limit: Int,
                  @Header("Authorization") token: String): Single<List<Order>>

    @GET("orders/statuses")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun getStatuses(@Header("Authorization") token: String): Single<List<OrderStatus>>

    @PUT("orders/cancel")
    @Headers("Content-Type:application/json",
        "If-Modified-Since: Mon, 1 Jun 2020 08:00:00 GMT")
    fun cancelOrder(@Query("orderId") orderId: String,
                  @Header("Authorization") token: String): Single<Order>

}