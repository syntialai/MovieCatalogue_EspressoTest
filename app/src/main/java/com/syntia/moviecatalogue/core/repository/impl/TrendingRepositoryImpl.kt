package com.syntia.moviecatalogue.core.repository.impl

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.service.TrendingService
import com.syntia.moviecatalogue.core.repository.TrendingRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.utils.datamapper.TrendingMapper
import kotlinx.coroutines.flow.Flow

class TrendingRepositoryImpl(private val trendingService: TrendingService,
    private val repositoryHelper: RepositoryHelper) : TrendingRepository {

  override suspend fun getTrendingItems(): Flow<ResponseWrapper<List<TrendingItemUiModel>>> {
    return repositoryHelper.createFlow(trendingService::getTrendingItems,
        TrendingMapper::toTrendingItemUiModelList)
  }

  override suspend fun getPopularMovies(): Flow<PagingData<MovieUiModel>> {
    return repositoryHelper.createNetworkPagingFlow(trendingService::getPopularMovies,
        TrendingMapper::toMovieUiModelList)
  }

  override suspend fun getPopularTvShows(): Flow<PagingData<TvShowsUiModel>> {
    return repositoryHelper.createNetworkPagingFlow(trendingService::getPopularTvShows,
        TrendingMapper::toTvShowsUiModelList)
  }
}