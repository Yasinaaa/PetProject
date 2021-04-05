package ru.skillbranch.sbdelivery.ui.menu

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
import ru.skillbranch.sbdelivery.databinding.ItemMenuBinding


open class MenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MenuItem(val title: Int, val img: Int)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemMenuBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<MenuItem> = mutableListOf(
        MenuItem(R.string.promotions, R.drawable.ic_star),
        MenuItem(R.string.drinks, R.drawable.ic_drink),
        MenuItem(R.string.sauces, R.drawable.ic_sauce),
        MenuItem(R.string.snacks, R.drawable.ic_snacks),
        MenuItem(R.string.deserts, R.drawable.ic_desert),
        MenuItem(R.string.salads, R.drawable.ic_salad),
        MenuItem(R.string.dumplings, R.drawable.ic_dumplings),
        MenuItem(R.string.paste, R.drawable.ic_paste),
        MenuItem(R.string.side_dishes, R.drawable.ic_side_dish),
        MenuItem(R.string.main_dish, R.drawable.ic_main_dish),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.mbItem!!.icon =
                ContextCompat.getDrawable(context, list[position].img)
            holder.bindingItem.mbItem.text = context.getString(list[position].title)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
