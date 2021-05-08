package ru.skillbranch.sbdelivery.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentFavoriteBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter.Companion.FAVORITE
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class FavoriteFragment : BaseFragment<FavoriteViewModel>() {

    override val viewModel: FavoriteViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { FavoriteBinding() }
    private var binding: FragmentFavoriteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun setupViews() {
        binding?.rvItems?.adapter = CardAdapter(type=FAVORITE){

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class FavoriteBinding : Binding() {
        override fun bind(data: IViewModelState) {

        }
    }
}