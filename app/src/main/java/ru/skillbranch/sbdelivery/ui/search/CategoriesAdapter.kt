package ru.skillbranch.sbdelivery.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.bumptech.glide.Glide
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.databinding.ItemCacheSearchBinding
import ru.skillbranch.sbdelivery.databinding.ItemMenuBinding
import ru.skillbranch.sbdelivery.databinding.ItemSearchCategoryBinding


open class CategoriesAdapter(
    val list: MutableList<CategoryEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MenuItem(val title: Int, val img: Int? = null)

    private lateinit var context: Context
    private lateinit var imageLoader: ImageLoader

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemSearchCategoryBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_category, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            if (list[position].icon != null) {
                val request = ImageRequest.Builder(context).data(list[position].icon)
                if (list[position].name.trim().contains(" "))
                    request.size(70, 70)
                else
                    request.size(100, 100)

                val requestBuild = request
                    .scale(Scale.FIT)
                    .target { drawable -> holder.bindingItem?.mbItem?.icon = drawable }
                    .build()

                imageLoader.enqueue(requestBuild)
            }

            holder.bindingItem?.mbItem?.text = list[position].name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
