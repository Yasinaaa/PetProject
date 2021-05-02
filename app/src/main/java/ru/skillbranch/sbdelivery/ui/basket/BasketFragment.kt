package ru.skillbranch.sbdelivery.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Loading

class BasketFragment : BaseFragment<BasketViewModel>() {

    override val viewModel: BasketViewModel by stateViewModel()
    private var binding: FragmentBasketBinding? = null
    override val baseBinding: BasketBinding by lazy { BasketBinding() }
    private lateinit var basketAdapter: BasketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        viewModel.getBasket()
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.items.observe(viewLifecycleOwner, {
            basketAdapter = BasketAdapter(it)
            binding?.rvItems?.adapter = basketAdapter
        })

        viewModel.cart.observe(viewLifecycleOwner, {

        })
    }

    override fun renderLoading(loadingState: Loading) {
        super.renderLoading(loadingState)
        when(loadingState){
            Loading.SHOW_LOADING, Loading.SHOW_BLOCKING_LOADING -> {
                binding?.etPromocode?.visibility = GONE
                binding?.mbApply?.visibility = GONE
                binding?.tvDiscount?.visibility = GONE
                binding?.tvTotal?.visibility = GONE
                binding?.tvTotalPrice?.visibility = GONE
                binding?.mbProceedToCheckout?.visibility = GONE
            }
            Loading.HIDE_LOADING -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class BasketBinding : Binding() {

        override fun bind(data: IViewModelState) {
            data as BasketState

        }
    }
}