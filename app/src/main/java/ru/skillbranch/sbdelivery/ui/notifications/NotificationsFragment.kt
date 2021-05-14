package ru.skillbranch.sbdelivery.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.databinding.FragmentNotificationsBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class NotificationsFragment : BaseFragment<NotificationsViewModel>() {

    override val viewModel: NotificationsViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { NotificationBinding() }
    private var binding: FragmentNotificationsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun setupViews() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("TAG", "token=$token")
        })
        binding?.rvItems?.adapter = NotificationsAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class NotificationBinding : Binding() {
        override fun bind(data: IViewModelState) {

        }
    }
}