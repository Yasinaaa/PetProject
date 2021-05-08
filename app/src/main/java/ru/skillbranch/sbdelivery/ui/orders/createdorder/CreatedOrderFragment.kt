package ru.skillbranch.sbdelivery.ui.orders.createdorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.data.dto.OrderItemDto
import ru.skillbranch.sbdelivery.databinding.FragmentCreatedOrderBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.utils.RenderProp
import ru.skillbranch.sbdelivery.utils.parseMilliseconds
import ru.skillbranch.sbdelivery.utils.removeZeroAndReplaceComma

class CreatedOrderFragment : BaseFragment<CreatedOrderViewModel>() {

    override val viewModel: CreatedOrderViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { CreatedOrderBinding() }
    private var binding: FragmentCreatedOrderBinding? = null
    private val args: CreatedOrderFragmentArgs by navArgs()
    private val adapter: CreatedOrderAdapter = CreatedOrderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatedOrderBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.getOrder(args.orderId)
        binding?.rvItems?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class CreatedOrderBinding : Binding() {

        private var returnBack: Boolean by RenderProp(false){
            if(it) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("update", true)
                findNavController().popBackStack()
            }
        }

        private var status: String by RenderProp(""){
            binding?.tvStatus?.text = it
        }

        private var price: Float by RenderProp(0f){
            binding?.tvTotalPrice?.text = String.format(
                requireContext().getString(R.string.rub),
                it.removeZeroAndReplaceComma()
            )
        }

        private var address: String by RenderProp(""){
            binding?.tvAddress?.text = it
        }

        private var date: String by RenderProp(""){
            binding?.tvDate?.text = it.parseMilliseconds()
        }

        private var items: MutableList<OrderItemDto> by RenderProp(mutableListOf()) {
            adapter.update(it)
        }

        override fun bind(data: IViewModelState) {
            data as CreatedOrderState
            status = data.status
            price = data.price
            address = data.address
            date = data.date
            items = data.items
            returnBack = data.returnBack
        }
    }
}