package ru.skillbranch.sbdelivery.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.widget.textChanges
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentSignBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Notify
import ru.skillbranch.sbdelivery.utils.RenderProp
import ru.skillbranch.sbdelivery.utils.toDp

class SignFragment : BaseFragment<SignViewModel>() {

    override val baseBinding: Binding? by lazy { SignBinding() }
    override val viewModel: SignViewModel by stateViewModel()
    private var binding: FragmentSignBinding? = null
    private val args: SignFragmentArgs by navArgs()
    private var currentType: Int = 0

    companion object{
        const val SIGN_IN = 0
        const val SIGN_UP = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        currentType = args.type
        return binding!!.root
    }

    override fun setupViews() {
        viewModel.observeEmailField(binding?.tietEmail?.obs(), currentType)
        viewModel.observePasswordField(binding?.tietPassword?.obs())

        if (currentType == SIGN_IN){
            viewModel.init()

        }else if (currentType == SIGN_UP){
            viewModel.observeNameField(binding?.tietName?.obs(), SignViewModel.NAME_TYPE)
            viewModel.observeNameField(binding?.tietSurname?.obs(), SignViewModel.SURNAME_NAME_TYPE)

            binding?.tilName?.visibility = VISIBLE
            binding?.tvName?.visibility = VISIBLE
            binding?.tilSurname?.visibility = VISIBLE
            binding?.tvSurname?.visibility = VISIBLE
            binding?.tvForgetPassword?.visibility = GONE
            binding?.mbEnter?.text = getString(R.string.to_sign_up)
            binding?.mbCreateAccount?.text = getString(R.string.sign_in)

            changeTopConnection(binding?.tvEmail)
            changeTopConnection(binding?.tvEmailError)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.
        getLiveData<Boolean>("backb")?.observe(
            viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun changeTopConnection(tv: TextView?){
        val params = tv?.layoutParams as ConstraintLayout.LayoutParams
        params.topToTop = ConstraintLayout.LayoutParams.UNSET
        params.topToBottom = binding?.tilSurname?.id!!
        params.topMargin = 16.toDp(requireContext())
        tv.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class SignBinding : Binding() {

        private var isInit: Boolean by RenderProp(true)

        private var returnBack: String by RenderProp(""){
            when {
                it == "back" -> {
                    findNavController().popBackStack()
                }
                it == "backb" -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("backb", true)
                    findNavController().popBackStack()
                }
                it != "" -> {
                    Snackbar.make(view!!, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        private var isSurnameSuccess: Boolean by RenderProp(true){
            showErrorOrSuccessView(it, binding?.tvSurname, binding?.tvSurnameError,
                binding?.tilSurname)
        }

        private var isNameSuccess: Boolean by RenderProp(true){
            showErrorOrSuccessView(it, binding?.tvName, binding?.tvNameError,
                binding?.tilName)
        }

        private var isEmailSuccess: Boolean by RenderProp(false){
            showErrorOrSuccessView(it, binding?.tvEmail, binding?.tvEmailError,
                binding?.tilEmail)
        }

        private var isPasswordSuccess: Boolean by RenderProp(false){
            showErrorOrSuccessView(it, binding?.tvPassword, binding?.tvPasswordError,
                binding?.tilPassword)
        }

        private fun showErrorOrSuccessView(
            condition: Boolean, tv: TextView?,
            errorTv: TextView?, til: TextInputLayout?
        ){
            if(condition){
                errorTv?.visibility = GONE
                tv?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                til?.setBoxStrokeColorStateList(
                    AppCompatResources.getColorStateList(requireContext(), R.color.white))
            }else{
                if (currentType == SIGN_UP) {
                    errorTv?.visibility = VISIBLE
                    tv?.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    til?.setBoxStrokeColorStateList(
                        AppCompatResources.getColorStateList(requireContext(), R.color.red)
                    )
                }else{
                    errorTv?.visibility = GONE
                }
            }
            checkSignInBtn()
        }

        private fun checkSignInBtn(){
            if (currentType == SIGN_IN){
                binding?.mbEnter?.isEnabled = isPasswordSuccess && isEmailSuccess
            }else if (currentType == SIGN_UP){
                binding?.mbEnter?.isEnabled =
                    isNameSuccess && isSurnameSuccess && isPasswordSuccess && isEmailSuccess
            }
        }

        override fun bind(data: IViewModelState) {
            data as SignState
            isInit = data.isInit
            isNameSuccess = data.isSuccessName
            isSurnameSuccess = data.isSuccessSurname
            isEmailSuccess = data.isSuccessEmail
            isPasswordSuccess = data.isSuccessPassword
            returnBack = data.returnBack
        }
    }
}