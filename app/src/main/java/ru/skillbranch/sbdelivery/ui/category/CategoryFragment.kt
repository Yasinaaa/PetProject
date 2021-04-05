package ru.skillbranch.sbdelivery.ui.category

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentCategoryBinding
import ru.skillbranch.sbdelivery.ui.category.viewpager.CategoriesAdapter

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private var binding: FragmentCategoryBinding? = null
    private var categoriesAdapter: CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        categoriesAdapter = CategoriesAdapter(this)
        binding?.viewPager?.adapter = categoriesAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TabLayoutMediator(binding?.tabs!!, binding?.viewPager!!) { tab, position ->
            tab.text = categoriesAdapter!!.items[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_category, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_category -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}