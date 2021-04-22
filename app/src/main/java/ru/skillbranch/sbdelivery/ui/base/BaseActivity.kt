package ru.skillbranch.sbdelivery.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ActivityRootBinding
import ru.skillbranch.sbdelivery.ui.base.Loading.*

abstract class BaseActivity<T : BaseViewModel<out IViewModelState>> : AppCompatActivity() {

    protected abstract val viewModel: T
    abstract var binding: ActivityRootBinding
    lateinit var navController: NavController

    val toolbarBuilder = ToolbarBuilder()

    abstract fun subscribeOnState(state: IViewModelState)

    abstract fun renderNotification(notify: Notify)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        viewModel.observeState(this) { subscribeOnState(it) }
        viewModel.observeNotifications(this) { renderNotification(it) }
        viewModel.observeNavigation(this) { subscribeOnNavigation(it) }
        viewModel.observeLoading(this) { renderLoading(it) }
        renderLoading(SHOW_LOADING)
        navController = findNavController(R.id.nav_host_fragment_content_main)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.restoreState()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun subscribeOnNavigation(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.To -> {
                navController.navigate(
                    command.destination,
                    command.args,
                    command.options,
                    command.extras
                )
            }

//            is NavigationCommand.FinishLogin -> {
//                navController.navigate(R.id.finish_login)
//                if (command.privateDestination != null)
//                    navController.navigate(command.privateDestination)
//            }
//
//            is NavigationCommand.StartLogin -> {
//                navController.navigate(
//                    R.id.start_login,
//                    bundleOf("private_destination" to (command.privateDestination ?: -1))
//                )
//            }
        }
    }

    open fun renderLoading(loadingState: Loading) {
        when (loadingState) {
            SHOW_LOADING -> binding.appBarMain.content.pb.isVisible = true
            SHOW_BLOCKING_LOADING -> {
                binding.appBarMain.content.pb.isVisible = true
                //TODO block interact with UI
            }
            HIDE_LOADING -> binding.appBarMain.content.pb.isVisible = false
        }
    }

    class ToolbarBuilder() {
        var subtitle: String? = null
        var logo: String? = null
        var visibility: Boolean = true
        var items: MutableList<MenuItemHolder> = mutableListOf()

        fun setSubTitle(subtitle: String): ToolbarBuilder {
            this.subtitle = subtitle
            return this
        }

        fun setLogo(logo: String): ToolbarBuilder {
            this.logo = logo
            return this
        }

        fun setVisibility(isVisible: Boolean): ToolbarBuilder {
            this.visibility = isVisible
            return this
        }

        fun addMenuItem(item: MenuItemHolder): ToolbarBuilder {
            this.items.add(item)
            return this
        }

        fun invalidate(): ToolbarBuilder {
            this.subtitle = null
            this.logo = null
            this.visibility = true
            this.items.clear()
            return this
        }

        fun prepare(prepareFn: (ToolbarBuilder.() -> Unit)?): ToolbarBuilder {
            prepareFn?.invoke(this)
            return this
        }

        fun build(binding: ActivityRootBinding) {
            //show appbar if hidden due to scroll behavior
            binding.appBarMain.apl.setExpanded(true, true)
        }
    }

    data class MenuItemHolder(
        val title: String,
        val menuId: Int,
        val icon: Int,
        val actionViewLayout: Int? = null,
        val clickListener: ((MenuItem) -> Unit)? = null
    )

}
