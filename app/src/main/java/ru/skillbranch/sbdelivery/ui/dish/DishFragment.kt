package ru.skillbranch.sbdelivery.ui.dish

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.FragmentDishBinding
import ru.skillbranch.sbdelivery.databinding.FragmentMainBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.dish.review.ReviewDialogFragment
import ru.skillbranch.sbdelivery.utils.removeZero

class DishFragment : BaseFragment<DishViewModel>() {

    override val viewModel: DishViewModel by stateViewModel()
    private var binding: FragmentDishBinding? = null
    private val args: DishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDishBinding.inflate(inflater, container, false)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDish(args.dishId)
    }

    override fun setupViews() {
        binding?.tvStrikePrice?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding?.tvStrikePrice?.text =
            String.format(requireContext().getString(R.string.rub), 780.0f.removeZero())
        binding?.tvTitle?.text = "Пицца Маргарита"
        binding?.tvText?.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt ut labore et dolore " +
                "magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
                "laboris nisi ut aliquip ex ea commodo consequat."
        binding?.tvPrice?.text =
            String.format(requireContext().getString(R.string.rub), 680.0f.removeZero())
        binding?.tvEmptyReviewsTitle?.visibility = GONE
        binding?.rvReviews?.visibility = VISIBLE
        binding?.rvReviews?.adapter = ReviewsAdapter()
        //showReviewDialog()
    }

    private fun showReviewDialog(){
        val reviewDialog = ReviewDialogFragment()
        reviewDialog.show(parentFragmentManager, "review")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}