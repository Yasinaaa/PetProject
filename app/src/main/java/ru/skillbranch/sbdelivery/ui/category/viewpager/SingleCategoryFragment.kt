package ru.skillbranch.sbdelivery.ui.category.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentFavoriteBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class SingleCategoryFragment : BaseFragment<SingleCategoryViewModel>() {

    override val viewModel: SingleCategoryViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { SingleCategoryBinding() }
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
        //        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        binding?.rvItems?.adapter = CardAdapter(type=CardAdapter.CATEGORY){

        }
    }

    inner class SingleCategoryBinding : Binding() {
        override fun bind(data: IViewModelState) {

        }
    }
}