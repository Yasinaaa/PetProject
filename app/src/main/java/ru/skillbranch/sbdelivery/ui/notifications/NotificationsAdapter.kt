package ru.skillbranch.sbdelivery.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemNotificationBinding


open class NotificationsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class NotifItem(val title: String, val text: String, val isNew: Boolean)

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemNotificationBinding? = DataBindingUtil.bind(view)
    }

    private var list: MutableList<NotifItem> = mutableListOf(
        NotifItem("Заказ №56787 доставляется",
            "Ваш заказ на сумму 1300 руб. доставляется курьером по адресу Москва, ул. Тверская, 7. Ожидайте!",
            true),
        NotifItem("Заказ №56787 доставляется",
            "Ваш заказ на сумму 1300 руб. доставляется курьером по адресу Москва, ул. Тверская, 7. Ожидайте!",
            true),
        NotifItem("Заказ №56787 доставляется",
            "Ваш заказ на сумму 1300 руб. доставляется курьером по адресу Москва, ул. Тверская, 7. Ожидайте!",
            false),
        NotifItem("Заказ №56787 доставляется",
            "Ваш заказ на сумму 1300 руб. доставляется курьером по адресу Москва, ул. Тверская, 7. Ожидайте!",
            false),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = list[position].title
            holder.bindingItem?.tvText?.text = list[position].text

            if (list[position].isNew){
                holder.bindingItem?.ivCircle?.visibility = View.VISIBLE
                holder.bindingItem?.tvTitle?.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }else{
                holder.bindingItem?.ivCircle?.visibility = View.GONE
                holder.bindingItem?.tvTitle?.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
