package com.syntia.moviecatalogue.core.repository.impl

import com.syntia.moviecatalogue.core.model.detail.Cast
import com.syntia.moviecatalogue.core.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.network.api.ApiPath
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import com.syntia.moviecatalogue.core.network.service.DetailService
import com.syntia.moviecatalogue.core.repository.DetailRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.utils.datamapper.DetailMapper
import kotlinx.coroutines.flow.Flow

class DetailRepositoryImpl(private val detailService: DetailService,
    private val repositoryHelper: RepositoryHelper) : DetailRepository {

  override suspend fun getDetails(mediaType: String, id: Int): Flow<ResponseWrapper<DetailUiModel>> {
    return repositoryHelper.createFlow(suspend {
       getDetailMethod(mediaType, id, detailService::getMovieDetails, detailService::getTvDetails)
    }, DetailMapper::toDetailUiModel)
  }

  override suspend fun getDetailCasts(mediaType: String, id: Int): Flow<ResponseWrapper<MutableList<Cast>>> {
    return repositoryHelper.createFlow(suspend {
      getDetailMethod(mediaType, id, detailService::getMovieCredits, detailService::getTvCredits)
    }, DetailMapper::toCastList)
  }

  private suspend fun <T> getDetailMethod(mediaType: String, id: Int,
      movieMethod: suspend (Int) -> T, tvMethod: suspend (Int) -> T) = if (mediaType == ApiPath.MOVIE) {
    movieMethod.invoke(id)
  } else {
    tvMethod.invoke(id)
  }
}