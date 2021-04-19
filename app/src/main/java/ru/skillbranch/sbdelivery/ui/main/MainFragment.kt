package ru.skillbranch.sbdelivery.ui.main

import android.os.Bundle
import android.view.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerViewsDelegate

class MainFragment : BaseFragment<MainViewModel>() {

//    private var recyclersList: MutableList<RecyclerItem> = mutableListOf(
//        RecyclerItem(R.string.recommend),
//        RecyclerItem(R.string.best),
//        RecyclerItem(R.string.popular)
//    )

    override val viewModel: MainViewModel by stateViewModel()
    override val baseBinding: MainBinding by lazy { MainBinding() }

    private var binding: FragmentMainBinding? = null
    private val recyclerViewsAdapter by lazy { RecyclerViewsDelegate().createAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val recyclersList: MutableList<RecyclerItem> = mutableListOf(
            RecyclerItem(R.string.recommend),
            RecyclerItem(R.string.promotions),
            RecyclerItem(R.string.popular),
        )
        recyclerViewsAdapter.items = recyclersList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        rootView = binding!!.root
        return rootView
    }

    override fun setupViews() {
        viewModel.recommendDishesLists(viewLifecycleOwner){
            recyclerViewsAdapter.items[0].cards = it
            binding!!.rv.adapter = recyclerViewsAdapter
        }
        viewModel.bestDishesLists(viewLifecycleOwner){
            recyclerViewsAdapter.items[1].cards = it
        }
        viewModel.mostLikedDishesLists(viewLifecycleOwner){
            recyclerViewsAdapter.items[2].cards = it
        }
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