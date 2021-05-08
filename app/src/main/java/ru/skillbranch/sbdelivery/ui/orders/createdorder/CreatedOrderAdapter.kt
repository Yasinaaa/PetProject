package ru.skillbranch.sbdelivery.ui.orders.createdorder

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.OrderItemDto
import ru.skillbranch.sbdelivery.databinding.ItemCreatedOrderBinding
import ru.skillbranch.sbdelivery.utils.removeZeroAndReplaceComma


class CreatedOrderAdapter(
    private var list: MutableList<OrderItemDto> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var imageLoader: ImageLoader

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemCreatedOrderBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_created_order, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = list[position].name
            holder.bindingItem?.tvCount?.text =
                String.format(context.getString(R.string.count), list[position].amount.toString())
            holder.bindingItem?.tvPrice?.text = String.format(context.getString(R.string.rub),
                list[position].price?.removeZeroAndReplaceComma() ?: 0f)

            val requestBuild = ImageRequest.Builder(context)
                .data(list[position].image)
                .target { drawable -> holder.bindingItem?.ivProduct?.setImageDrawable(drawable) }
                .build()
            imageLoader.enqueue(requestBuild)

            if (position == list.lastIndex){
                holder.bindingItem?.viewLine?.visibility = GONE
            }
        }
    }

    fun update(list: MutableList<OrderItemDto>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
