package com.syntia.moviecatalogue.core.repository.helper

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.google.gson.Gson
import com.syntia.moviecatalogue.core.network.data.BasePagingSource
import com.syntia.moviecatalogue.core.network.data.ListPagingSource
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.error.ErrorResponse
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RepositoryHelperImpl(private val ioDispatcher: CoroutineDispatcher) : RepositoryHelper {

  override suspend fun <T, U> createFlow(
      apiFetch: suspend () -> T, mapper: (T) -> U): Flow<ResponseWrapper<U>> {
    return flow {
      try {
        val response = apiFetch.invoke()
        val responseWrapper = ResponseWrapper.Success(mapper.invoke(response))
        emit(responseWrapper)
      } catch (throwable: Throwable) {
        emit(when (throwable) {
          is IOException -> ResponseWrapper.NetworkError
          is HttpException -> getErrorResponseWrapper(throwable)
          else -> ResponseWrapper.Error()
        })
      }
    }.flowOn(ioDispatcher)
  }

  override suspend fun <T : Any> createLocalPagingFlow(
      localFetch: () -> PagingSource<Int, T>): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(pageSize = BasePagingSource.PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = localFetch
    ).flow.flowOn(ioDispatcher)
  }

  override suspend fun <T : Any, U : Any> createNetworkPagingFlow(
      apiFetch: suspend (Int) -> ListItemResponse<T>,
      mapper: (ListItemResponse<T>) -> List<U>): Flow<PagingData<U>> {
    return Pager(
        config = PagingConfig(pageSize = BasePagingSource.PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { ListPagingSource(apiFetch, mapper) }
    ).flow.flowOn(ioDispatcher)
  }

  private fun getErrorResponseWrapper(exception: HttpException): ResponseWrapper.Error {
    return ResponseWrapper.Error(exception.code(), getErrorResponse(exception))
  }

  private fun getErrorResponse(exception: HttpException) = try {
    Gson().fromJson(exception.response()?.errorBody()?.charStream(), ErrorResponse::class.java)
  } catch (ex: Exception) {
    null
  }
}