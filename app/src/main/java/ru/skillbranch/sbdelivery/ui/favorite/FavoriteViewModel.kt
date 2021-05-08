package ru.skillbranch.sbdelivery.ui.favorite

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class FavoriteViewModel(
    handle: SavedStateHandle,
): BaseViewModel<FavoriteState>(handle, FavoriteState()) {

}

class FavoriteState: IViewModelState