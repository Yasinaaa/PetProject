package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.DishesDao
import ru.skillbranch.sbdelivery.data.local.dao.ReviewDao
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.local.entity.ReviewEntity
import ru.skillbranch.sbdelivery.data.mapper.DishesMapper
import ru.skillbranch.sbdelivery.data.mapper.ReviewsMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.data.remote.RetrofitProvider
import ru.skillbranch.sbdelivery.data.remote.models.request.RefreshToken
import ru.skillbranch.sbdelivery.data.remote.models.response.Category
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.data.remote.models.response.Favorite
import ru.skillbranch.sbdelivery.data.remote.models.response.Review

interface IReviewsRepository {
    fun getReviews(offset: Int, limit: Int, dishId: String): Single<List<ReviewEntity>>
    fun addReview(rating: Int, text: String): Single<ReviewEntity>
}

class ReviewsRepository(
    private val api: RestService,
    private val mapper: ReviewsMapper,
    private val reviewDao: ReviewDao
) : IReviewsRepository {

    override fun getReviews(
        offset: Int,
        limit: Int,
        dishId: String
    ): Single<List<ReviewEntity>> =
        api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
            .flatMap {
                api.getReviews(offset, limit, dishId,
                    "${RetrofitProvider.BEARER} ${it.accessToken}")
            }.doOnSuccess {
                reviewDao.insert(mapper.mapReviewListToEntityList(it))
            }.map {
                mapper.mapReviewListToEntityList(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addReview(rating: Int, text: String): Single<ReviewEntity> =
        api.refreshToken(RefreshToken(RetrofitProvider.REFRESH_TOKEN))
            .flatMap {
                api.addReview(rating, text,
                    "${RetrofitProvider.BEARER} ${it.accessToken}")
            }.doOnSuccess {
                if (it.isSuccessful)
                    reviewDao.insert(mapper.createReview(rating, text))
            }.map {
                mapper.createReview(rating, text)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}