package ru.skillbranch.sbdelivery.ui.dish

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.dto.DishDto
import ru.skillbranch.sbdelivery.data.dto.ReviewDto
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.mapper.IDishesMapper
import ru.skillbranch.sbdelivery.data.mapper.IReviewsMapper
import ru.skillbranch.sbdelivery.data.repository.*
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class DishViewModel(
    handle: SavedStateHandle,
    profRep: IProfileRepository,
    private val dishRep: IDishRepository,
    private val basketRep: ICartRepository,
    private val reviewRep: IReviewsRepository,
    private val dishesMapper: IDishesMapper,
    private val reviewsMapper: IReviewsMapper
): BaseViewModel<DishState>(handle, DishState()){

    private var currentDishEntity: DishEntity? = null
    private val minus = MutableLiveData<Boolean>()
    private val plus = MutableLiveData<Boolean>()

    val currentDish = MutableLiveData<DishDto>()
    val dishCount = MutableLiveData<Int>()
    val addReview = MutableLiveData<Boolean>()
    val addToCart = MutableLiveData<Int>()

    init {
        dishCount.value = 1
        subscribeOnDataSource(profRep.isAuth()) { isAuth, state ->
            state.copy(isAuth = isAuth)
        }
    }

    fun getDish(id: String){
        Single.zip(
            dishRep.getDishById(id)
                .map {
                    currentDishEntity = it
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

    fun onMinusClick(){
        dishCount.value = dishCount.value?.minus(1)
    }

    fun onPlusClick(){
        dishCount.value = dishCount.value?.plus(1)
    }

    fun onAddToCartBtnClick(){
        //addToCart.value = dishCount.value
        if (currentDishEntity != null) {
            basketRep.saveLocally(currentDishEntity!!, dishCount.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addToCart.value = dishCount.value
                    hideLoading()
                }, {
                    Log.d("MainViewModel", it.message!!)
                })
        }
    }

    fun onAddReviewClick(){
        addReview.value = currentState.isAuth
    }
}

data class DishState(
    val isAuth: Boolean = false,
    val comments: MutableList<ReviewDto> = mutableListOf()
): IViewModelState
