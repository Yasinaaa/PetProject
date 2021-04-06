package ru.skillbranch.sbdelivery.ui.orderdetails.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentInputAddressBinding
import ru.skillbranch.sbdelivery.databinding.FragmentMapBinding
import ru.skillbranch.sbdelivery.ui.adapters.TextLineAdapter

class MapAddressFragment : Fragment() {

    private lateinit var viewModel: MapAddressViewModel
    private var binding: FragmentMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MapAddressViewModel::class.java)

        binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}