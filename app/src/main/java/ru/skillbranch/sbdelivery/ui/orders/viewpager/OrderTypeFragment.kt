package ru.skillbranch.sbdelivery.ui.orders.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.data.dto.OrderDto
import ru.skillbranch.sbdelivery.databinding.FragmentNotificationsBinding

import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.orderdetails.OrderDetailsFragmentDirections
import ru.skillbranch.sbdelivery.utils.toDp
import java.util.ArrayList

class OrderTypeFragment : BaseFragment<OrderTypeViewModel>() {

    override val viewModel: OrderTypeViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { OrderTypeBinding() }
    private var binding: FragmentNotificationsBinding? = null
    private var adapter: OrderItemsAdapter = OrderItemsAdapter{
//        requireParentFragment().arguments?.apply {
//            putString(ORDER_ID, it.id)
//        }
        viewModel.openOrderPage(it.id)
    }

    companion object{
        const val ORDERS_LIST = "ORDERS_LIST"
        const val ORDER_ID = "ORDER_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun setupViews() {
        binding?.rvItems?.setPadding(0, 6.toDp(requireContext()), 0, 16.toDp(requireContext()))
        binding?.rvItems?.adapter = adapter
        arguments?.takeIf { it.containsKey(ORDERS_LIST) }?.apply {
            adapter.updateList(getParcelableArrayList(ORDERS_LIST))
        }
    }

    inner class OrderTypeBinding : Binding() {
        override fun bind(data: IViewModelState) {

        }
    }
}