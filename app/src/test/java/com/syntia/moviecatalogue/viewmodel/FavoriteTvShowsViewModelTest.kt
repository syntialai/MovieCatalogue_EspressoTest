package com.syntia.moviecatalogue.viewmodel

import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.features.favorite.adapter.FavoriteTvShowsAdapter
import com.syntia.moviecatalogue.features.favorite.viewmodel.FavoriteTvShowsViewModel
import com.syntia.moviecatalogue.helper.BaseViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FavoriteTvShowsViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: FavoriteTvShowsViewModel

  private val favoriteItemRepository = mock<FavoriteItemRepository>()

  override fun setUp() {
    super.setUp()
    viewModel = FavoriteTvShowsViewModel(favoriteItemRepository)
  }

  @Test
  fun `Given when fetch favorite tv shows then success update live data`() {
    val data = listOf(TvShowsEntity(
        id = ID,
        image = IMAGE,
        releasedYear = YEAR,
        title = NAME,
        voteAverage = VOTE_AVERAGE_STRING
    ))
    val flow = data.getFakePagingData()
    val differ = getAsyncPagingDataDiffer(FavoriteTvShowsAdapter.diffCallback)

    rule.dispatcher.runBlockingTest {
      val job = launch {
        flow.collectLatest { data ->
          differ.submitData(data)
        }
      }

      whenever(favoriteItemRepository.getFavoriteTvShows()) doReturn flow

      viewModel.fetchFavoriteTvShows()
      verify(favoriteItemRepository).getFavoriteTvShows()
      advanceUntilIdle()

      Assert.assertTrue(differ.snapshot().contains(data[0]))
      Assert.assertNotNull(viewModel.tvShows.value)

      verifyNoMoreInteractions(favoriteItemRepository)
      job.cancel()
    }
  }
}