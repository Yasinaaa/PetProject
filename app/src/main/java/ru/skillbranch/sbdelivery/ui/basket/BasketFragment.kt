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
import ru.skillbranch.sbdelivery.utils.RenderProp

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
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.getBasket()

        viewModel.items.observe(viewLifecycleOwner, {
            basketAdapter = BasketAdapter(it){ item, numChanger, size ->
                if (item != null && numChanger != null)
                    viewModel.updateItem(item, numChanger)
                else if(size == 0){
                    baseBinding.isEmptyCart = true
                }
            }
            binding?.rvItems?.adapter = basketAdapter
        })

        viewModel.cart.observe(viewLifecycleOwner, {

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        onBackPressedDispatcher.addCallback(
//            this,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    onBackPressed()
//                }
//            }
//        )

    }

//    override fun renderLoading(loadingState: Loading) {
//        super.renderLoading(loadingState)
//        when(loadingState){
//            Loading.SHOW_LOADING, Loading.SHOW_BLOCKING_LOADING -> {
//
//            }
//            Loading.HIDE_LOADING -> {
//
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class BasketBinding : Binding() {

        var isEmptyCart: Boolean by RenderProp(false){
            if(it){
                binding?.tvEmptyBasket?.visibility = VISIBLE
            }else{
                binding?.tvEmptyBasket?.visibility = GONE
            }
        }

        override fun bind(data: IViewModelState) {
            data as BasketState
            isEmptyCart = data.isEmptyCart
        }
    }
}