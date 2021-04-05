package ru.skillbranch.sbdelivery.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemCacheSearchBinding
import ru.skillbranch.sbdelivery.databinding.ItemMenuBinding
import ru.skillbranch.sbdelivery.databinding.ItemSearchCategoryBinding


open class CategoriesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MenuItem(val title: Int, val img: Int? = null)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemSearchCategoryBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<MenuItem> = mutableListOf(
        MenuItem(R.string.sushi_and_rolls, R.drawable.ic_sushi),
        MenuItem(R.string.sushi_and_rolls),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_category, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (list[position].img != null)
                holder.bindingItem?.mbItem!!.icon =
                    ContextCompat.getDrawable(context, list[position].img!!)

            holder.bindingItem?.mbItem?.text = context.getString(list[position].title)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
