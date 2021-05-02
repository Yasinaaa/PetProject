package ru.skillbranch.sbdelivery.ui.basket

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.google.android.material.button.MaterialButton
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.local.entity.CartWithImage
import ru.skillbranch.sbdelivery.databinding.ItemBasketBinding
import ru.skillbranch.sbdelivery.databinding.ItemNotificationBinding
import ru.skillbranch.sbdelivery.utils.removeZero
import ru.skillbranch.sbdelivery.utils.toDp


open class BasketAdapter(
    var list: MutableList<CartWithImageDto> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var imageLoader: ImageLoader

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemBasketBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        imageLoader = ImageLoader.Builder(context)
            .componentRegistry { add(SvgDecoder(context)) }
            .build()
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_basket, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = list[position].name
            holder.bindingItem?.tvNum?.text = list[position].amount.toString()
            holder.bindingItem?.tvPrice?.text = String.format(context.getString(R.string.rub),
                list[position].price?.removeZero())

            val requestBuild = ImageRequest.Builder(context)
                .data(list[position].image)
                //.scale(Scale.)
                .target { drawable -> holder.bindingItem?.ivProduct?.setImageDrawable(drawable) }
                .build()
            imageLoader.enqueue(requestBuild)

            setNumStartMargin(list[position], holder.bindingItem?.tvNum, holder.bindingItem?.mbMinus)

            holder.bindingItem?.mbMinus?.setOnClickListener {
                changeNum(list[position], -1, holder.bindingItem.tvNum,
                    holder.bindingItem.mbMinus)
            }

            holder.bindingItem?.mbPlus?.setOnClickListener {
                changeNum(list[position], 1, holder.bindingItem.tvNum,
                    holder.bindingItem.mbMinus)
            }

            if (position == list.lastIndex){
                holder.bindingItem?.viewLine?.visibility = GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun changeNum(item: CartWithImageDto, count: Int, tv: TextView, mbMinus: MaterialButton){
        item.amount = item.amount!! + count
        tv.text = item.amount.toString()
        setNumStartMargin(item, tv, mbMinus)
    }

    private fun setNumStartMargin(item: CartWithImageDto, tv: TextView?, mbMinus: MaterialButton?){
        if (item.amount!! <= 1){
            mbMinus?.visibility = GONE
            changeMarginStart(tv, 0.toDp(context))
        }else{
            mbMinus?.visibility = VISIBLE
            changeMarginStart(tv, 33.toDp(context))
        }
    }

    private fun changeMarginStart(tv: TextView?, num: Int){
        val layoutParams = tv?.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = num
        tv.layoutParams = layoutParams
    }
}
