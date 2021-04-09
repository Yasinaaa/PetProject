package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish


interface DishesMapper {
//    fun mapDtoToState(dishEntity: List<DishEntity>): List<ProductItemState>
//    fun mapDtoToEntity(dishesDto: List<Dish>): List<DishEntity>
    fun mapDtoToPersist(dishesDto: List<Dish>): List<DishEntity>
 //   fun mapPersistToEntity(dishesPersist: List<DishEntity>): List<DishEntity>
}