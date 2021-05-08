package ru.skillbranch.sbdelivery.ui.base

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import ru.skillbranch.sbdelivery.ui.root.RootActivity
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment


abstract class BaseDialogFragment<T : BaseViewModel<out IViewModelState>> : DialogFragment() {

    lateinit var rootView: View
    open val baseBinding: Binding? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    abstract val viewModel: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //restore state
        viewModel.restoreState()
        baseBinding?.restoreUi(savedInstanceState)

        //owner it is view
        viewModel.observeState(viewLifecycleOwner) { baseBinding?.bind(it) }
        //bind default values if viewmodel not loaded data
        if (baseBinding?.isInflated == false) baseBinding?.onFinishInflate()

//        viewModel.observeNotifications(viewLifecycleOwner) { root.renderNotification(it) }
//        viewModel.observeNavigation(viewLifecycleOwner) { root.viewModel.navigate(it) }
        viewModel.observeLoading(viewLifecycleOwner) { renderLoading(it) }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState()
        baseBinding?.saveUi(outState)
        super.onSaveInstanceState(outState)
    }

    //open for overwrite in fregment if need
    open fun renderLoading(loadingState: Loading) {
        //root.renderLoading(loadingState)
    }

    fun TextInputEditText?.obs(): Observable<String>? =
        this?.textChanges()?.skipInitialValue()?.map { it.toString() }

    fun hideKeyboard() {
        val manager: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager?
        manager
            ?.hideSoftInputFromWindow(
                requireView().windowToken, 0
            )
    }
}