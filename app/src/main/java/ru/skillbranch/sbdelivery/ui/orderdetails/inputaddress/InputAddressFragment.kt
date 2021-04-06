package ru.skillbranch.sbdelivery.ui.orderdetails.inputaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentInputAddressBinding
import ru.skillbranch.sbdelivery.databinding.FragmentNotificationsBinding
import ru.skillbranch.sbdelivery.ui.adapters.TextLineAdapter

class InputAddressFragment : Fragment() {

    private lateinit var menuViewModel: InputAddressViewModel
    private var binding: FragmentInputAddressBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        menuViewModel = ViewModelProvider(this).get(InputAddressViewModel::class.java)

        binding = FragmentInputAddressBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        binding?.rvAddresses?.adapter = TextLineAdapter(type=TextLineAdapter.ADDRESS)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}