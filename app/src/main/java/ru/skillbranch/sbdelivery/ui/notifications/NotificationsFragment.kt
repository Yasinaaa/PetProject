package ru.skillbranch.sbdelivery.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.skillbranch.sbdelivery.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var menuViewModel: NotificationsViewModel
    private var binding: FragmentNotificationsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        menuViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        binding?.rvItems?.adapter = NotificationsAdapter()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}