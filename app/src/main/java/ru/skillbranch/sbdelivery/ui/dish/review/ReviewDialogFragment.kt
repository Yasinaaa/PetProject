package ru.skillbranch.sbdelivery.ui.dish.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.DialogReviewBinding


class ReviewDialogFragment: DialogFragment() {

    private var binding: DialogReviewBinding? = null
    private val viewModel: ReviewDialogViewModel by stateViewModel()

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReviewBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel

        requireActivity().window.setDimAmount(0f)
        requireActivity().window.setBackgroundDrawableResource(android.R.color.transparent)

        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cancelDialog.observe(viewLifecycleOwner, {
            dismiss()
        })
    }
}