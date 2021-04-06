package ru.skillbranch.sbdelivery.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import ru.skillbranch.sbdelivery.databinding.FragmentCategoryBinding
import ru.skillbranch.sbdelivery.ui.orders.viewpager.OrdersAdapter

class OrdersFragment : Fragment() {

    private lateinit var viewModel: OrdersViewModel
    private var binding: FragmentCategoryBinding? = null
    private var ordersAdapter: OrdersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        ordersAdapter = OrdersAdapter(this)
        binding?.viewPager?.adapter = ordersAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(binding?.tabs!!, binding?.viewPager!!) { tab, position ->
            tab.text = requireContext().getString(ordersAdapter!!.items[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}