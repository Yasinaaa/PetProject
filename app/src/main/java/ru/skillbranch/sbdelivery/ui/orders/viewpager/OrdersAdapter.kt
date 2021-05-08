package ru.skillbranch.sbdelivery.ui.orders.viewpager

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.ui.orders.viewpager.OrderTypeFragment.Companion.ORDERS_LIST

class OrdersAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = mutableListOf(OrderTypeFragment(), OrderTypeFragment())
    val items = mutableListOf(R.string.current_orders, R.string.completed_orders)

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addCompletedFragment(list: MutableList<OrderDto>){
        updateFragment(fragments[1], list)
        notifyItemChanged(1)
    }

    fun addNotCompletedFragment(list: MutableList<OrderDto>){
        updateFragment(fragments[0], list)
        notifyItemChanged(0)
    }

    private fun updateFragment(fragment: Fragment, list: MutableList<OrderDto>): Fragment{
        fragment.arguments = Bundle().apply {
            putParcelableArrayList(ORDERS_LIST, ArrayList<Parcelable>(list))
        }
        return fragment
    }
}