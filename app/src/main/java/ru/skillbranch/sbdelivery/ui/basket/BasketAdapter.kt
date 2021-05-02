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
import com.google.android.material.button.MaterialButton
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
import ru.skillbranch.sbdelivery.databinding.ItemBasketBinding
import ru.skillbranch.sbdelivery.utils.removeZero


open class BasketAdapter(
    var list: MutableList<CartWithImageDto> = mutableListOf(),
    private val numChange: (CartWithImageDto?, Observable<Int>?, Int?) -> Unit
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
                .target { drawable -> holder.bindingItem?.ivProduct?.setImageDrawable(drawable) }
                .build()
            imageLoader.enqueue(requestBuild)

            val observable = holder.bindingItem?.tvNum?.textChanges()?.skipInitialValue()?.
            map {
                it.toString().toInt()
            }
            numChange.invoke(list[position], observable, list.size)

            holder.bindingItem?.mbMinus?.setOnClickListener {
                if(list[position].amount!! > 0)
                    changeNum(list[position], -1, holder.bindingItem.tvNum)
            }

            holder.bindingItem?.mbPlus?.setOnClickListener {
                changeNum(list[position], 1, holder.bindingItem.tvNum)
            }

            if (position == list.lastIndex){
                holder.bindingItem?.viewLine?.visibility = GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeNum(
        item: CartWithImageDto, count: Int, tv: TextView
    ){
        item.amount = item.amount!! + count
        tv.text = item.amount.toString()

        if (item.amount == 0) {
            list.remove(item)
            numChange.invoke(null, null, list.size)
            notifyDataSetChanged()
        }
    }
}
