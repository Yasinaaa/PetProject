package ru.skillbranch.sbdelivery.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.dao.ReviewDao
import ru.skillbranch.sbdelivery.data.local.entity.ReviewEntity
import ru.skillbranch.sbdelivery.data.local.pref.PrefManager
import ru.skillbranch.sbdelivery.data.mapper.IReviewsMapper
import ru.skillbranch.sbdelivery.data.remote.RestService
import ru.skillbranch.sbdelivery.utils.getDate

interface IReviewsRepository {
    fun getReviews(offset: Int, limit: Int, dishId: String): Single<List<ReviewEntity>>
    fun addReview(dishId: String, rating: Int, text: String): Single<ReviewEntity>
}

class ReviewsRepository(
    private val prefs: PrefManager,
    private val api: RestService,
    private val mapper: IReviewsMapper,
    private val reviewDao: ReviewDao
) : IReviewsRepository {

    override fun getReviews(
        offset: Int,
        limit: Int,
        dishId: String
    ): Single<List<ReviewEntity>> =
        api.getReviews(dishId, offset, limit,
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .doOnSuccess {
                reviewDao.insert(mapper.mapReviewListToEntityList(it))
            }.map {
                mapper.mapReviewListToEntityList(it.sortedByDescending { review -> review.date?.getDate() })
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addReview(dishId: String, rating: Int, text: String): Single<ReviewEntity> =
        api.addReview(dishId, text, rating,
            "${PrefManager.BEARER} ${prefs.accessToken}")
            .doOnSuccess {
                //if (it.)
                    reviewDao.insert(mapper.createReview(rating, text))
            }.map {
                mapper.createReview(rating, text)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}