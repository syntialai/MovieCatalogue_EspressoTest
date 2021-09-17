package com.syntia.moviecatalogue.core.repository

import com.syntia.moviecatalogue.core.model.detail.Cast
import com.syntia.moviecatalogue.core.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.network.response.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface DetailRepository {

  suspend fun getDetails(mediaType: String, id: Int): Flow<ResponseWrapper<DetailUiModel>>

  suspend fun getDetailCasts(mediaType: String, id: Int): Flow<ResponseWrapper<MutableList<Cast>>>
}