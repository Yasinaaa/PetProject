package ru.skillbranch.sbdelivery.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

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
        //binding?.rvItems?.adapter = BasketAdapter()
        return binding!!.root
    }

    override fun setupViews() {

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