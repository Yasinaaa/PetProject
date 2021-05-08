package ru.skillbranch.sbdelivery.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.data.dto.ReviewDto
import ru.skillbranch.sbdelivery.databinding.FragmentCategoryBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.orders.viewpager.OrderTypeViewModel
import ru.skillbranch.sbdelivery.ui.orders.viewpager.OrdersAdapter
import ru.skillbranch.sbdelivery.utils.RenderProp

class OrdersFragment : BaseFragment<OrdersViewModel>() {

    override val viewModel: OrdersViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { OrdersBinding() }
    private var binding: FragmentCategoryBinding? = null
    private var ordersAdapter: OrdersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun setupViews() {
        ordersAdapter = OrdersAdapter(this)
        binding?.viewPager?.adapter = ordersAdapter
        TabLayoutMediator(binding?.tabs!!, binding?.viewPager!!) { tab, position ->
            tab.text = requireContext().getString(ordersAdapter!!.items[position])
        }.attach()
        viewModel.getOrders()

        findNavController().currentBackStackEntry?.savedStateHandle?.
        getLiveData<Boolean>("update")?.observe(
            viewLifecycleOwner) {
            if (it)
                viewModel.getOrders()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class OrdersBinding : Binding() {

        private var notCompleted: MutableList<OrderDto> by RenderProp(mutableListOf()) {
            ordersAdapter?.addNotCompletedFragment(it)
        }

        private var completed: MutableList<OrderDto> by RenderProp(mutableListOf()) {
            ordersAdapter?.addCompletedFragment(it)
        }

        override fun bind(data: IViewModelState) {
            data as OrderState
            notCompleted = data.notCompleted
            completed = data.completed
        }
    }
}