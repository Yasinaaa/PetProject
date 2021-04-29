package ru.skillbranch.sbdelivery.data.dto

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import ru.skillbranch.sbdelivery.R


data class DishDto(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val oldPrice: String,
    val price: String,
    val rating: String,
    val likes: Long,
    val commentsCount: Int,
    val isHasPromotion: Boolean,
    val isFavorite: Boolean
){

    companion object{
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: AppCompatImageView, url: String?) {
            Glide.with(view.context)
                .load(url)
                .placeholder(R.drawable.ic_bowl)
                .into(view)
        }
    }



}