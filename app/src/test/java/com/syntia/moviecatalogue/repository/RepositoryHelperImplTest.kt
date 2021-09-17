package com.syntia.moviecatalogue.repository

import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.response.error.ErrorResponse
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelperImpl
import com.syntia.moviecatalogue.helper.BaseTest
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class RepositoryHelperImplTest : BaseTest() {

  companion object {
    private const val BOOL_DATA = true
    private const val STRING_DATA = "true"
    private const val ERROR_CODE = 404
    private const val ERROR_BODY = "{\"status_message\": \"Not found\"}"
    private const val ERROR_MESSAGE = "Not found"
  }

  private lateinit var repositoryHelper: RepositoryHelper

  private val dispatcher = TestCoroutineDispatcher()

  override fun setUp() {
    super.setUp()
    repositoryHelper = RepositoryHelperImpl(dispatcher)
  }

  override fun tearDown() {
    super.tearDown()
    dispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `Given when createFlow, apiFetch success then emit the result as Success Wrapper`() {
    dispatcher.runBlockingTest {
      val apiFetch = suspend {
        BOOL_DATA
      }

      val responseFlow = repositoryHelper.createFlow(apiFetch, ::testMapper)

      responseFlow.collectLatest {
        Assert.assertEquals(ResponseWrapper.Success(STRING_DATA), it)
      }
    }
  }

  @Test
  fun `Given when createFlow, apiFetch fail and got IOException then emit the result as Network Error Wrapper`() {
    dispatcher.runBlockingTest {
      val apiFetch = suspend {
        throw IOException()
      }

      val responseFlow = repositoryHelper.createFlow(apiFetch, ::testMapper)

      responseFlow.collectLatest {
        Assert.assertEquals(ResponseWrapper.NetworkError, it)
      }
    }
  }

  @Test
  fun `Given when createFlow, apiFetch fail and got HttpException then emit the result as Error with code and error body Wrapper`() {
    dispatcher.runBlockingTest {
      val apiFetch = suspend {
        throw HttpException(Response.error<Any>(ERROR_CODE,
            ERROR_BODY.toResponseBody("application/json".toMediaTypeOrNull())))
      }

      val responseFlow = repositoryHelper.createFlow(apiFetch, ::testMapper)

      responseFlow.collectLatest {
        Assert.assertEquals(ResponseWrapper.Error(ERROR_CODE, ErrorResponse(ERROR_MESSAGE)), it)
      }
    }
  }

  @Test
  fun `Given when createFlow, apiFetch fail and got other Exception then emit the result as empty Error Wrapper`() {
    dispatcher.runBlockingTest {
      val apiFetch = suspend {
        throw Exception()
      }

      val responseFlow = repositoryHelper.createFlow(apiFetch, ::testMapper)

      responseFlow.collectLatest {
        Assert.assertEquals(ResponseWrapper.Error(), it)
      }
    }
  }

  @Test
  fun `Given when createLocalPagingFlow then return flow with paging data`() {
//    dispatcher.runBlockingTest {
//      val mockPagingSource = mock<PagingSource<Int, String>>()
//      val localFetch = {
//        mockPagingSource
//      }
//      val expectedResult = Pager(
//          config = PagingConfig(pageSize = BasePagingSource.PAGE_SIZE, enablePlaceholders = false),
//          pagingSourceFactory = localFetch).flow
//
//      val localPagingFlow = repositoryHelper.createLocalPagingFlow(localFetch)
//
//      localPagingFlow.collect { actual ->
//        actual.
//      }
//    }
  }

  @Test
  fun `Given when createTrendingPagingFlow then return flow with paging data`() {
//    dispatcher.runBlockingTest {
//      val mockTrendingPagingSource = mock<ListPagingSource<String, Boolean>>()
//      val expectedResult = Pager(
//          config = PagingConfig(pageSize = BasePagingSource.PAGE_SIZE, enablePlaceholders = false),
//          pagingSourceFactory = { mockTrendingPagingSource })
//      val pagingApiFetch: (suspend (Int) -> ListItemResponse<String>) = {
//        ListItemResponse(listOf("String"))
//      }
//      val pagingMapper: (ListItemResponse<String>) -> List<Boolean> = {
//        listOf(true)
//      }
//
//      val pagingFlow = repositoryHelper.createNetworkPagingFlow(pagingApiFetch, pagingMapper)
//
//      pagingFlow.collect {
//        Assert.assertEquals(expectedResult, it)
//      }
//    }
  }

  private fun testMapper(value: Boolean) = value.toString()
}