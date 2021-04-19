package ru.skillbranch.sbdelivery.ui.main.adapters

import android.view.View.GONE
import android.view.View.VISIBLE
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.skillbranch.sbdelivery.databinding.ItemMainRvBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter


class RecyclerViewsDelegate {

    fun createAdapter() = ListDelegationAdapter(createCategories())

    private fun createCategories() =
        adapterDelegateViewBinding<RecyclerItem, RecyclerItem, ItemMainRvBinding>({
                layoutInflater, parent ->
                    ItemMainRvBinding.inflate(layoutInflater, parent, false)
            })
        {
            bind {
                if(item.cards.size == 0) {
                    itemView.visibility = GONE
                    binding.cl.visibility = GONE
                    binding.root.visibility = GONE
                }else{
                    itemView.visibility = VISIBLE
                    binding.cl.visibility = VISIBLE
                    binding.tvTitle.text = context.getString(item.title)
                    val adapter = CardAdapter(type = CardAdapter.MAIN)
                    adapter.list = item.cards
                    binding.rvItems.adapter = adapter
                }
            }
        }
}