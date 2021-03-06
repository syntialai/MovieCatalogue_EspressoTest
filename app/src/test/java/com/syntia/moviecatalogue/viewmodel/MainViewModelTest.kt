package com.syntia.moviecatalogue.viewmodel

import com.syntia.moviecatalogue.core.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.repository.TrendingRepository
import com.syntia.moviecatalogue.features.main.viewmodel.MainViewModel
import com.syntia.moviecatalogue.helper.BaseViewModelTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: MainViewModel

  private val trendingRepository = mock<TrendingRepository>()

  override fun setUp() {
    super.setUp()
    viewModel = MainViewModel(trendingRepository)
  }

  @Test
  fun `Given when fetch trending items then update live data`() {
    val uiModel = mutableListOf(TrendingItemUiModel(
        id = ID,
        title = TITLE,
        image = IMAGE,
        releasedYear = YEAR,
        voteAverage = VOTE_AVERAGE_STRING,
        type = MEDIA_TYPE_MOVIE
    ))
    val response = getFlow(ResponseWrapper.Success(uiModel), true).flowOn(dispatcher)

    rule.dispatcher.runBlockingTest {
      whenever(trendingRepository.getTrendingItems()) doReturn response

      viewModel.fetchTrendingItems()

      verify(trendingRepository).getTrendingItems()
      delay(1000)

      viewModel.trendingItems.observeForTesting { actual ->
        assertEquals(uiModel, actual)
      }

      verifyNoMoreInteractions(trendingRepository)
    }
  }
}