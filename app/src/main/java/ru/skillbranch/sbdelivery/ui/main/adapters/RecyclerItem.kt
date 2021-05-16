package ru.skillbranch.sbdelivery.ui.main.adapters

import androidx.paging.PagingData

data class RecyclerItem(
    val title: Int,
    var cards: PagingData<CardItem>? = null,
    var isEmpty: Boolean = false
)

data class CardItem(
    val id: String,
    val title: String,
    val img: String,
    val price: Float,
    val isFavorite: Boolean,
    val isPromotion: Boolean
)
