package com.syntia.moviecatalogue.core.network.service

import com.syntia.moviecatalogue.core.network.api.ApiPath
import com.syntia.moviecatalogue.core.network.response.data.detail.Detail
import com.syntia.moviecatalogue.core.network.response.data.detail.MediaCredits
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {

  @GET(ApiPath.GET_MOVIE_ID)
  suspend fun getMovieDetails(@Path(ApiPath.ID) id: Int): Detail

  @GET(ApiPath.GET_TV_ID)
  suspend fun getTvDetails(@Path(ApiPath.ID) id: Int): Detail

  @GET(ApiPath.GET_MOVIE_ID_CREDITS)
  suspend fun getMovieCredits(@Path(ApiPath.ID) id: Int): MediaCredits

  @GET(ApiPath.GET_TV_ID_CREDITS)
  suspend fun getTvCredits(@Path(ApiPath.ID) id: Int): MediaCredits
}