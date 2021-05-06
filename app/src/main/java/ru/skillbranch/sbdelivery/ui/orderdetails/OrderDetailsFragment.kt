package ru.skillbranch.sbdelivery.ui.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding
import ru.skillbranch.sbdelivery.databinding.FragmentOrderDetailsBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class OrderDetailsFragment : BaseFragment<OrderDetailsViewModel>() {

    override val baseBinding: Binding? by lazy { OrderDetailsBinding() }
    //private val args: OrderDetailsFragmentArgs by navArgs()
    override val viewModel: OrderDetailsViewModel by stateViewModel()
    private var binding: FragmentOrderDetailsBinding? = null

    companion object{
        const val DELIVERY_ADDRESS = "DELIVERY_ADDRESS"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        findNavController().currentBackStackEntry?.savedStateHandle?.
        getLiveData<String>(DELIVERY_ADDRESS)?.observe(viewLifecycleOwner) {
            viewModel.address.value = it
        }
        return binding!!.root
    }

    override fun setupViews() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class OrderDetailsBinding : Binding() {
        override fun bind(data: IViewModelState) {

        }
    }
}