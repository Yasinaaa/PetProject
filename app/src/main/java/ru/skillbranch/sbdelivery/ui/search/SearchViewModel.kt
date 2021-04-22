package ru.skillbranch.sbdelivery.ui.search

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.skillbranch.sbdelivery.data.local.entity.CategoryEntity
import ru.skillbranch.sbdelivery.data.repository.ICategoryRepository
import ru.skillbranch.sbdelivery.data.repository.IDishRepository
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem
import java.util.concurrent.TimeUnit

class SearchViewModel(
    handle: SavedStateHandle,
    private val dishRepo: IDishRepository,
    private val categoryRepo: ICategoryRepository,
): BaseViewModel<SearchState>(handle, SearchState()){

    private val searchText = MutableLiveData<String>()

    init {
        subscribeOnDataSource(dishRepo.getCachedSearchHistory()) { history, state ->
            history ?: return@subscribeOnDataSource null
            state.copy(
                searchHistory = history
            )
        }
        hideLoading()
    }

    fun returnSearchText(searchEvent: Observable<String>,
                         owner: LifecycleOwner,
                         onChange: (list: String) -> Unit
    ){
        showLoading()
        searchText.observe(owner, { onChange(it) })
        searchEvent
            .delay(2, TimeUnit.SECONDS)
            .debounce(800L, TimeUnit.MILLISECONDS)
            .filter {
                it.isNotBlank()
            }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchText.value = it
            }
    }

    fun setSearchEvent(searchText: String) {
        dishRepo.findDishesByName(searchText)
            .doOnNext { dishes ->
                if (dishes.isNotEmpty()){
                    updateState { it.copy(dishes = dishes) }
                }
            }
            .flatMap {
                categoryRepo.findCategoriesByName(searchText)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { categs ->
                if (categs.isNotEmpty()){
                    updateState { it.copy(foundCategories = categs) }
                }
                hideLoading()
            }
    }
}

data class SearchState(
    val searchHistory: MutableSet<String> = mutableSetOf(),
    val dishes: MutableList<CardItem> = mutableListOf(),
    val foundCategories: MutableList<CategoryEntity> = mutableListOf()
): IViewModelState
