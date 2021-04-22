package ru.skillbranch.sbdelivery.data.mapper

import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Category

interface ICategoriesMapper {
    fun mapDtoToEntity(dto: List<Category>): List<CategoryEntity>
}

class CategoriesMapper: ICategoriesMapper {

    override fun mapDtoToEntity(dto: List<Category>): List<CategoryEntity> =
        dto.map {
            CategoryEntity(
                it.categoryId,
                it.name,
                it.order,
                it.icon,
                it.parent,
                it.active,
                it.createdAt,
                it.updatedAt
            )
        }
}