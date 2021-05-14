package ru.skillbranch.sbdelivery.ui.search

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.databinding.FragmentSearchBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import ru.skillbranch.sbdelivery.utils.RenderProp

class SearchFragment : BaseFragment<SearchViewModel>() {

    override val viewModel: SearchViewModel by stateViewModel()
    override val baseBinding: SearchBinding by lazy { SearchBinding() }
    private var binding: FragmentSearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        val searchEvent = binding?.searchView?.queryTextChanges()?.skipInitialValue()?.map { it.toString() }
        viewModel.searchText(searchEvent!!)
        return root
    }

    override fun setupViews() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class SearchBinding : Binding() {

        private var foundCategories: MutableList<CategoryEntity>
        by RenderProp(mutableListOf()) {
            if(it.isNotEmpty()) {
                binding?.rvCache?.visibility = GONE
                binding?.rvCategories?.visibility = VISIBLE
                binding?.rvCategories?.adapter = CategoriesAdapter(it)
            }
        }

        private var foundDishes: MutableList<CardItem> by RenderProp(mutableListOf()) {
            if(it.isNotEmpty()) {
                binding?.rvCache?.visibility = GONE
                binding?.rvProducts?.visibility = VISIBLE
                //TODO
//                binding?.rvProducts?.adapter =
//                    CardAdapter(type = CardAdapter.FAVORITE, it) { item ->
//                    val action = SearchFragmentDirections.dishPage(item.id)
//                    viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
//                }
            }
        }

        private var searchHistory: MutableSet<String> by RenderProp(mutableSetOf()) {
            binding?.rvCache?.adapter = SearchAdapter(it)
            binding?.rvCache?.visibility = VISIBLE
        }

        override fun bind(data: IViewModelState) {
            data as SearchState
            searchHistory = data.searchHistory
            foundDishes = data.dishes
            foundCategories = data.foundCategories
        }
    }

}