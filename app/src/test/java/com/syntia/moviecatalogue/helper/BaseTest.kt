package com.syntia.moviecatalogue.helper

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseTest {

  @Before
  open fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @After
  open fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  protected fun <T> getFlow(data: T, isDelay: Boolean = false) = flow {
    if (isDelay) {
      delay(1000)
    }
    emit(data)
  }
}