package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.data.remote.models.response.Favorite
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem

interface IDishesMapper {
    fun mapDtoToPersist(dishesDto: List<Dish>): List<DishEntity>
    fun mapFavoriteToStringIds(dishesDto: List<Favorite>): List<String>


    fun mapDishToCardItem(
        dishes: List<DishEntity>,
        recommended: List<String> = mutableListOf()
    ): MutableList<CardItem>
}

open class DishesMapper : IDishesMapper {

    override fun mapDtoToPersist(dishesDto: List<Dish>): List<DishEntity> =
        dishesDto.map {
            DishEntity(
                it.id ?: "",
                it.name ?: "",
                it.description ?: "",
                it.image ?: "",
                it.oldPrice ?: 0F,
                it.price ?: 0F,
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

    override fun mapDishToCardItem(
        dishes: List<DishEntity>,
        recommended: List<String>)
    : MutableList<CardItem> {
        val result = mutableListOf<CardItem>()
        for(dish in dishes){
            if (recommended.isEmpty()){
                result.add(createCardItem(dish))
            }else{
                for(id in recommended) {
                    if(id == dish.id)
                        result.add(createCardItem(dish))
                }
            }
        }
        return result
    }

    private fun createCardItem(dish: DishEntity) =
        CardItem(
            title = dish.name ?: "",
            price = dish.price,
            img = dish.image ?: "",
            isFavorite = false, //TODO
            isPromotion = dish.oldPrice.compareTo(dish.price) > 0
        )

}