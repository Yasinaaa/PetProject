package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.data.remote.models.response.Favorite

interface IDishesMapper {
    fun mapDtoToPersist(dishesDto: List<Dish>): List<DishEntity>
    fun mapFavoriteToStringIds(dishesDto: List<Favorite>): List<String>
}

open class DishesMapper : IDishesMapper {

    override fun mapDtoToPersist(dishesDto: List<Dish>): List<DishEntity> =
        dishesDto.map {
            DishEntity(
                it.id ?: "",
                it.name ?: "",
                it.description ?: "",
                it.image ?: "",
                it.oldPrice ?: 0,
                it.price ?: 0L,
                it.rating ?: 0F,
                it.likes ?: 0,
                it.category ?: "",
                it.commentsCount ?: 0,
                it.active ?: false,
                it.createdAt ?: "",
                it.updatedAt ?: "",
                false
            )
        }

    override fun mapFavoriteToStringIds(dishesDto: List<Favorite>): List<String> =
        dishesDto.map {
            it.dishId
        }
}