package ru.skillbranch.sbdelivery.ui.root

import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.ui.base.BaseViewModel
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.NavigationCommand
import ru.skillbranch.sbdelivery.data.repository.ProfileRepository

class RootViewModel(
    handle: SavedStateHandle,
    repository: ProfileRepository
): BaseViewModel<RootState>(handle, RootState()) {

    private val privateRoutes = listOf(R.id.nav_orders, )

    init {
        subscribeOnDataSource(repository.isAuth()) { isAuth, state ->
            state.copy(isAuth = isAuth)
        }
    }

    override fun navigate(command: NavigationCommand) {
        when(command){
            is NavigationCommand.To ->{
                if(privateRoutes.contains(command.destination) && !currentState.isAuth){
                    //set requested destination as arg
                    super.navigate(NavigationCommand.StartLogin(command.destination))
                }else{
                    super.navigate(command)
                }
            }
            else -> super.navigate(command)
        }
    }
}

data class RootState(
    val isAuth: Boolean = false
) : IViewModelState