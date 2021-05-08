package ru.skillbranch.sbdelivery.ui.category

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class CategoryViewModel(
    handle: SavedStateHandle,
): BaseViewModel<CategoryState>(handle, CategoryState()) {

}

class CategoryState: IViewModelState