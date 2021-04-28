package ru.skillbranch.sbdelivery.data.mapper

import android.content.Context
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.DishDto
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Dish
import ru.skillbranch.sbdelivery.data.remote.models.response.Favorite
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.utils.removeZero

interface IDishesMapper {
    fun mapDtoToPersist(dishesDto: List<Dish>): List<DishEntity>
    fun mapFavoriteToStringIds(dishesDto: List<Favorite>): List<String>
    fun mapEntitiesToDto(dishes: List<DishEntity>): List<DishDto>
    fun mapEntityToDto(dish: DishEntity): DishDto

    fun mapDishToCardItem(
        dishes: List<DishEntity>,
        recommended: List<String> = mutableListOf()
    ): MutableList<CardItem>
}

open class DishesMapper(
    val context: Context
) : IDishesMapper {

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
            id = dish.id,
            title = dish.name ?: "",
            price = dish.price,
            img = dish.image ?: "",
            isFavorite = false, //TODO
            isPromotion = dish.oldPrice.compareTo(dish.price) > 0
        )

    override fun mapEntitiesToDto(dishes: List<DishEntity>): List<DishDto> =
        dishes.map { mapEntityToDto(it) }

    override fun mapEntityToDto(dish: DishEntity): DishDto =
        DishDto(
            dish.id ?: "",
            dish.name ?: "",
            dish.description ?: "",
            dish.image ?: "",
            String.format(context.getString(R.string.rub), dish.oldPrice.removeZero()),
            String.format(context.getString(R.string.rub), dish.price.removeZero()),
            String.format(context.getString(R.string.rating), dish.rating.removeZero()),
            dish.likes ?: 0,
            dish.commentsCount,
            dish.oldPrice.compareTo(dish.price) > 0,
            dish.active ?: false,
        )

}