package ru.skillbranch.sbdelivery.ui.menu

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.java.KoinJavaComponent.inject
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentMenuBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment

class MenuFragment : BaseFragment<MenuViewModel>() {

    override val viewModel: MenuViewModel by stateViewModel()
    private var binding: FragmentMenuBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        return root
    }

    override fun setupViews() {
        viewModel.getCategories(viewLifecycleOwner){
            binding!!.rvItems.adapter = MenuAdapter(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search -> {
                findNavController().navigate(R.id.nav_search)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}