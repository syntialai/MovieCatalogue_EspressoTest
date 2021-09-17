package com.syntia.moviecatalogue.repository

import com.syntia.moviecatalogue.core.model.detail.Cast
import com.syntia.moviecatalogue.core.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.model.detail.Genre
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.response.data.detail.Detail
import com.syntia.moviecatalogue.core.network.response.data.detail.MediaCredits
import com.syntia.moviecatalogue.core.network.service.DetailService
import com.syntia.moviecatalogue.core.repository.DetailRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.repository.impl.DetailRepositoryImpl
import com.syntia.moviecatalogue.core.utils.datamapper.DetailMapper
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
import org.mockito.kotlin.stub
import org.mockito.kotlin.verifyNoMoreInteractions

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class DetailRepositoryImplTest : BaseTest() {

  companion object {
    private const val ID = 123
    private const val FIRST_AIR_DATE =  "2021-01-01"
    private const val RELEASE_DATE = "2021-01-01"
    private const val MEDIA_TYPE_MOVIE = "movie"
    private const val MEDIA_TYPE_TV = "tv"
    private const val NAME = "Finding Nemo"
    private const val TITLE = "Finding Nemo"
    private const val POSTER_PATH = "/abc.jpg"
    private const val VOTE_AVERAGE = 8.9
    private const val LANGUAGE = "en"

    private const val CAST_ID = 1234
    private const val CAST_NAME = "Towel"
    private const val CAST_CHARACTER = "OODFKS"

    private val GENRES = listOf(Genre(1, "Action"))
  }

  private val testDispatcher = TestCoroutineDispatcher()

  private lateinit var detailRepository: DetailRepository

  @Mock
  private lateinit var detailService: DetailService

  @Mock
  private lateinit var repositoryHelper: RepositoryHelper

  private val detailsApiFetchCaptor = argumentCaptor<suspend () -> Detail>()

  private val detailsMapperApiFetchCaptor = argumentCaptor<(Detail) -> DetailUiModel>()

  private val detailCastsApiFetchCaptor = argumentCaptor<suspend () -> MediaCredits>()

  private val detailCastsMapperApiFetchCaptor = argumentCaptor<(MediaCredits) -> MutableList<Cast>>()

  override fun setUp() {
    super.setUp()
    detailRepository = DetailRepositoryImpl(detailService, repositoryHelper)
  }

  override fun tearDown() {
    super.tearDown()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `Given when get movie details then return flow of wrapped response`() {
    val expectedResponse = Detail(
        id = ID,
        posterPath = POSTER_PATH,
        title = TITLE,
        releaseDate = RELEASE_DATE,
        overview = null,
        voteAverage = VOTE_AVERAGE,
        genres = GENRES,
        originalLanguage = LANGUAGE
    )
    val expectedUiModel = DetailMapper.toDetailUiModel(expectedResponse)

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createFlow<Detail, DetailUiModel>(any(), any())
        } doReturn getFlow(ResponseWrapper.Success(expectedUiModel))
      }

      val flow = detailRepository.getDetails(MEDIA_TYPE_MOVIE, ID)

      flow.collect {
        Mockito.verify(repositoryHelper).createFlow(detailsApiFetchCaptor.capture(),
            detailsMapperApiFetchCaptor.capture())

        assertEquals(DetailMapper::toDetailUiModel, detailsMapperApiFetchCaptor.firstValue)

        assertEquals(ResponseWrapper.Success(expectedUiModel), it)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when get tv details then return flow of wrapped response`() {
    val expectedResponse = Detail(
        id = ID,
        posterPath = POSTER_PATH,
        name = NAME,
        firstAirDate = FIRST_AIR_DATE,
        overview = null,
        voteAverage = VOTE_AVERAGE,
        genres = GENRES,
        originalLanguage = LANGUAGE
    )
    val expectedUiModel = DetailMapper.toDetailUiModel(expectedResponse)

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createFlow<Detail, DetailUiModel>(any(), any())
        } doReturn getFlow(ResponseWrapper.Success(expectedUiModel))
      }

      val flow = detailRepository.getDetails(MEDIA_TYPE_TV, ID)

      flow.collect {
        Mockito.verify(repositoryHelper).createFlow(detailsApiFetchCaptor.capture(),
            detailsMapperApiFetchCaptor.capture())

        assertEquals(DetailMapper::toDetailUiModel, detailsMapperApiFetchCaptor.firstValue)

        assertEquals(ResponseWrapper.Success(expectedUiModel), it)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when get movie detail cast then return flow of wrapped response`() {
    val expectedResponse = MediaCredits(0, listOf(Cast(
        id = CAST_ID,
        character = CAST_CHARACTER,
        name = CAST_NAME
    )))
    val expectedUiModel = DetailMapper.toCastList(expectedResponse)

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createFlow<MediaCredits, MutableList<Cast>>(any(), any())
        } doReturn getFlow(ResponseWrapper.Success(expectedUiModel))
      }

      val flow = detailRepository.getDetailCasts(MEDIA_TYPE_MOVIE, ID)

      flow.collect {
        Mockito.verify(repositoryHelper).createFlow(detailCastsApiFetchCaptor.capture(),
            detailCastsMapperApiFetchCaptor.capture())

        assertEquals(DetailMapper::toCastList, detailCastsMapperApiFetchCaptor.firstValue)

        assertEquals(ResponseWrapper.Success(expectedUiModel), it)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }

  @Test
  fun `Given when get tv detail cast then return flow of wrapped response`() {
    val expectedResponse = MediaCredits(0, listOf(Cast(
        id = CAST_ID,
        character = CAST_CHARACTER,
        name = CAST_NAME
    )))
    val expectedUiModel = DetailMapper.toCastList(expectedResponse)

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createFlow<MediaCredits, MutableList<Cast>>(any(), any())
        } doReturn getFlow(ResponseWrapper.Success(expectedUiModel))
      }

      val flow = detailRepository.getDetailCasts(MEDIA_TYPE_TV, ID)

      flow.collect {
        Mockito.verify(repositoryHelper).createFlow(detailCastsApiFetchCaptor.capture(),
            detailCastsMapperApiFetchCaptor.capture())

        assertEquals(DetailMapper::toCastList, detailCastsMapperApiFetchCaptor.firstValue)

        assertEquals(ResponseWrapper.Success(expectedUiModel), it)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }
}