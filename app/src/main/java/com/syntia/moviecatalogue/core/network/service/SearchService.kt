package com.syntia.moviecatalogue.core.network.service

import com.syntia.moviecatalogue.core.network.api.ApiPath
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.search.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

  @GET(ApiPath.SEARCH_MULTI)
  suspend fun searchByQuery(
      @Query(ApiPath.PAGE) page: Int,
      @Query(value = ApiPath.QUERY, encoded = true) query: String
  ): ListItemResponse<SearchResult>
}