package ru.skillbranch.sbdelivery.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.adapters.CardAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        recyclerViewsAdapter = RecyclersAdapter(lifecycle){
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
        //binding?.rv?.adapter = recyclerViewsAdapter
        return rootView
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupViews() {
        binding?.flShimmer?.startShimmer()

        viewModel.getJobs()
        viewModel.recommend.observe(viewLifecycleOwner, {
            val adapter = CardAdapter(type = CardAdapter.MAIN){
                //click.invoke(it)
            }
            binding?.rvItemsRec?.apply {
                this.adapter = adapter
                adapter.addLoadStateListener { loadState ->
                    if (adapter.itemCount >= 1) {
                        binding?.rvItemsRec?.visibility = View.VISIBLE
                        binding?.tvTitle?.visibility = View.VISIBLE
                        binding?.tvSeeAll?.visibility = View.VISIBLE
                    }

                    if (loadState.append.endOfPaginationReached) {
                        if (adapter.itemCount == 0) {
                            binding?.rvItemsRec?.visibility = View.GONE
                            binding?.tvTitle?.visibility = View.GONE
                            binding?.tvSeeAll?.visibility = View.GONE
                        }
                    }
                }
            }

            adapter.submitData(lifecycle, it)

            binding?.sivSb?.visibility = View.VISIBLE
            binding?.sivWallpaper?.visibility = View.VISIBLE
            binding?.flShimmer?.visibility = View.GONE
        })
        viewModel.best.observe(viewLifecycleOwner, {
            val adapter = CardAdapter(type = CardAdapter.MAIN){
                //click.invoke(it)
            }
            binding?.rvItemsBest?.apply {
                this.adapter = adapter
                adapter.addLoadStateListener { loadState ->
                    if (adapter.itemCount >= 1) {
                        binding?.rvItemsBest?.visibility = View.VISIBLE
                        binding?.tvBestTitle?.visibility = View.VISIBLE
                        binding?.tvSeeAllBest?.visibility = View.VISIBLE
                    }

                    if (loadState.append.endOfPaginationReached) {
                        if (adapter.itemCount < 1) {
                            binding?.rvItemsBest?.visibility = View.GONE
                            binding?.tvBestTitle?.visibility = View.GONE
                            binding?.tvSeeAllBest?.visibility = View.GONE
                        }
                    }
                }
            }

            adapter.submitData(lifecycle, it)
        })
        viewModel.popular.observe(viewLifecycleOwner, {
            val adapter = CardAdapter(type = CardAdapter.MAIN){
                //click.invoke(it)
            }
            binding?.rvItemsPopular?.apply {
                this.adapter = adapter
                adapter.addLoadStateListener { loadState ->
                    if (adapter.itemCount >= 1) {
                        binding?.rvItemsPopular?.visibility = View.VISIBLE
                        binding?.tvPopularTitle?.visibility = View.VISIBLE
                        binding?.tvSeeAllPopular?.visibility = View.VISIBLE
                    }

                    if (loadState.append.endOfPaginationReached) {
                        if (adapter.itemCount < 1) {
                            binding?.rvItemsPopular?.visibility = View.GONE
                            binding?.tvPopularTitle?.visibility = View.GONE
                            binding?.tvSeeAllPopular?.visibility = View.GONE
                        }
                    }
                }
            }

            adapter.submitData(lifecycle, it)
        })
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