package com.syntia.moviecatalogue.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.movie.Movie
import com.syntia.moviecatalogue.core.network.response.data.trending.TrendingItem
import com.syntia.moviecatalogue.core.network.response.data.tvshow.TvShows
import com.syntia.moviecatalogue.core.network.service.TrendingService
import com.syntia.moviecatalogue.core.repository.TrendingRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.repository.impl.TrendingRepositoryImpl
import com.syntia.moviecatalogue.core.utils.datamapper.TrendingMapper
import com.syntia.moviecatalogue.helper.BaseTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TrendingRepositoryImplTest : BaseTest() {

  companion object {
    private const val ID = 123
    private const val FIRST_AIR_DATE =  "2021-01-01"
    private const val RELEASE_DATE = "2021-01-01"
    private const val MEDIA_TYPE = "movie"
    private const val NAME = "Finding Nemo"
    private const val TITLE = "Finding Nemo"
    private const val POSTER_PATH = "/abc.jpg"
    private const val VOTE_AVERAGE = 8.9
  }

  private val testDispatcher = TestCoroutineDispatcher()

  private lateinit var trendingRepository: TrendingRepository

  @Mock
  private lateinit var trendingService: TrendingService

  @Mock
  private lateinit var repositoryHelper: RepositoryHelper

  private val trendingItemApiFetchCaptor =
      argumentCaptor<suspend () -> ListItemResponse<TrendingItem>>()

  private val trendingItemMapperApiFetchCaptor =
      argumentCaptor<(ListItemResponse<TrendingItem>) -> List<TrendingItemUiModel>>()

  private val movieApiFetchCaptor =
      argumentCaptor<suspend (Int) -> ListItemResponse<Movie>>()

  private val movieMapperApiFetchCaptor =
      argumentCaptor<(ListItemResponse<Movie>) -> List<MovieUiModel>>()

  private val tvShowsApiFetchCaptor =
      argumentCaptor<suspend (Int) -> ListItemResponse<TvShows>>()

  private val tvShowsMapperApiFetchCaptor =
      argumentCaptor<(ListItemResponse<TvShows>) -> List<TvShowsUiModel>>()

  override fun setUp() {
    super.setUp()
    trendingRepository = TrendingRepositoryImpl(trendingService, repositoryHelper)
  }

  override fun tearDown() {
    super.tearDown()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `Given when get trending items then return flow of wrapped response`() {
    val expectedResponse = generateListItemResponse(listOf(TrendingItem(
        firstAirDate = FIRST_AIR_DATE,
        id = ID,
        posterPath = POSTER_PATH,
        mediaType = MEDIA_TYPE,
        voteAverage = VOTE_AVERAGE,
        name = NAME,
        releaseDate = null,
        title = null
    )))
    val expectedUiModel = TrendingMapper.toTrendingItemUiModelList(expectedResponse)

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createFlow<ListItemResponse<TrendingItem>, List<TrendingItemUiModel>>(any(), any())
        } doReturn getFlow(ResponseWrapper.Success(expectedUiModel))
      }

      val flow = trendingRepository.getTrendingItems()

      flow.collect {
        Mockito.verify(repositoryHelper).createFlow(trendingItemApiFetchCaptor.capture(),
            trendingItemMapperApiFetchCaptor.capture())

        assertEquals(trendingItemApiFetchCaptor.firstValue, trendingService::getTrendingItems)
        assertEquals(trendingItemMapperApiFetchCaptor.firstValue,
            TrendingMapper::toTrendingItemUiModelList)

        assertEquals(ResponseWrapper.Success(expectedUiModel), it)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when get popular movies then return flow of paging data ui model`() {
    val expected = mock<PagingData<MovieUiModel>>()

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createNetworkPagingFlow<Movie, MovieUiModel>(any(), any())
        } doReturn getFlow(expected)
      }

      val flow = trendingRepository.getPopularMovies()

      flow.collect { actual ->
        Mockito.verify(repositoryHelper).createNetworkPagingFlow(movieApiFetchCaptor.capture(),
            movieMapperApiFetchCaptor.capture())

        assertEquals(movieApiFetchCaptor.firstValue, trendingService::getPopularMovies)
        assertEquals(movieMapperApiFetchCaptor.firstValue, TrendingMapper::toMovieUiModelList)

        assertEquals(expected, actual)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when get popular tvShows then return flow of paging data ui model`() {
    val expected = mock<PagingData<TvShowsUiModel>>()

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createNetworkPagingFlow<TvShows, TvShowsUiModel>(any(), any())
        } doReturn getFlow(expected)
      }

      val flow = trendingRepository.getPopularTvShows()

      flow.collect { actual ->
        Mockito.verify(repositoryHelper).createNetworkPagingFlow(tvShowsApiFetchCaptor.capture(),
            tvShowsMapperApiFetchCaptor.capture())

        assertEquals(tvShowsApiFetchCaptor.firstValue, trendingService::getPopularTvShows)
        assertEquals(tvShowsMapperApiFetchCaptor.firstValue, TrendingMapper::toTvShowsUiModelList)

        assertEquals(expected, actual)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  private fun <T> generateListItemResponse(results: List<T>) = ListItemResponse(results = results)
}