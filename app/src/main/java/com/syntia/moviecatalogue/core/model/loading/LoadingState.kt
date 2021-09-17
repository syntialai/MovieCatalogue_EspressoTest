package com.syntia.moviecatalogue.core.model.loading

data class LoadingState constructor(val status: Status, val message: String? = null) {

  companion object {
    val LOADED = LoadingState(Status.SUCCESS)
    val LOADING = LoadingState(Status.RUNNING)

    fun error(message: String? = null) = LoadingState(Status.FAILED, message)

    fun networkError() = LoadingState(Status.NETWORK_ERROR)
  }

  enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    NETWORK_ERROR
  }
}