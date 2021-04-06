package ru.skillbranch.sbdelivery.ui.orders.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.skillbranch.sbdelivery.databinding.FragmentFavoriteBinding
import ru.skillbranch.sbdelivery.databinding.FragmentNotificationsBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
import ru.skillbranch.sbdelivery.utils.toDp

class OrderTypeFragment : Fragment() {

    private var binding: FragmentNotificationsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        binding?.rvItems?.setPadding(0, 6.toDp(requireContext()), 0,
            16.toDp(requireContext()))
        binding?.rvItems?.adapter = OrderItemsAdapter()
    }
}