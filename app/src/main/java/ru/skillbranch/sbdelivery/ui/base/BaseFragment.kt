package ru.skillbranch.sbdelivery.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.ui.root.RootActivity
import java.util.*


abstract class BaseFragment<T : BaseViewModel<out IViewModelState>> : Fragment() {

    lateinit var root: RootActivity
    lateinit var rootView: View
    open val baseBinding: Binding? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract val viewModel: T

    open val prepareToolbar: (BaseActivity.ToolbarBuilder.() -> Unit)? = null

    val toolbar
        get() = root.binding.appBarMain.toolbar

    //set listeners, tuning views
    abstract fun setupViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = activity as RootActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //restore state
        viewModel.restoreState()
        baseBinding?.restoreUi(savedInstanceState)

        //owner it is view
        viewModel.observeState(viewLifecycleOwner) { baseBinding?.bind(it) }
        //bind default values if viewmodel not loaded data
        if (baseBinding?.isInflated == false) baseBinding?.onFinishInflate()

        viewModel.observeNotifications(viewLifecycleOwner) { root.renderNotification(it) }
        viewModel.observeNavigation(viewLifecycleOwner) { root.viewModel.navigate(it) }
        viewModel.observeLoading(viewLifecycleOwner) { renderLoading(it) }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        //prepare toolbar
        root.toolbarBuilder
            .invalidate()
            .prepare(prepareToolbar)
            .build(root.binding)

        setupViews()

        baseBinding?.rebind()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        baseBinding?.saveUi(outState)
        super.onSaveInstanceState(outState)
    }

//    override fun onPrepareOptionsMenu(menu: Menu) {
//        if (root.toolbarBuilder.items.isNotEmpty()) {
//            for ((index, menuHolder) in root.toolbarBuilder.items.withIndex()) {
//                val item = menu.add(0, menuHolder.menuId, index, menuHolder.title)
//                item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
//                    .setIcon(menuHolder.icon)
//                    .setOnMenuItemClickListener {
//                        menuHolder.clickListener?.invoke(it)?.let { true } ?: false
//                    }
//
//                if (menuHolder.actionViewLayout != null) item.setActionView(menuHolder.actionViewLayout)
//            }
//        } else menu.clear()
//        super.onPrepareOptionsMenu(menu)
//    }

    //open for overwrite in fregment if need
    open fun renderLoading(loadingState: Loading) {
        root.renderLoading(loadingState)
    }

    fun TextInputEditText?.obs(): Observable<String>? =
        this?.textChanges()?.skipInitialValue()?.map { it.toString() }
}