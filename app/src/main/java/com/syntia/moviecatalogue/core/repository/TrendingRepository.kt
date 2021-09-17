package com.syntia.moviecatalogue.core.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface TrendingRepository {

  suspend fun getTrendingItems(): Flow<ResponseWrapper<List<TrendingItemUiModel>>>

  suspend fun getPopularMovies(): Flow<PagingData<MovieUiModel>>

  suspend fun getPopularTvShows(): Flow<PagingData<TvShowsUiModel>>
}