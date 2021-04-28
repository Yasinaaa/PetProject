package ru.skillbranch.sbdelivery.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclersAdapter
import ru.skillbranch.sbdelivery.ui.search.SearchFragmentDirections

class MainFragment : BaseFragment<MainViewModel>() {

    override val viewModel: MainViewModel by stateViewModel()
    override val baseBinding: MainBinding by lazy { MainBinding() }

    private var binding: FragmentMainBinding? = null
    private lateinit var recyclerViewsAdapter: RecyclersAdapter
    //private val recyclerViewsAdapter by lazy { RecyclerViewsDelegate().createAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        recyclerViewsAdapter = RecyclersAdapter{
            val action = SearchFragmentDirections.dishPage(it.id)
            viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        rootView = binding!!.root
        binding!!.rv.adapter = recyclerViewsAdapter
        //findNavController().navigate(R.id.nav_menu)
        return rootView
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupViews() {
        viewModel.recommendDishesLists(viewLifecycleOwner){
            recyclerViewsAdapter.list = it
            recyclerViewsAdapter.notifyDataSetChanged()
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
            data as MainState
        }
    }

}