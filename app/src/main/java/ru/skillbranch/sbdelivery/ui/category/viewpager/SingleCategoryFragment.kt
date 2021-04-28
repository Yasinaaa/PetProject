package ru.skillbranch.sbdelivery.ui.category.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.skillbranch.sbdelivery.databinding.FragmentFavoriteBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter

class SingleCategoryFragment : Fragment() {

    private var binding: FragmentFavoriteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        binding?.rvItems?.adapter = CardAdapter(type=CardAdapter.CATEGORY){

        }
    }
}