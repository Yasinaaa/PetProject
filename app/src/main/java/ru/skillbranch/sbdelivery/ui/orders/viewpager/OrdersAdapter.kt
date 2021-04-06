package ru.skillbranch.sbdelivery.ui.orders.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.sbdelivery.R

class OrdersAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val items = mutableListOf(R.string.current_orders, R.string.completed_orders)

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = OrderTypeFragment()
        fragment.arguments = Bundle().apply {
            //putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}