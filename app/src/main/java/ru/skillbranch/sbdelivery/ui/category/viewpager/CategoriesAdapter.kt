package ru.skillbranch.sbdelivery.ui.category.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CategoriesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val items = mutableListOf("Суши", "Роллы", "Сашими", "Гунканы", "Суши", "Роллы", "Сашими", "Гунканы")

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        val fragment = SingleCategoryFragment()
        fragment.arguments = Bundle().apply {
            //putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}