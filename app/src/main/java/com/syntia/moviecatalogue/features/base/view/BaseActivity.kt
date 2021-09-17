package com.syntia.moviecatalogue.features.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.core.model.loading.LoadingState
import com.syntia.moviecatalogue.core.utils.IdlingResourceHelper
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel
import java.io.IOException
import kotlin.reflect.KClass
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity<VB : ViewBinding, out VM : BaseViewModel>(viewModelClass: KClass<VM>) :
    AppCompatActivity() {

  private var _binding: VB? = null
  protected val binding get() = _binding as VB

  abstract val viewBindingInflater: (LayoutInflater) -> VB

  protected val viewModel: VM by viewModel(viewModelClass)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    _binding = viewBindingInflater.invoke(layoutInflater)
    setContentView(binding.root)
    setupViews()
    setupObserver()
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  abstract fun setupViews()

  open fun setupObserver() {
    viewModel.loadingState.observe(this, { loadingState ->
      showLoadingState(loadingState == LoadingState.LOADING)
      checkErrorState(loadingState)
    })
  }

  open fun showEmptyState(isEmpty: Boolean) {
    decrementIdle()
  }

  open fun showLoadingState(isLoading: Boolean) {}

  open fun showErrorState(isError: Boolean, message: String?, @StringRes defaultMessageId: Int) {
    decrementIdle()
    showErrorSnackbar((message ?: getString(defaultMessageId)))
  }

  private fun showErrorSnackbar(message: String) {
    if (message.isNotBlank()) {
      findViewById<View>(android.R.id.content)?.let { view ->
        getSnackbar(view, message).setBackgroundTint(getColor(R.color.red_600)).setTextColor(
            getColor(R.color.white)).show()
      }
    }
  }

  protected fun showSnackbar(message: String) {
    if (message.isNotBlank()) {
      findViewById<View>(android.R.id.content)?.let { view ->
        getSnackbar(view, message).show()
      }
    }
  }

  protected fun getDimenSize(@DimenRes dimenId: Int) = resources.getDimensionPixelSize(dimenId)

  private fun checkErrorState(loadingState: LoadingState) {
    when (loadingState.status) {
      LoadingState.Status.FAILED -> showErrorState(true, loadingState.message,
          R.string.unknown_error_message)
      LoadingState.Status.NETWORK_ERROR -> showErrorState(true, null,
          R.string.no_connection_message)
      else -> {
      }
    }
  }

  protected fun checkPagingErrorState(error: Throwable) {
    when (error) {
      is IOException -> showErrorState(true, null, R.string.no_connection_message)
      else -> showErrorState(true, error.message, R.string.unknown_error_message)
    }
  }

  private fun getSnackbar(view: View, message: String) = Snackbar.make(view, message,
      Snackbar.LENGTH_SHORT)

  protected fun incrementIdle() {
    IdlingResourceHelper.increment()
  }

  protected fun decrementIdle() {
    if (IdlingResourceHelper.idlingResource.isIdleNow.not()) {
      IdlingResourceHelper.decrement()
    }
  }
}