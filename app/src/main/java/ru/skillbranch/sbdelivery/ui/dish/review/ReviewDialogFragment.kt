package ru.skillbranch.sbdelivery.ui.dish.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.DialogReviewBinding


class ReviewDialogFragment: DialogFragment() {

    private var binding: DialogReviewBinding? = null
    private val viewModel: ReviewDialogViewModel by stateViewModel()
    private val args: ReviewDialogFragmentArgs by navArgs()

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReviewBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        viewModel.setDish(args.dishId)

        requireActivity().window.setDimAmount(0f)
        requireActivity().window.setBackgroundDrawableResource(android.R.color.transparent)

        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cancelDialog.observe(viewLifecycleOwner, {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("update", true)
            dismiss()
        })
    }
}