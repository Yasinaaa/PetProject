package ru.skillbranch.sbdelivery.ui.root

import android.os.Bundle
import android.view.View.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat

import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ActivityRootBinding
import ru.skillbranch.sbdelivery.databinding.NavHeaderMainBinding
import ru.skillbranch.sbdelivery.ui.base.BaseActivity
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Notify
import ru.skillbranch.sbdelivery.ui.sign.SignFragment.Companion.SIGN_UP
import ru.skillbranch.sbdelivery.utils.toDp

class RootActivity : BaseActivity<RootViewModel>(){

    public override val viewModel: RootViewModel by stateViewModel()
    private lateinit var appBarConfiguration: AppBarConfiguration
    override lateinit var binding: ActivityRootBinding

    private lateinit var navHeaderMainBinding: NavHeaderMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main, R.id.nav_menu, R.id.nav_favorite,
                R.id.nav_basket, R.id.nav_profile, R.id.nav_orders,
                R.id.nav_notifications
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, bundle ->
            if (destination.id == R.id.nav_search){
                binding.appBarMain.toolbar.visibility = GONE
            }else{
                binding.appBarMain.toolbar.visibility = VISIBLE
            }

            if (destination.id == R.id.nav_sign){
                if (bundle != null) {
                    if (bundle.containsKey("type")) {
                        if (bundle.getInt("type") == SIGN_UP){
                            binding.appBarMain.toolbar.title = getString(R.string.sign_up)
                        }
                    }
                }else{
                    binding.appBarMain.toolbar.title = getString(R.string.sign_in)
                }
                binding.appBarMain.toolbar.navigationIcon =
                    ContextCompat.getDrawable(baseContext, R.drawable.ic_back)
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            }else if(destination.id == R.id.nav_forget_password){
//                binding.appBarMain.toolbar.navigationIcon =
//                    ContextCompat.getDrawable(baseContext, R.drawable.ic_back)
            }
        }

        binding.drawerLayout.closeDrawers()

        val aboutApp = navView.findViewById<TextView>(R.id.tv_about_app)
        aboutApp.setOnClickListener {
            drawerLayout.closeDrawers()
            navController.navigate(R.id.nav_about_app)
        }

        val headerView = binding.navView.getHeaderView(0)
        navHeaderMainBinding = NavHeaderMainBinding.bind(headerView)
        if (!viewModel.currentState.isAuth) {
            navHeaderMainBinding.ibLogout.visibility = GONE
            navHeaderMainBinding.tvEmail.visibility = GONE
            navHeaderMainBinding.tvUsername.setPadding(
                navHeaderMainBinding.tvUsername.paddingLeft,
                navHeaderMainBinding.tvUsername.paddingTop,
                navHeaderMainBinding.tvUsername.paddingRight,
                15.toDp(baseContext)
            )
            navHeaderMainBinding.tvUsername.text = getString(R.string.enter_to_the_profile)
            navHeaderMainBinding.tvUsername.setOnClickListener {
                navController.navigate(R.id.nav_sign)
            }
        }else{
            //TODO
            navHeaderMainBinding.ibLogout.visibility = VISIBLE
            navHeaderMainBinding.tvEmail.visibility = VISIBLE
            navHeaderMainBinding.tvUsername.setPadding(
                navHeaderMainBinding.tvUsername.paddingLeft,
                navHeaderMainBinding.tvUsername.paddingTop,
                navHeaderMainBinding.tvUsername.paddingRight,
                0
            )
        }

        binding.appBarMain.content.swl.setOnRefreshListener {
            val id = navController.currentDestination?.id
            navController.popBackStack(id!!,true)
            navController.navigate(id)
            binding.appBarMain.content.swl.isRefreshing = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun subscribeOnState(state: IViewModelState) {

    }

    override fun renderNotification(notify: Notify) {
        val snackbar = Snackbar.make(
            binding.appBarMain.container, notify.message,
            Snackbar.LENGTH_LONG)
        snackbar.behavior

        when (notify) {
            is Notify.ActionMessage -> {
                val (_, label, handler) = notify


                with(snackbar) {
                    setActionTextColor(ContextCompat.getColor(context, R.color.grey_box))
                    setAction(label) { handler.invoke() }
                }
            }

            is Notify.ErrorMessage -> {
                val (_, label, handler) = notify

                with(snackbar) {
                    setBackgroundTint(ContextCompat.getColor(context,
                        R.color.design_default_color_error))
                    setTextColor(ContextCompat.getColor(context, android.R.color.white))
                    setActionTextColor(ContextCompat.getColor(context, android.R.color.white))
                    handler ?: return@with
                    setAction(label) { handler.invoke() }
                }
            }
            else -> {

            }
        }
        snackbar.show()
    }

}