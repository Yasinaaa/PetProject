package ru.skillbranch.sbdelivery.ui.dish

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.data.dto.ReviewDto
import ru.skillbranch.sbdelivery.databinding.FragmentDishBinding
import ru.skillbranch.sbdelivery.ui.base.BaseFragment
import ru.skillbranch.sbdelivery.ui.base.Binding
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.ui.dish.review.ReviewDialogFragment
import ru.skillbranch.sbdelivery.utils.RenderProp

class DishFragment : BaseFragment<DishViewModel>() {

    override val viewModel: DishViewModel by stateViewModel()
    override val baseBinding: Binding? by lazy { DishBinding() }
    private var binding: FragmentDishBinding? = null
    private val args: DishFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDishBinding.inflate(inflater, container, false)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addReview.observe(viewLifecycleOwner, {
            if (it)
                showCreateReviewDialog()
            else
                openLogInPage()
        })
        viewModel.getDish(args.dishId)

        findNavController().currentBackStackEntry?.savedStateHandle?.
        getLiveData<Boolean>("update")?.observe(
            viewLifecycleOwner) {
            if (it)
                viewModel.getDish(args.dishId)
        }
    }

    override fun setupViews() {
        binding?.tvStrikePrice?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding?.tvEmptyReviewsTitle?.visibility = VISIBLE

//        viewModel.addToCart2.observe(viewLifecycleOwner, {
//            openBasketPage(it)
//        })
    }

    private fun showCreateReviewDialog(){
        val action = DishFragmentDirections.addReviewPage(args.dishId)
        viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
    }

    private fun openLogInPage(){
        viewModel.navigate(NavigationCommand.To(DishFragmentDirections.signPage().actionId))
    }

    private fun openBasketPage(dishCount: Int){
        val action = DishFragmentDirections.basketPage(args.dishId, dishCount)
        viewModel.navigate(NavigationCommand.To(action.actionId, action.arguments))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    inner class DishBinding : Binding() {

        private var comments: MutableList<ReviewDto>
                by RenderProp(mutableListOf()) {
                    if(it.isNotEmpty()) {
                        binding?.rvReviews?.visibility = VISIBLE
                        binding?.tvEmptyReviewsTitle?.visibility = GONE
                        binding?.rvReviews?.adapter = ReviewsAdapter(it)
                    }
                }

        override fun bind(data: IViewModelState) {
            data as DishState
            comments = data.comments
        }
    }
}