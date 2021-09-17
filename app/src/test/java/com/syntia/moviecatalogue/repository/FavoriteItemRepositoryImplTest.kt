package com.syntia.moviecatalogue.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.syntia.moviecatalogue.core.local.dao.FavoriteMoviesDAO
import com.syntia.moviecatalogue.core.local.dao.FavoriteTvShowsDAO
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.repository.impl.FavoriteItemRepositoryImpl
import com.syntia.moviecatalogue.helper.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class FavoriteItemRepositoryImplTest : BaseTest() {

  companion object {
    private const val ID = 12345
  }

  private val testDispatcher = TestCoroutineDispatcher()

  private lateinit var favoriteItemRepository: FavoriteItemRepository

  @Mock
  private lateinit var favoriteMoviesDAO: FavoriteMoviesDAO

  @Mock
  private lateinit var favoriteTvShowsDAO: FavoriteTvShowsDAO

  @Mock
  private lateinit var repositoryHelper: RepositoryHelper

  private val movieLocalFetchCaptor = argumentCaptor<() -> PagingSource<Int, MovieEntity>>()

  private val tvShowsLocalFetchCaptor = argumentCaptor<() -> PagingSource<Int, TvShowsEntity>>()

  private val idCaptor = argumentCaptor<Int>()

  private val movieCaptor = argumentCaptor<MovieEntity>()

  private val tvShowsCaptor = argumentCaptor<TvShowsEntity>()

  override fun setUp() {
    super.setUp()
    favoriteItemRepository = FavoriteItemRepositoryImpl(repositoryHelper, favoriteMoviesDAO,
        favoriteTvShowsDAO, testDispatcher)
  }

  override fun tearDown() {
    super.tearDown()
    testDispatcher.cleanupTestCoroutines()
  }
  
  @Test
  fun `Given when getFavoriteMovies then return flow of paging data response`() {
    val expected = mock<PagingData<MovieEntity>>()

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createLocalPagingFlow<MovieEntity>(any())
        } doReturn getFlow(expected)
      }

      val flow = favoriteItemRepository.getFavoriteMovies()

      flow.collect { actual ->
        Mockito.verify(repositoryHelper).createLocalPagingFlow(movieLocalFetchCaptor.capture())
        Assert.assertEquals(movieLocalFetchCaptor.firstValue,
            favoriteMoviesDAO::getAllFavoriteMovies)

        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when getFavoriteTvShows then return flow of paging data response`() {
    val expected = mock<PagingData<TvShowsEntity>>()

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createLocalPagingFlow<TvShowsEntity>(any())
        } doReturn getFlow(expected)
      }

      val flow = favoriteItemRepository.getFavoriteTvShows()

      flow.collect { actual ->
        Mockito.verify(repositoryHelper).createLocalPagingFlow(tvShowsLocalFetchCaptor.capture())

        Assert.assertEquals(tvShowsLocalFetchCaptor.firstValue,
            favoriteTvShowsDAO::getAllFavoriteTvShows)

        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when getIsMovieExist then return flow of boolean result`() {
    val expected = true

    testDispatcher.runBlockingTest {
      whenever(favoriteMoviesDAO.getIsMovieExists(any())) doReturn getFlow(expected)

      val flow = favoriteItemRepository.getIsMovieExist(ID)

      flow.collect { actual ->
        Mockito.verify(favoriteMoviesDAO).getIsMovieExists(idCaptor.capture())
        Assert.assertEquals(ID, idCaptor.firstValue)

        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(favoriteMoviesDAO)
      }
    }
  }

  @Test
  fun `Given when getIsTvShowExist then return flow of boolean result`() {
    val expected = false

    testDispatcher.runBlockingTest {
      whenever(favoriteTvShowsDAO.getIsTvShowExists(any())) doReturn getFlow(expected)

      val flow = favoriteItemRepository.getIsTvShowExist(ID)

      flow.collect { actual ->
        Mockito.verify(favoriteTvShowsDAO).getIsTvShowExists(idCaptor.capture())
        Assert.assertEquals(ID, idCaptor.firstValue)

        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(favoriteTvShowsDAO)
      }
    }
  }

  @Test
  fun `Given when addMovie then verify DAO is called`() {
    val expectedMovie = mock<MovieEntity>()

    testDispatcher.runBlockingTest {
      whenever(favoriteMoviesDAO.addMovie(any())).thenReturn(Unit)

      favoriteItemRepository.addMovie(expectedMovie)

      Mockito.verify(favoriteMoviesDAO).addMovie(movieCaptor.capture())
      Assert.assertEquals(expectedMovie, movieCaptor.firstValue)

      verifyNoMoreInteractions(favoriteMoviesDAO)
    }
  }

  @Test
  fun `Given when addTvShows then verify DAO is called`() {
    val expectedTvShow = mock<TvShowsEntity>()

    testDispatcher.runBlockingTest {
      whenever(favoriteTvShowsDAO.addTvShow(any())).thenReturn(Unit)

      favoriteItemRepository.addTvShows(expectedTvShow)

      Mockito.verify(favoriteTvShowsDAO).addTvShow(tvShowsCaptor.capture())
      Assert.assertEquals(expectedTvShow, tvShowsCaptor.firstValue)

      verifyNoMoreInteractions(favoriteTvShowsDAO)
    }
  }

  @Test
  fun `Given when deleteMovieById then verify DAO is called`() {
    testDispatcher.runBlockingTest {
      whenever(favoriteMoviesDAO.deleteMovieById(any())).thenReturn(Unit)

      favoriteItemRepository.deleteMovieById(ID)

      Mockito.verify(favoriteMoviesDAO).deleteMovieById(idCaptor.capture())
      Assert.assertEquals(ID, idCaptor.firstValue)

      verifyNoMoreInteractions(favoriteMoviesDAO)
    }
  }

  @Test
  fun `Given when deleteTvShowById then verify DAO is called`() {
    testDispatcher.runBlockingTest {
      whenever(favoriteTvShowsDAO.deleteTvShowById(any())).thenReturn(Unit)

      favoriteItemRepository.deleteTvShowsById(ID)

      Mockito.verify(favoriteTvShowsDAO).deleteTvShowById(idCaptor.capture())
      Assert.assertEquals(ID, idCaptor.firstValue)

      verifyNoMoreInteractions(favoriteTvShowsDAO)
    }
  }
}