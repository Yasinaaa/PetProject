package ru.skillbranch.sbdelivery.ui.basket

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.data.local.entity.CartEntity
import ru.skillbranch.sbdelivery.databinding.ItemBasketBinding
import ru.skillbranch.sbdelivery.utils.removeZeroAndReplaceComma

interface BasketListener{
    fun updateCount(dto: CartWithImageDto, obs: Observable<Int>?)
    fun updateTotalPrice(totalPrice: String)
    fun setEmptyList()
}

open class BasketAdapter(
    private val cart: CartEntity,
    private var list: MutableList<CartWithImageDto> = mutableListOf(),
    private val listener: BasketListener
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
            setPrice(holder.bindingItem?.tvPrice, list[position])

            val requestBuild = ImageRequest.Builder(context)
                .data(list[position].image)
                .target { drawable -> holder.bindingItem?.ivProduct?.setImageDrawable(drawable) }
                .build()
            imageLoader.enqueue(requestBuild)

            val observable = holder.bindingItem?.tvNum?.textChanges()?.skipInitialValue()?.
            map {
                it.toString().toInt()
            }

            holder.bindingItem?.mbMinus?.setOnClickListener {
                if(list[position].amount!! > 0)
                    changeNum(
                        list[position], -1,
                        holder.bindingItem.tvNum,
                        holder.bindingItem.tvPrice,
                        observable
                    )
            }

            holder.bindingItem?.mbPlus?.setOnClickListener {
                changeNum(
                    list[position], 1,
                    holder.bindingItem.tvNum,
                    holder.bindingItem.tvPrice,
                    observable
                )
            }

            if (position == list.lastIndex){
                holder.bindingItem?.viewLine?.visibility = GONE
                listener.updateTotalPrice(getTotalPrice())
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeNum(
        item: CartWithImageDto, count: Int,
        tvCount: TextView?, tvPrice: TextView?,
        obs: Observable<Int>?
    ){
        item.amount = item.amount!! + count
        tvCount?.text = item.amount.toString()
        setPrice(tvPrice, item)

        if (item.amount == 0) {
            list.remove(item)
            if (list.size == 0)
                listener.setEmptyList()
            notifyDataSetChanged()
        }
        listener.updateTotalPrice(getTotalPrice())
        listener.updateCount(item, obs)
    }

    private fun setPrice(tvPrice: TextView?, item: CartWithImageDto){
        tvPrice?.text = String.format(context.getString(R.string.rub),
            (item.price!! * item.amount!!).removeZeroAndReplaceComma())
    }

    private fun getTotalPrice(): String{
        val cart = cart.total ?: 0f
        return String.format(
            context.getString(R.string.rub),
            list.map {
                it.price!! * it.amount!!
            }
            .sum()
            .plus(if(cart == 0f) 0f else cart)
            .removeZeroAndReplaceComma())
    }
}
