package ru.skillbranch.sbdelivery.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentProfileBinding
import ru.skillbranch.sbdelivery.databinding.FragmentSignBinding
import ru.skillbranch.sbdelivery.ui.profile.password.PasswordDialogFragment

class SignFragment : Fragment() {

    private lateinit var viewModel: SignViewModel
    private var binding: FragmentSignBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SignViewModel::class.java)

        binding = FragmentSignBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}