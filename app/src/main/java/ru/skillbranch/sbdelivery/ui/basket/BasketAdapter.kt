package ru.skillbranch.sbdelivery.ui.basket

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemBasketBinding
import ru.skillbranch.sbdelivery.databinding.ItemNotificationBinding
import ru.skillbranch.sbdelivery.utils.removeZero


open class BasketAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BasketItem(val title: String, val count: Int, val price: Float)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemBasketBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<BasketItem> = mutableListOf(
        BasketItem("Сет Королевский", 2, 1280f),
        BasketItem("Сет Королевский", 2, 1280f),
        BasketItem("Сет Королевский", 2, 1280f),
        BasketItem("Сет Королевский", 2, 1280f),
        BasketItem("Сет Королевский", 2, 1280f),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_basket, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = list[position].title
            holder.bindingItem?.tvNum?.text = list[position].count.toString()
            holder.bindingItem?.tvPrice?.text = String.format(context.getString(R.string.rub),
                list[position].price.removeZero())

            if (position == list.lastIndex){
                holder.bindingItem?.viewLine?.visibility = GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
