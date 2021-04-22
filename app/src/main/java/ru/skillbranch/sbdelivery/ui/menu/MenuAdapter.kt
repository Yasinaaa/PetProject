package ru.skillbranch.sbdelivery.ui.menu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.SizeResolver
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.data.remote.models.response.Category
import ru.skillbranch.sbdelivery.databinding.ItemMenuBinding
import java.io.File
import java.util.*


open class MenuAdapter(
    val list: MutableList<CategoryEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var imageLoader: ImageLoader

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemMenuBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_menu, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.mbItem?.text = list[position].name.trim()
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
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
