package ru.skillbranch.sbdelivery.ui.dish

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import ru.skillbranch.sbdelivery.data.local.entity.DishEntity
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.main.adapters.RecyclerItem

class DishViewModel(
    handle: SavedStateHandle,
    private val repository: IDishRepository,
): BaseViewModel<DishState>(handle, DishState()){

    val currentDish = MutableLiveData<DishEntity>()

    fun getDish(id: String){
        repository.getDishById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null)
                    currentDish.value = it
                hideLoading()
            }, {
                Log.d("MainViewModel", "it.error=" + it.message)
                hideLoading()
            })
    }

}

data class DishState(
    val title: String? = null,
    val isPromotion: Boolean? = null,
    val isFavorite: Boolean = false,
    val description: String? = null,
    val oldPrice: String? = null,
    val newPrice: String? = null,
    val count: Int? = null,
    val comments: MutableList<ReviewsAdapter.CustomerReview> = mutableListOf()
): IViewModelState