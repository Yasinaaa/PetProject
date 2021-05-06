package ru.skillbranch.sbdelivery.ui.orderdetails.inputaddress

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.remote.models.response.Address
import ru.skillbranch.sbdelivery.databinding.ItemCacheSearchBinding


open class AddressAdapter(
    private var list: MutableList<Address> = mutableListOf(),
    private val click: (Address) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemCacheSearchBinding? = DataBindingUtil.bind(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: MutableList<Address>){
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cache_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bindingItem?.tvTitle?.text = String.format(context.getString(R.string.address_input),
            list[position].city, list[position].street, list[position].house)
            holder.bindingItem?.ivClose?.visibility = GONE
            holder.bindingItem?.root?.setOnClickListener { click.invoke(list[position]) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}
