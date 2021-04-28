package ru.skillbranch.sbdelivery.ui.dish.review

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.dto.DishDto
import ru.skillbranch.sbdelivery.data.dto.ReviewDto
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.mapper.IReviewsMapper
import ru.skillbranch.sbdelivery.data.mapper.ReviewsMapper
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.data.repository.IReviewsRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class ReviewDialogViewModel(
    handle: SavedStateHandle,
    private val reviewRep: IReviewsRepository,
    private val reviewsMapper: IReviewsMapper
): BaseViewModel<ReviewState>(handle, ReviewState()){

    val cancelDialog = MutableLiveData<Boolean>()
    val starCount = MutableLiveData<Int>()

    fun sendReview(dishId: String){

    }

    fun onStarClick(star: Int){
        if (star == starCount.value){
            starCount.value = star - 1
        }else
            starCount.value = star
    }

    fun onSendReviewClick(view: View){

    }

    fun onCloseDialogClick(){
        cancelDialog.value = true
    }

}

class ReviewState(): IViewModelState
