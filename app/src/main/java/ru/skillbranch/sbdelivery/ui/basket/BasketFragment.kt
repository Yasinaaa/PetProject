package ru.skillbranch.sbdelivery.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding

class BasketFragment : Fragment() {

    private lateinit var viewModel: BasketViewModel
    private var binding: FragmentBasketBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(BasketViewModel::class.java)

        binding = FragmentBasketBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        binding?.rvItems?.adapter = BasketAdapter()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}