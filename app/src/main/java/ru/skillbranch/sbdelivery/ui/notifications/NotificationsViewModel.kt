package ru.skillbranch.sbdelivery.ui.notifications

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState

class NotificationsViewModel(
    handle: SavedStateHandle,
): BaseViewModel<NotificationState>(handle, NotificationState()) {

}

class NotificationState: IViewModelState