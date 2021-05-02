package ru.skillbranch.sbdelivery.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import io.reactivex.rxjava3.core.Observable
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.data.dto.CartWithImageDto
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
        binding?.viewModel = viewModel
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.getBasket()

        viewModel.cart.observe(viewLifecycleOwner, {
            basketAdapter = BasketAdapter(it.cart, it.items, object : BasketListener{
                override fun updateCount(dto: CartWithImageDto, obs: Observable<Int>?) {
                    viewModel.updateItem(dto, obs)
                }

                override fun updateTotalPrice(totalPrice: String) {
                    binding?.tvTotalPrice?.text = totalPrice
                }

                override fun setEmptyList() {
                    baseBinding.isEmptyCart = true
                }
            })
            binding?.rvItems?.adapter = basketAdapter
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class BasketBinding : Binding() {

        var isEmptyCart: Boolean by RenderProp(false){
            if(it){
                showItems(VISIBLE)
            }else{
                showItems(GONE)
            }
        }

        private fun showItems(v: Int){
            binding?.tvEmptyBasket?.visibility = v

            val newV = if (v == GONE){
                VISIBLE
            }else{
                GONE
            }
            binding?.tvTotal?.visibility = newV
            binding?.tvTotalPrice?.visibility = newV
            binding?.etPromocode?.visibility = newV
            binding?.mbApply?.visibility = newV
            binding?.mbProceedToCheckout?.visibility = newV
        }

        override fun bind(data: IViewModelState) {
            data as BasketState
            isEmptyCart = data.isEmptyCart
        }
    }
}