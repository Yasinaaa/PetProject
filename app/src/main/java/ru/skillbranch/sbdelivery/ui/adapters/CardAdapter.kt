package ru.skillbranch.sbdelivery.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemProductCardBinding
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.utils.removeZero
import ru.skillbranch.sbdelivery.utils.toDp


open class CardAdapter(
    val type: Int,
    var list: MutableList<CardItem> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val MAIN = 1
        const val FAVORITE = 2
        const val CATEGORY = 3
    }

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemProductCardBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_card, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {

            holder.bindingItem?.tvTitle?.text = list[position].title
            holder.bindingItem?.tvPrice?.text = String.format(context.getString(R.string.rub),
                list[position].price.removeZero())

            Glide.with(context)
                .load(list[position].img)
                .placeholder(R.drawable.ic_bowl)
                .into(holder.bindingItem?.ivProductPhoto!!)

            setVisibility(list[position].isPromotion, holder.bindingItem.tvPromotion)

            if (list[position].isFavorite)
                holder.bindingItem.ibFavorite.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.yellow)
            else
                holder.bindingItem.ibFavorite.backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.white)

            val lp: RecyclerView.LayoutParams = holder.bindingItem.cv.layoutParams
                    as RecyclerView.LayoutParams

            if (type == FAVORITE) {
                if (position != 1 || position != 2) {
                    lp.topMargin = 10.toDp(context)
                }
            }else if (type == MAIN){
                lp.width = 158.toDp(context)

            }else if (type == CATEGORY){
                if (position != 1 || position != 2) {
                    lp.topMargin = 10.toDp(context)
                }
            }
        }
    }

    private fun setVisibility(condition: Boolean, view: View?){
        if (condition)
            view?.visibility = VISIBLE
        else
            view?.visibility = GONE
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
