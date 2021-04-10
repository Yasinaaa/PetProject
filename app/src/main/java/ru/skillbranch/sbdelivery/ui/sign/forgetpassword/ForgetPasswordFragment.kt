package ru.skillbranch.sbdelivery.ui.sign.forgetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentForgetPasswordBinding
import ru.skillbranch.sbdelivery.databinding.FragmentSignBinding

class ForgetPasswordFragment : Fragment() {

    private lateinit var viewModel: ForgetPasswordViewModel
    private var binding: FragmentForgetPasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ForgetPasswordViewModel::class.java)

        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}