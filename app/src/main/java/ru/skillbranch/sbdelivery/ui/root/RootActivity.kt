package ru.skillbranch.sbdelivery.ui.root

import android.os.Bundle
import android.view.View.*
import android.widget.TextView

import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import ru.skillbranch.sbdelivery.R
import ru.skillbranch.sbdelivery.databinding.ActivityRootBinding
import ru.skillbranch.sbdelivery.ui.base.BaseActivity
import ru.skillbranch.sbdelivery.ui.base.IViewModelState
import ru.skillbranch.sbdelivery.ui.base.Notify

class RootActivity : BaseActivity<RootViewModel>(){

    override val viewModel: RootViewModel by stateViewModel()

    private lateinit var appBarConfiguration: AppBarConfiguration
    override lateinit var binding: ActivityRootBinding

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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_search){
                binding.appBarMain.toolbar.visibility = GONE
            }else{
                binding.appBarMain.toolbar.visibility = VISIBLE
            }

            if (destination.id == R.id.nav_sign){
                binding.appBarMain.toolbar.navigationIcon = null
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }else{
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
            }
        }

        val aboutApp = navView.findViewById<TextView>(R.id.tv_about_app)
        aboutApp.setOnClickListener {
            drawerLayout.closeDrawers()
            navController.navigate(R.id.nav_about_app)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun subscribeOnState(state: IViewModelState) {

    }

    override fun renderNotification(notify: Notify) {

    }

}