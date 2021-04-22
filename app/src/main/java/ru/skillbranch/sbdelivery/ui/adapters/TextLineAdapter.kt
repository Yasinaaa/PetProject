package ru.skillbranch.sbdelivery.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemCacheSearchBinding


open class TextLineAdapter(
    val type: Int,
    var list: MutableSet<String> = mutableSetOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val CACHE = 1
        const val ADDRESS = 2
    }

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemCacheSearchBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cache_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = list.elementAt(position)

            if(type == ADDRESS){
                holder.bindingItem?.ivClose?.visibility = GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
