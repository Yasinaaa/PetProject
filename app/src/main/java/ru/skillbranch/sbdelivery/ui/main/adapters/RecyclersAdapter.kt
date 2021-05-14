package ru.skillbranch.sbdelivery.ui.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemMainRvBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter.Companion.MAIN


open class RecyclersAdapter(
    private val lifecycleOwner: Lifecycle,
    private val click: (CardItem) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemMainRvBinding? = DataBindingUtil.bind(view)
    }

    var list: MutableList<RecyclerItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_main_rv, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
//            if(list[position].cards.isNullOrEmpty()) {
//                holder.bindingItem?.cl?.visibility = GONE
//                holder.bindingItem?.root?.visibility = GONE
//            }else{
                holder.bindingItem?.cl?.visibility = VISIBLE

                holder.bindingItem?.tvTitle?.text = context.getString(list[position].title)
                val adapter = CardAdapter(type = MAIN){
                    click.invoke(it)
                }
                //, list[position].cards
//                if (list[position].cards.filter {
//
//                    } < 10)
//                    holder.bindingItem?.tvSeeAll?.visibility = GONE
                holder.bindingItem?.rvItems?.adapter = adapter
                adapter.submitData(lifecycleOwner, list[position].cards)
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}