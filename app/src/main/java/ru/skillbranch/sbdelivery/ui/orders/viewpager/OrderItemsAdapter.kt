package ru.skillbranch.sbdelivery.ui.orders.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemOrderBinding
import ru.skillbranch.sbdelivery.utils.removeZeroAndReplaceComma


open class OrderItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class OrderItem(val date: String, val orderId: String, val status: String,
    val price: Float, val address: String)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemOrderBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<OrderItem> = mutableListOf(
        OrderItem(date = "5 апреля 2020", orderId = "№ 56789", status = "В обработке",
        address = "г. Москва, ул. Тверская, д. 7", price = 1130f),
    )

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
            holder.bindingItem?.tvDate?.text = list[position].date
            holder.bindingItem?.tvOrderId?.text = list[position].orderId
            holder.bindingItem?.tvStatus?.text = list[position].status
            holder.bindingItem?.tvAddress?.text = list[position].address
            holder.bindingItem?.tvPrice?.text = String.format(
                context.getString(R.string.rub),
                list[position].price.removeZeroAndReplaceComma()
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
