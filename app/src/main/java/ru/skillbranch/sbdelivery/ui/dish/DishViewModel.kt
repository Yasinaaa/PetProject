package ru.skillbranch.sbdelivery.ui.dish

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

class DishViewModel(
    handle: SavedStateHandle,
    private val dishRep: IDishRepository,
    private val reviewRep: IReviewsRepository,
    private val dishesMapper: IDishesMapper,
    private val reviewsMapper: IReviewsMapper
): BaseViewModel<DishState>(handle, DishState()){

    val currentDish = MutableLiveData<DishDto>()
    val dishCount = MutableLiveData<Int>()
    val addReview = MutableLiveData<Boolean>()

    private val minus = MutableLiveData<Boolean>()
    private val plus = MutableLiveData<Boolean>()
    private val addToCart = MutableLiveData<Boolean>()

    init {
        dishCount.value = 1
    }

    fun getDish(id: String){
        Single.zip(
            dishRep.getDishById(id)
                .map {
                    if (it != null)
                        dishesMapper.mapEntityToDto(it)
                    else
                        null
                }
                .subscribeOn(Schedulers.io()),
            reviewRep.getReviews(0, 100, id)
                .map {
                    if (it != null)
                        reviewsMapper.mapReviewEntitiesToDto(it)
                    else
                        null
                }
                .subscribeOn(Schedulers.io()),
            { t1, t2 -> t1 to t2 }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val dish = it.first
                val reviews = it.second

                if (dish != null)
                    currentDish.value = dish!!
                if (reviews != null)
                    updateState { it.copy(comments = reviews.toMutableList()) }

                hideLoading()
            },{

            })
    }

    fun onMinusClick(view: View){

    }

    fun onPlusClick(view: View){

    }

    fun onAddToCartBtnClick(view: View){

    }

    fun onAddReviewClick(view: View){
        addReview.value = true
    }
}

data class DishState(
    val comments: MutableList<ReviewDto> = mutableListOf()
): IViewModelState
