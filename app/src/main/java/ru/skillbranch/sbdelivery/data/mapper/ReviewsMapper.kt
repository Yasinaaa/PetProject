package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.ReviewEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Review

interface IReviewsMapper {
    fun mapReviewListToEntityList(review: List<Review>): List<ReviewEntity>
    fun mapReviewToEntity(review: Review): ReviewEntity
    fun createReview(rating: Int, text: String): ReviewEntity
}

open class ReviewsMapper : IReviewsMapper {

    override fun mapReviewListToEntityList(review: List<Review>): List<ReviewEntity> =
        review.map {
            ReviewEntity(
                id = it.id ?: "",
                author = it.author ?: "",
                date = it.date ?: "",
                rating = it.rating ?: 0f,
                text = it.text ?: "",
                active = it.active ?: false,
                createdAt = it.createdAt ?: "",
                updatedAt = it.updatedAt ?: ""
            )
        }

    override fun mapReviewToEntity(review: Review): ReviewEntity = ReviewEntity(
            id = review.id ?: "",
            author = review.author ?: "",
            date = review.date ?: "",
            rating = review.rating ?: 0f,
            text = review.text ?: "",
            active = review.active ?: false,
            createdAt = review.createdAt ?: "",
            updatedAt = review.updatedAt ?: ""
        )

    override fun createReview(rating: Int, text: String): ReviewEntity = ReviewEntity(
        id = "", //TODO
        author = "", //TODO
        date = "", //TODO
        rating = rating.toFloat(),
        text = text,
        active = true, //TODO
        createdAt = "", //TODO
        updatedAt = "", //TODO
    )
}