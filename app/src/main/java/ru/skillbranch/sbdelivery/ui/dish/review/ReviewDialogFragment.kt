package ru.skillbranch.sbdelivery.ui.dish.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.DialogReviewBinding


class ReviewDialogFragment: DialogFragment() {

    private var binding: DialogReviewBinding? = null

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReviewBinding.inflate(inflater, container, false)
        val root: View = binding?.root!!

        requireActivity().window.setDimAmount(0f)
        requireActivity().window.setBackgroundDrawableResource(android.R.color.transparent)

        return root
    }

}