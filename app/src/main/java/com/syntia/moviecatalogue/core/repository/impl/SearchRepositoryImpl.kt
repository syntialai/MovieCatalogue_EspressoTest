package com.syntia.moviecatalogue.core.repository.impl

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.search.SearchResult
import com.syntia.moviecatalogue.core.network.service.SearchService
import com.syntia.moviecatalogue.core.repository.SearchRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.utils.datamapper.SearchMapper
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(private val searchService: SearchService,
    private val repositoryHelper: RepositoryHelper) : SearchRepository {

  override suspend fun searchByQuery(query: String): Flow<PagingData<SearchResultUiModel>> {
    val searchLambda: (suspend (Int) -> ListItemResponse<SearchResult>) = { page: Int ->
      searchService.searchByQuery(page, query)
    }
    return repositoryHelper.createNetworkPagingFlow(searchLambda,
        SearchMapper::toSearchResultUiModelList)
  }
}