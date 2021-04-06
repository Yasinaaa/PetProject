package ru.skillbranch.sbdelivery.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentProfileBinding
import ru.skillbranch.sbdelivery.ui.profile.password.PasswordDialogFragment

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        showChangePasswordDialog()

        return root
    }

    private fun showChangePasswordDialog(){
        val reviewDialog = PasswordDialogFragment()
        reviewDialog.show(parentFragmentManager, "password")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}