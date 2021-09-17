package com.syntia.moviecatalogue.features.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.syntia.moviecatalogue.core.model.loading.LoadingState
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

  private val _loadingState = MutableLiveData<LoadingState>()
  val loadingState: LiveData<LoadingState>
    get() = _loadingState

  protected fun launchViewModelScope(block: suspend () -> Unit) {
    viewModelScope.launch {
      block.invoke()
    }
  }

  protected suspend fun <T> Flow<ResponseWrapper<T>>.runFlow(onSuccessFetch: (T) -> Unit) {
    onStart {
      setStartLoading()
    }.collect {
      checkResponse(it, onSuccessFetch)
    }
  }

  protected suspend fun <T : Any> Flow<PagingData<T>>.runPagingFlow(
      onSuccessFetch: (PagingData<T>) -> Unit) {
    cachedIn(viewModelScope).onStart {
      setStartLoading()
    }.collectLatest {
      onSuccessFetch.invoke(it)
    }
  }

  protected fun <T> checkResponse(response: ResponseWrapper<T>, onSuccessFetch: (T) -> Unit,
      onFailFetch: (() -> Unit)? = null) {
    when (response) {
      is ResponseWrapper.NetworkError -> {
        setNetworkError()
        onFailFetch?.invoke()
      }
      is ResponseWrapper.Error -> {
        setFailedLoading(response.error?.message)
        onFailFetch?.invoke()
      }
      is ResponseWrapper.Success -> {
        onSuccessFetch.invoke(response.data)
        setLoaded()
      }
    }
  }

  private fun setLoaded() {
    _loadingState.value = LoadingState.LOADED
  }

  private fun setStartLoading() {
    _loadingState.value = LoadingState.LOADING
  }

  private fun setFailedLoading(message: String? = null) {
    _loadingState.value = LoadingState.error(message)
  }

  private fun setNetworkError() {
    _loadingState.value = LoadingState.networkError()
  }
}