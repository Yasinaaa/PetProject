package ru.skillbranch.sbdelivery.ui.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemMainRvBinding
import ru.skillbranch.sbdelivery.databinding.ItemMenuBinding


open class RecyclersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RecyclerViewItem(val title: Int)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemMainRvBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<RecyclerViewItem> = mutableListOf(
        RecyclerViewItem(R.string.recommend),
        RecyclerViewItem(R.string.best),
        RecyclerViewItem(R.string.popular)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_main_rv, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
//            holder.bindingItem?.mbItem!!.icon =
//                ContextCompat.getDrawable(context, list[position].img)
            holder.bindingItem?.tvTitle?.text = context.getString(list[position].title)
            holder.bindingItem?.rvItems?.adapter = CardAdapter()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
