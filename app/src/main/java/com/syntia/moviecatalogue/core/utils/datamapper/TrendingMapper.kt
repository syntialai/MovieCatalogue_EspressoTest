package com.syntia.moviecatalogue.core.utils.datamapper

import com.syntia.moviecatalogue.core.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.model.trending.TrendingItemUiModel
import com.syntia.moviecatalogue.core.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.movie.Movie
import com.syntia.moviecatalogue.core.network.response.data.trending.TrendingItem
import com.syntia.moviecatalogue.core.network.response.data.tvshow.TvShows
import java.util.Locale

object TrendingMapper {

  fun toTrendingItemUiModelList(responses: ListItemResponse<TrendingItem>) = DataMapper.toUiModels(
      responses, ::toTrendingItemUiModel)

  fun toMovieUiModelList(responses: ListItemResponse<Movie>) = DataMapper.toUiModels(responses,
      ::toMovieUiModel)

  fun toTvShowsUiModelList(responses: ListItemResponse<TvShows>) = DataMapper.toUiModels(responses,
      ::toTvShowsUiModel)

  private fun toTrendingItemUiModel(response: TrendingItem) = TrendingItemUiModel(
      id = response.id,
      title = DataMapper.getTitle(response.title, response.name),
      image = response.posterPath.orEmpty(),
      releasedYear = DataMapper.getYear(response.releaseDate, response.firstAirDate),
      voteAverage = response.voteAverage.toString(),
      type = response.mediaType.capitalize(Locale.ROOT))

  private fun toMovieUiModel(response: Movie) = MovieUiModel(
      id = response.id,
      image = response.posterPath.orEmpty(),
      title = response.title,
      releasedYear = DataMapper.getYear(response.releaseDate),
      voteAverage = response.voteAverage.toString(),
      adult = response.adult)

  private fun toTvShowsUiModel(response: TvShows) = TvShowsUiModel(
      id = response.id,
      image = response.posterPath.orEmpty(),
      title = response.name,
      releasedYear = DataMapper.getYear(response.firstAirDate),
      voteAverage = response.voteAverage.toString())
}