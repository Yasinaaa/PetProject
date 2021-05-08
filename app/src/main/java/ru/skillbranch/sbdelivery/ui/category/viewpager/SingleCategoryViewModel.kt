package ru.skillbranch.sbdelivery.ui.category.viewpager

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState


class SingleCategoryViewModel(
    handle: SavedStateHandle,
): BaseViewModel<SingleCategoryState>(handle, SingleCategoryState()) {

}

class SingleCategoryState: IViewModelState