package ru.skillbranch.sbdelivery.ui.main.adapters

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.skillbranch.sbdelivery.databinding.ItemMainRvBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter


class RecyclerViewsDelegate {

    fun createAdapter() = ListDelegationAdapter(createCategories())

    private fun createCategories() =
        adapterDelegateViewBinding<RecyclerItem, RecyclerItem, ItemMainRvBinding>({ layoutInflater, parent ->
            ItemMainRvBinding.inflate(layoutInflater, parent, false)
        }) {
            bind {
                binding.tvTitle.text = context.getString(item.title)
                binding.rvItems.adapter = CardAdapter(type = CardAdapter.MAIN)
            }
        }
}