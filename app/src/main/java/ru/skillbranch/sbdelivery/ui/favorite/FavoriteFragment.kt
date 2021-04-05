package ru.skillbranch.sbdelivery.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentFavoriteBinding
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.main.adapters.CardAdapter
import ru.skillbranch.sbdelivery.ui.main.adapters.CardAdapter.Companion.FAVORITE

class FavoriteFragment : Fragment() {

    private lateinit var menuViewModel: FavoriteViewModel
    private var binding: FragmentFavoriteBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        menuViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        binding?.rvItems?.adapter = CardAdapter(type=FAVORITE)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}