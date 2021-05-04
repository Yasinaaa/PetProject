package ru.skillbranch.sbdelivery.ui.sign.forgetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentForgetPasswordBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.utils.RenderProp
import ru.skillbranch.sbdelivery.utils.toDp

class ForgetPasswordFragment : BaseFragment<ForgetPasswordViewModel>() {

    override val viewModel: ForgetPasswordViewModel by stateViewModel()
    private var binding: FragmentForgetPasswordBinding? = null
    override val baseBinding: Binding? by lazy { ForgetPasswordBinding() }
    private val args: ForgetPasswordFragmentArgs by navArgs()
    private var currentType: Int = 0

    companion object{
        const val INPUT_EMAIL_TYPE = 0
        const val INPUT_CODE_TYPE = 1
        const val INPUT_NEW_PASSWORD_TYPE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        currentType = args.type
        findNavController().currentBackStackEntry?.savedStateHandle?.
        getLiveData<Boolean>("backb")?.observe(
            viewLifecycleOwner) {
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
        return binding!!.root
    }

    override fun setupViews() {
        when(currentType){
            INPUT_EMAIL_TYPE -> {
                viewModel.observeFirstField(binding?.tietEmail?.obs(), INPUT_EMAIL_TYPE)
            }

            INPUT_CODE_TYPE -> {
                binding?.tvText?.text = getString(R.string.input_verification_code)
                binding?.tvEmail?.visibility = View.GONE
                binding?.tilEmail?.visibility = View.GONE
                binding?.mbSend?.visibility = View.GONE
                binding?.tilVerification1?.visibility = View.VISIBLE
                binding?.tilVerification2?.visibility = View.VISIBLE
                binding?.tilVerification3?.visibility = View.VISIBLE
                binding?.tilVerification4?.visibility = View.VISIBLE
                viewModel.observeNumbers(
                    args.email,
                    binding?.tietVerification1?.obs(),
                    binding?.tietVerification2?.obs(),
                    binding?.tietVerification3?.obs(),
                    binding?.tietVerification4?.obs()
                )
            }

            INPUT_NEW_PASSWORD_TYPE -> {
                binding?.tvText?.text = getString(R.string.input_new_password)
                binding?.tvEmail?.visibility = View.VISIBLE
                binding?.tvEmail?.text = getString(R.string.password)
                binding?.tietEmail?.inputType = EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                binding?.tilEmail?.visibility = View.VISIBLE
                binding?.tilVerification1?.visibility = View.GONE
                binding?.tilVerification2?.visibility = View.GONE
                binding?.tilVerification3?.visibility = View.GONE
                binding?.tilVerification4?.visibility = View.GONE
                binding?.mbSend?.visibility = View.VISIBLE
                binding?.mbSend?.text = getString(R.string.save)
                binding?.mbSend?.isEnabled = false
                binding?.tvRepeatPassword?.visibility = View.VISIBLE
                binding?.tilRepeatPassword?.visibility = View.VISIBLE

                val params = binding?.mbSend?.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = binding?.tilRepeatPassword?.id!!
                params.topMargin = 16.toDp(requireContext())
                binding?.mbSend?.requestLayout()

                viewModel.init(args.code, args.email)
                viewModel.observeFirstField(
                    binding?.tietEmail?.obs(),
                    INPUT_NEW_PASSWORD_TYPE, true
                )
                viewModel.observeFirstField(
                    binding?.tietRepeatPassword?.obs(),
                    INPUT_NEW_PASSWORD_TYPE, false
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class ForgetPasswordBinding : Binding() {

        private var returnBack: String by RenderProp(""){
            when {
                it == "backb" -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set("backb", true)
                    findNavController().popBackStack()
                }
                it != "" -> {
                    Snackbar.make(view!!, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        private var isSuccessField: Boolean by RenderProp(false){
            binding?.mbSend?.isEnabled = it
        }

        private var number: Int by RenderProp(0){
            when(it){
                0 -> {
                    setFocus(binding?.tietVerification1)
                }
                1 -> {
                    setFocus(binding?.tietVerification2)
                }
                2 -> {
                    setFocus(binding?.tietVerification3)
                }
                3 -> {
                    setFocus(binding?.tietVerification4)
                }
            }
        }

        private fun setFocus(tiet: TextInputEditText?){
            tiet?.isEnabled = true
            tiet?.requestFocus()
        }

        override fun bind(data: IViewModelState) {
            data as ForgetPasswordState
            isSuccessField = data.isSuccessField
            number = data.number
            returnBack = data.returnBack
        }
    }
}