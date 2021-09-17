package com.syntia.moviecatalogue.data

import androidx.paging.PagingSource
import com.syntia.moviecatalogue.core.network.data.ListPagingSource
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.movie.Movie
import com.syntia.moviecatalogue.core.network.service.TrendingService
import com.syntia.moviecatalogue.helper.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ListPagingSourceTest : BaseTest() {

  private lateinit var listPagingSource: ListPagingSource<Movie, Boolean>

  @Mock
  private lateinit var trendingService: TrendingService

  private lateinit var mapper: (ListItemResponse<Movie>) -> List<Boolean>

  private val pageCaptor = argumentCaptor<Int>()

  override fun setUp() {
    super.setUp()
    mapper = { listItemResponse ->
      listItemResponse.results.map {
        true
      }
    }
    listPagingSource = ListPagingSource(trendingService::getPopularMovies, mapper)
  }

  @Test
  fun `Given when load list paging source then verify service has been called`() {
    val listItemResponse = ListItemResponse(results = listOf(Movie(
        adult = false,
        id = 123,
        title = "Title",
        voteAverage = 0.0
    )))
    val params = mock<PagingSource.LoadParams<Int>>()

    runBlocking {
      trendingService.stub {
        onBlocking { getPopularMovies(any()) } doReturn listItemResponse
      }

      listPagingSource.load(params)

      delay(1000)
      verify(trendingService).getPopularMovies(pageCaptor.capture())
    }
  }

  @Test
  fun `Given when getResults list paging source then verify service has been called and check results`() {
    val listItemResponse = ListItemResponse(results = listOf(Movie(
        adult = false,
        id = 123,
        title = "Title",
        voteAverage = 0.0
    )))

    runBlocking {
      trendingService.stub {
        onBlocking { getPopularMovies(any()) } doReturn listItemResponse
      }

      val actual = listPagingSource.getResult(1)

      delay(1000)
      verify(trendingService).getPopularMovies(pageCaptor.capture())
      Assert.assertEquals(1, pageCaptor.firstValue)
      Assert.assertEquals(listOf(true), actual)
    }
  }
}