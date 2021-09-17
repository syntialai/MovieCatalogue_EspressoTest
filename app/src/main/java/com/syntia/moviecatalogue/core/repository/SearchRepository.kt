package com.syntia.moviecatalogue.core.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.search.SearchResultUiModel
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

  suspend fun searchByQuery(query: String): Flow<PagingData<SearchResultUiModel>>
}