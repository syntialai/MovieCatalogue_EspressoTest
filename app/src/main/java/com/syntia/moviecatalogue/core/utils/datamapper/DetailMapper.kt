package com.syntia.moviecatalogue.core.utils.datamapper

import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.model.detail.Cast
import com.syntia.moviecatalogue.core.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.network.response.data.detail.Detail
import com.syntia.moviecatalogue.core.network.response.data.detail.MediaCredits
import java.util.Locale
import kotlin.math.floor

object DetailMapper {

  fun toCastList(response: MediaCredits): MutableList<Cast> = response.cast.toMutableList()

  fun toDetailUiModel(response: Detail) = DetailUiModel(
      id = response.id,
      image = response.posterPath.orEmpty(),
      title = DataMapper.getTitle(response.title, response.name),
      releaseOrFirstAirDate = DataMapper.getFormattedDate(response.releaseDate, response.firstAirDate),
      language = response.originalLanguage.toUpperCase(Locale.ROOT),
      runtime = response.runtime?.let { getRuntime(it) },
      episodeCount = response.numberOfEpisodes ?: 0,
      isAdult = response.adult ?: false,
      genres = response.genres,
      overview = response.overview,
      rating = getRating(response.voteAverage)
  )

  fun toMovieEntity(uiModel: DetailUiModel) = MovieEntity(
      id = uiModel.id,
      image = uiModel.image,
      title = uiModel.title,
      releasedYear = getReleasedYear(uiModel.releaseOrFirstAirDate),
      voteAverage = uiModel.rating.first,
      adult = uiModel.isAdult,
      insertedAt = System.currentTimeMillis()
  )

  fun toTvShowsEntity(uiModel: DetailUiModel) = TvShowsEntity(
      id = uiModel.id,
      image = uiModel.image,
      title = uiModel.title,
      releasedYear = getReleasedYear(uiModel.releaseOrFirstAirDate),
      voteAverage = uiModel.rating.first,
      insertedAt = System.currentTimeMillis()
  )

  private fun getHour(minutes: Int) = floor((minutes / 60).toDouble()).toInt()

  private fun getRating(rating: Double) = Pair(rating.toString(), rating.toFloat().div(2f))

  private fun getRuntime(runtime: Int) = "${getHour(runtime)} h ${runtime % 60} min"

  private fun getReleasedYear(releaseOrFirstAirDate: String) = if (releaseOrFirstAirDate.isNotBlank()) {
    releaseOrFirstAirDate.substringAfterLast(", ")
  } else {
    ""
  }
}