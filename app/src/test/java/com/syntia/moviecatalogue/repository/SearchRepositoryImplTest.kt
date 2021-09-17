package com.syntia.moviecatalogue.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.search.SearchResult
import com.syntia.moviecatalogue.core.network.service.SearchService
import com.syntia.moviecatalogue.core.repository.SearchRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.repository.impl.SearchRepositoryImpl
import com.syntia.moviecatalogue.core.utils.datamapper.SearchMapper
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

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SearchRepositoryImplTest : BaseTest() {

  private val testDispatcher = TestCoroutineDispatcher()

  private lateinit var searchRepository: SearchRepository

  @Mock
  private lateinit var searchService: SearchService

  @Mock
  private lateinit var repositoryHelper: RepositoryHelper

  private val searchApiFetchCaptor =
      argumentCaptor<suspend (Int) -> ListItemResponse<SearchResult>>()

  private val searchMapperApiFetchCaptor =
      argumentCaptor<(ListItemResponse<SearchResult>) -> List<SearchResultUiModel>>()

  override fun setUp() {
    super.setUp()
    searchRepository = SearchRepositoryImpl(searchService, repositoryHelper)
  }

  override fun tearDown() {
    super.tearDown()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `Given when search by query then return flow of paging data response`() {
    val query = "QUERY"
    val expected = mock<PagingData<SearchResultUiModel>>()

    testDispatcher.runBlockingTest {
      repositoryHelper.stub {
        onBlocking {
          createNetworkPagingFlow<SearchResult, SearchResultUiModel>(any(), any())
        } doReturn getFlow(expected)
      }

      val flow = searchRepository.searchByQuery(query)

      flow.collect { actual ->
        Mockito.verify(repositoryHelper).createNetworkPagingFlow(searchApiFetchCaptor.capture(),
            searchMapperApiFetchCaptor.capture())

        Assert.assertEquals(searchMapperApiFetchCaptor.firstValue,
            SearchMapper::toSearchResultUiModelList)

        Assert.assertEquals(expected, actual)

        verifyNoMoreInteractions(repositoryHelper)
      }
    }
  }
}