package ru.skillbranch.sbdelivery.ui.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.paging.*
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ItemMainRvBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter.Companion.MAIN

interface RecyclersAdapterListener{
    fun click(c: CardItem)
    fun setEmpty()
}

open class RecyclersAdapter(
    private val lifecycleOwner: Lifecycle,
    private val listener: RecyclersAdapterListener

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    private var errorCount = 0

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bindingItem: ItemMainRvBinding? = DataBindingUtil.bind(view)
    }

    var list: MutableList<RecyclerItem> = mutableListOf(
        RecyclerItem(R.string.recommend),
        RecyclerItem(R.string.best),
        RecyclerItem(R.string.popular)
    )

    fun update(position: Int, data: PagingData<CardItem>){
        list[position].cards = data
        notifyItemChanged(position)
    }

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
            holder.bindingItem?.tvTitle?.text = context.getString(list[position].title)
            val adapter = CardAdapter(type = MAIN){
                listener.click(it)
            }
            holder.bindingItem?.rvItems?.apply {
                this.adapter = adapter
                adapter.addLoadStateListener {
                    if (it.refresh is LoadState.NotLoading){
                        if (adapter.itemCount >= 1) {
                            holder.bindingItem.cl.visibility = VISIBLE
                            holder.bindingItem.tvTitle.visibility = VISIBLE
                            holder.bindingItem.rvItems.visibility = VISIBLE
                            holder.bindingItem.tvSeeAll.visibility = VISIBLE
                        }
                    }
                    else{
                        val error = when {
                            it.prepend is LoadState.Error -> it.prepend as LoadState.Error
                            it.append is LoadState.Error -> it.append as LoadState.Error
                            it.refresh is LoadState.Error -> it.refresh as LoadState.Error
                            else -> null
                        }
                        error?.let {
                            errorCount += 1
                            if (errorCount >= list.size){
                                listener.setEmpty()
                            }
                        }

                    }

                }
            }

            if (list[position].cards != null) {
                adapter.submitData(lifecycleOwner, list[position].cards!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}