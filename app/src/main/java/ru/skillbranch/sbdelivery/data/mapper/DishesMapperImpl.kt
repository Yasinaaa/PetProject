package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish


open class DishesMapperImpl : DishesMapper {


//    override fun mapDtoToState(dishEntity: List<DishEntity>): List<ProductItemState> =
//        dishEntity.map {
//            ProductItemState(
//                id = it.id,
//                image = it.image,
//                price = it.price,
//                title = it.title,
//                categoryId = it.categoryId
//            )
//        }
//
//    override fun mapDtoToEntity(dishesDto: List<Dish>): List<DishEntity> =
//        dishesDto.map { DishEntity(it.id ?: "", it.category ?: "", it.image ?: "", "${it.price} ₽", it.name ?: "") }
//
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

//    override fun mapPersistToEntity(dishesPersist: List<DishPersistEntity>): List<DishEntity> =
//        dishesPersist.map { DishEntity(it.id, it.category, it.image, "${it.price} ₽", it.name) }
}