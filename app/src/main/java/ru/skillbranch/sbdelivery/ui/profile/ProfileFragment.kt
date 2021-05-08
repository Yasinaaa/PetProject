package ru.skillbranch.sbdelivery.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentProfileBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.orders.OrdersViewModel
import ru.skillbranch.sbdelivery.ui.profile.password.PasswordDialogFragment
import ru.skillbranch.sbdelivery.ui.sign.SignFragment
import ru.skillbranch.sbdelivery.ui.sign.SignViewModel
import ru.skillbranch.sbdelivery.utils.RenderProp

class ProfileFragment : BaseFragment<ProfileViewModel>() {

    override val viewModel: ProfileViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { ProfileBinding() }
    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.observeEmailField(binding?.tietEmail?.obs()!!)
        viewModel.observeNameField(binding?.tietName?.obs()!!, SignViewModel.NAME_TYPE)
        viewModel.observeNameField(binding?.tietSurname?.obs()!!, SignViewModel.SURNAME_NAME_TYPE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class ProfileBinding : Binding() {

        private var isSurnameSuccess: Boolean by RenderProp(true){
            showErrorOrSuccessView(it, binding?.tvSurname, binding?.tvSurnameError,
                binding?.tifSurname)
        }

        private var isNameSuccess: Boolean by RenderProp(true){
            showErrorOrSuccessView(it, binding?.tvName, binding?.tvNameError,
                binding?.tifName)
        }

        private var isEmailSuccess: Boolean by RenderProp(false){
            showErrorOrSuccessView(it, binding?.tvEmail, binding?.tvEmailError,
                binding?.tilEmail)
        }

        var name: String by RenderProp(""){
            binding?.tietName?.setText(it)
            checkEnableBtn()
        }

        var surname: String by RenderProp(""){
            binding?.tietSurname?.setText(it)
            checkEnableBtn()
        }

        var email: String by RenderProp(""){
            binding?.tietEmail?.setText(it)
            checkEnableBtn()
        }

        private fun checkEnableBtn(){
            binding?.mbChange?.isEnabled = name.isNotEmpty() && surname.isNotEmpty()
                    && email.isNotEmpty()
        }

        private fun showErrorOrSuccessView(
            condition: Boolean, tv: TextView?,
            errorTv: TextView?, til: TextInputLayout?
        ){
            if(condition){
                errorTv?.visibility = View.GONE
                tv?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                til?.setBoxStrokeColorStateList(
                    AppCompatResources.getColorStateList(requireContext(), R.color.white))
            }else{
                errorTv?.visibility = View.VISIBLE
                tv?.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                til?.setBoxStrokeColorStateList(
                    AppCompatResources.getColorStateList(requireContext(), R.color.red)
                )
            }
            checkEnableBtn()
        }

        override fun bind(data: IViewModelState) {
            data as ProfileState
            email = data.email
            surname = data.surname
            name = data.name

            isEmailSuccess = data.isSuccessEmail
            isNameSuccess = data.isSuccessName
            isSurnameSuccess = data.isSuccessSurname
        }
    }
}