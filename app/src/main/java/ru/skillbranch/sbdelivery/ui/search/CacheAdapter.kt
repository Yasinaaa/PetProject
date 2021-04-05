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


open class CacheAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CacheItem(val title: String)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemCacheSearchBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<CacheItem> = mutableListOf(
        CacheItem("Пицца"),
        CacheItem("Пицца"),
        CacheItem("Пицца")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cache_search, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = list[position].title
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
