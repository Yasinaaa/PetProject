package ru.skillbranch.sbdelivery.ui.orders.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.databinding.ItemOrderBinding
import ru.skillbranch.sbdelivery.utils.parseMilliseconds
import ru.skillbranch.sbdelivery.utils.removeZeroAndReplaceComma
import java.util.ArrayList


class OrderItemsAdapter(
    private var list: MutableList<OrderDto> = mutableListOf(),
    private val click: (OrderDto) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemOrderBinding? = DataBindingUtil.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvDate?.text = list[position].createdAt?.parseMilliseconds()
            holder.bindingItem?.tvOrderId?.text = String.format(
                context.getString(R.string.number), list[position].id)
            holder.bindingItem?.tvStatus?.text = list[position].statusName
            holder.bindingItem?.tvAddress?.text = list[position].address
            holder.bindingItem?.tvPrice?.text = String.format(
                context.getString(R.string.rub),
                list[position].total?.removeZeroAndReplaceComma()
            )
            holder.bindingItem?.root?.setOnClickListener { click.invoke(list[position]) }
        }
    }

    fun updateList(list: ArrayList<OrderDto>?){
        if (list != null) {
            this.list = list.toMutableList()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
