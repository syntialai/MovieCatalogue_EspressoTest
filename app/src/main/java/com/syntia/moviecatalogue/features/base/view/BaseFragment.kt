package com.syntia.moviecatalogue.features.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.core.model.loading.LoadingState
import com.syntia.moviecatalogue.core.utils.IdlingResourceHelper
import com.syntia.moviecatalogue.features.base.adapter.PagingLoadStateAdapter
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel
import java.io.IOException
import kotlin.reflect.KClass
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment<VB : ViewBinding, out VM : BaseViewModel>(viewModelClass: KClass<VM>) :
    Fragment() {

  private var _binding: VB? = null
  protected val binding get() = _binding as VB

  private var lifecycleJob: Job? = null

  abstract val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

  protected val viewModel: VM by viewModel(viewModelClass)

  protected val loadStateAdapter by lazy {
    PagingLoadStateAdapter(::checkPagingErrorState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    _binding = viewBindingInflater.invoke(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupViews()
    setupAdapterLoadStateListener()
    setupObserver()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  abstract fun setupViews()

  abstract fun setupObserver()

  open fun setupAdapterLoadStateListener() {}

  open fun showEmptyState(isEmpty: Boolean) {
    decrementIdle()
  }

  protected fun showErrorState(message: String?, @StringRes defaultMessageId: Int) {
    decrementIdle()
    showErrorSnackbar((message ?: getString(defaultMessageId)))
  }

  private fun showErrorSnackbar(message: String) {
    if (message.isNotBlank()) {
      view?.findViewById<View>(android.R.id.content)?.let { view ->
        getSnackbar(view, message).setBackgroundTint(
            getColor(requireContext(), R.color.red_600)).setTextColor(
            getColor(requireContext(), R.color.white)).show()
      }
    }
  }

  protected fun showSnackbar(message: String) {
    if (message.isNotBlank()) {
      view?.findViewById<View>(android.R.id.content)?.let { view ->
        getSnackbar(view, message).show()
      }
    }
  }

  private fun checkErrorState(loadingState: LoadingState) {
    when (loadingState.status) {
      LoadingState.Status.FAILED -> showErrorState(loadingState.message,
          R.string.unknown_error_message)
      LoadingState.Status.NETWORK_ERROR -> showErrorState(null, R.string.no_connection_message)
      else -> {
      }
    }
  }

  private fun checkPagingErrorState(error: Throwable) {
    when (error) {
      is IOException -> showErrorState(null, R.string.no_connection_message)
      else -> showErrorState(error.message, R.string.unknown_error_message)
    }
  }

  private fun getSnackbar(view: View, message: String) = Snackbar.make(view, message,
      Snackbar.LENGTH_SHORT)

  protected fun decrementIdle() {
    if (IdlingResourceHelper.idlingResource.isIdleNow.not()) {
      IdlingResourceHelper.decrement()
    }
  }

  protected fun incrementIdle() {
    IdlingResourceHelper.increment()
  }

  protected fun launchLifecycleScope(block: suspend () -> Unit) {
    lifecycleJob?.cancel()
    lifecycleJob = lifecycleScope.launch {
      block.invoke()
    }
  }
}