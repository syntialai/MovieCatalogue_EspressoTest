package com.syntia.moviecatalogue.core.utils

import androidx.test.espresso.idling.CountingIdlingResource

object IdlingResourceHelper {

  private const val RESOURCE = "MovieCatalogueResource"

  val idlingResource: CountingIdlingResource = CountingIdlingResource(RESOURCE)

  fun increment() {
    idlingResource.increment()
  }

  fun decrement() {
    idlingResource.decrement()
  }
}