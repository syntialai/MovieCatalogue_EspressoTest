package com.syntia.moviecatalogue.core.repository.helper

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import kotlinx.coroutines.flow.Flow

interface RepositoryHelper {

  suspend fun <T, U> createFlow(apiFetch: suspend () -> T, mapper: (T) -> U): Flow<ResponseWrapper<U>>

  suspend fun <T : Any> createLocalPagingFlow(localFetch: () -> PagingSource<Int, T>): Flow<PagingData<T>>

  suspend fun <T : Any, U : Any> createNetworkPagingFlow(
      apiFetch: suspend (Int) -> ListItemResponse<T>,
      mapper: (ListItemResponse<T>) -> List<U>): Flow<PagingData<U>>
}