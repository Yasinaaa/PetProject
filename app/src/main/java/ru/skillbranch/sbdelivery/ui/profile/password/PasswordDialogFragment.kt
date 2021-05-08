package ru.skillbranch.sbdelivery.ui.profile.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.DialogChangePasswordBinding
import ru.skillbranch.sbdelivery.databinding.DialogReviewBinding
import ru.skillbranch.sbdelivery.ui.base.BaseDialogFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.profile.ProfileState
import ru.skillbranch.sbdelivery.ui.profile.password.PasswordDialogViewModel.Companion.NEW_PASSWORD
import ru.skillbranch.sbdelivery.ui.profile.password.PasswordDialogViewModel.Companion.OLD_PASSWORD
import ru.skillbranch.sbdelivery.utils.RenderProp

//todo base DialogFragment class
class PasswordDialogFragment: BaseDialogFragment<PasswordDialogViewModel>() {

    private var binding: DialogChangePasswordBinding? = null
    override val baseBinding: Binding? by lazy { PasswordBinding() }
    override fun getTheme() = R.style.RoundedCornersDialog
    override val viewModel: PasswordDialogViewModel by stateViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChangePasswordBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        requireActivity().window.setDimAmount(0f)
        requireActivity().window.setBackgroundDrawableResource(android.R.color.transparent)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePasswordField(binding?.tietOldPassword.obs(), OLD_PASSWORD)
        viewModel.observePasswordField(binding?.tietNewPassword.obs(), NEW_PASSWORD)
    }

    inner class PasswordBinding : Binding() {

        private var isNewPasswordSuccess: Boolean by RenderProp(true){
            checkEnableBtn()
        }

        private var isOldPasswordSuccess: Boolean by RenderProp(true){
            checkEnableBtn()
        }

        var oldPassword: String by RenderProp(""){
            binding?.tietOldPassword?.setText(it)
            checkEnableBtn()
        }

        var newPassword: String by RenderProp(""){
            binding?.tietNewPassword?.setText(it)
            checkEnableBtn()
        }

        private fun checkEnableBtn(){
            binding?.mbSave?.isEnabled = newPassword.isNotEmpty() && oldPassword.isNotEmpty()
        }

        override fun bind(data: IViewModelState) {
            data as PasswordDialogState
            oldPassword = data.oldPassword
            newPassword = data.newPassword
            isOldPasswordSuccess = data.isSuccessOldPassword
            isNewPasswordSuccess = data.isSuccessNewPassword
        }
    }
}