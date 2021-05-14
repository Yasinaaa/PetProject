package ru.skillbranch.sbdelivery.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemProductCardBinding
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.utils.removeZeroAndReplaceComma
import ru.skillbranch.sbdelivery.utils.toDp


open class CardAdapter(
    val type: Int,
    //var list: MutableList<CardItem> = mutableListOf(),
    private val addClick: (CardItem) -> Unit
) : PagingDataAdapter<CardItem, RecyclerView.ViewHolder>(CardDiffCallback()) {

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

            holder.bindingItem?.tvTitle?.text = getItem(position)?.title
            holder.bindingItem?.tvPrice?.text = String.format(context.getString(R.string.rub),
                getItem(position)?.price?.removeZeroAndReplaceComma())

            holder.bindingItem?.ivProductPhoto?.load(getItem(position)?.img)
            setVisibility(getItem(position)?.isPromotion!!, holder.bindingItem?.tvPromotion)

            if (getItem(position)?.isFavorite!!)
                holder.bindingItem?.ibFavorite?.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.yellow)
            else
                holder.bindingItem?.ibFavorite?.backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.white)

            val lp: RecyclerView.LayoutParams = holder.bindingItem?.cv?.layoutParams
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

            holder.bindingItem.cv.setOnClickListener { addClick.invoke(getItem(position)!!) }
        }
    }

    private fun setVisibility(condition: Boolean, view: View?){
        if (condition)
            view?.visibility = VISIBLE
        else
            view?.visibility = GONE
    }

}

class CardDiffCallback : DiffUtil.ItemCallback<CardItem>() {
    override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
        oldItem == newItem
}
