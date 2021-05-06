package ru.skillbranch.sbdelivery.ui.orderdetails.inputaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentInputAddressBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.utils.RenderProp
import ru.skillbranch.sbdelivery.data.remote.models.response.Address
import ru.skillbranch.sbdelivery.ui.orderdetails.OrderDetailsFragment.Companion.DELIVERY_ADDRESS

class InputAddressFragment : BaseFragment<InputAddressViewModel>() {

    override val viewModel: InputAddressViewModel by stateViewModel()
    private var binding: FragmentInputAddressBinding? = null
    override val baseBinding: Binding? by lazy { InputAddressBinding() }
    private var adapter: AddressAdapter = AddressAdapter{
        hideKeyboard()
        findNavController().previousBackStackEntry?.savedStateHandle
            ?.set(DELIVERY_ADDRESS, it.string(requireContext()))
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputAddressBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.searchAddress(binding?.tietAddress.obs())
        binding?.rvAddresses?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class InputAddressBinding : Binding() {

        private var foundAddresses: MutableList<Address> by RenderProp(mutableListOf()) {
            adapter.setList(it)
        }

        override fun bind(data: IViewModelState) {
            data as InputAddressState
            foundAddresses = data.addresses
        }
    }
}