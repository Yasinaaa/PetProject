package ru.skillbranch.sbdelivery.ui.main

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerViewsDelegate
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclersAdapter
import ru.skillbranch.sbdelivery.ui.root.RootActivity
import ru.skillbranch.sbdelivery.utils.RenderProp

class MainFragment : BaseFragment<MainViewModel>() {

    private var recyclersList: MutableList<RecyclerItem> = mutableListOf(
        RecyclerItem(R.string.recommend),
        RecyclerItem(R.string.best),
        RecyclerItem(R.string.popular)
    )

    override val viewModel: MainViewModel by stateViewModel()
    override val baseBinding: MainBinding by lazy { MainBinding() }

    private var binding: FragmentMainBinding? = null
    private val recyclerViewsAdapter by lazy { RecyclerViewsDelegate().createAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        rootView = binding!!.root

        with(binding!!.rv){
            adapter = recyclerViewsAdapter
            recyclerViewsAdapter.items = recyclersList
        }

        viewModel.getRecommended()
        //viewModel.getFavorite()
        return rootView
    }

    override fun setupViews() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_basket -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class MainBinding : Binding() {

        override fun bind(data: IViewModelState) {

        }
    }

}