package ru.skillbranch.sbdelivery.ui.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentBasketBinding
import ru.skillbranch.sbdelivery.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : Fragment() {

    private lateinit var viewModel: OrderDetailsViewModel
    private var binding: FragmentOrderDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)

        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}