package ru.skillbranch.sbdelivery.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemProductCardBinding
import ru.skillbranch.sbdelivery.utils.toDp


open class CardAdapter(val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val MAIN = 1
        const val FAVORITE = 2
        const val CATEGORY = 3
    }

    class Item(val title: String, val price: String)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemProductCardBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<Item> = mutableListOf(
        Item("Сет Королевский", "1280P"),
        Item("Сет Королевский", "1280P"),
        Item("Сет Королевский", "1280P")
    )

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
//            holder.bindingItem?.mbItem!!.icon =
//                ContextCompat.getDrawable(context, list[position].img)
            holder.bindingItem?.tvTitle?.text = list[position].title
            holder.bindingItem?.tvPrice?.text = list[position].price

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
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
