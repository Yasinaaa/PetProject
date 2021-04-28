package ru.skillbranch.sbdelivery.ui.main.adapters

data class RecyclerItem(
    val title: Int,
    var cards: MutableList<CardItem> = mutableListOf()
)

data class CardItem(
    val id: String,
    val title: String,
    val img: String,
    val price: Float,
    val isFavorite: Boolean,
    val isPromotion: Boolean
)
