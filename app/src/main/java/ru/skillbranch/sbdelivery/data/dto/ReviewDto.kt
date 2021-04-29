package ru.skillbranch.sbdelivery.data.dto

import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import ru.skillbranch.sbdelivery.R

data class ReviewDto (
    val title: String,
    val text: String,
    val stars: Float
)