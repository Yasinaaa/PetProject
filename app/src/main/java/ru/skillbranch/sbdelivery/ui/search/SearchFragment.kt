package ru.skillbranch.sbdelivery.ui.search

import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentSearchBinding
import ru.skillbranch.sbdelivery.ui.main.adapters.CardAdapter

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
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
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

//        binding?.rvCache?.adapter = CacheAdapter()
//        binding?.rvCache?.visibility = VISIBLE

        binding?.rvCategories?.adapter = CategoriesAdapter()
        binding?.rvProducts?.adapter = CardAdapter(type= CardAdapter.FAVORITE)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}