package com.syntia.moviecatalogue.core.network.service

import com.syntia.moviecatalogue.core.network.api.ApiPath
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.movie.Movie
import com.syntia.moviecatalogue.core.network.response.data.trending.TrendingItem
import com.syntia.moviecatalogue.core.network.response.data.tvshow.TvShows
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingService {

  @GET(ApiPath.TRENDING_ALL_WEEK)
  suspend fun getTrendingItems(): ListItemResponse<TrendingItem>

  @GET(ApiPath.GET_MOVIE_POPULAR)
  suspend fun getPopularMovies(@Query(ApiPath.PAGE) page: Int): ListItemResponse<Movie>

  @GET(ApiPath.GET_TV_TOP_POPULAR)
  suspend fun getPopularTvShows(@Query(ApiPath.PAGE) page: Int): ListItemResponse<TvShows>
}