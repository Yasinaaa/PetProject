package ru.skillbranch.sbdelivery.ui.orders.createdorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import ru.skillbranch.sbdelivery.databinding.FragmentCategoryBinding
import ru.skillbranch.sbdelivery.databinding.FragmentCreatedOrderBinding
import ru.skillbranch.sbdelivery.ui.orders.viewpager.OrdersAdapter

class CreatedOrderFragment : Fragment() {

    private lateinit var viewModel: CreatedOrderViewModel
    private var binding: FragmentCreatedOrderBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CreatedOrderViewModel::class.java)

        binding = FragmentCreatedOrderBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        binding?.rvItems?.adapter = CreatedOrderAdapter()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}