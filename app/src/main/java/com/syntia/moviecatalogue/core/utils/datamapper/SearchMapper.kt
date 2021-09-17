package com.syntia.moviecatalogue.core.utils.datamapper

import com.syntia.moviecatalogue.core.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse
import com.syntia.moviecatalogue.core.network.response.data.search.KnownFor
import com.syntia.moviecatalogue.core.network.response.data.search.SearchResult

object SearchMapper {

  fun toSearchResultUiModelList(responses: ListItemResponse<SearchResult>) = DataMapper.toUiModels(
      responses, SearchMapper::toSearchResultUiModel)

  private fun toSearchResultUiModel(response: SearchResult) = SearchResultUiModel(
      id = response.id,
      image = response.posterPath ?: response.profilePath.orEmpty(),
      name = response.title ?: response.name.orEmpty(),
      releasedYear = DataMapper.getYear(response.releaseDate, response.firstAirDate),
      type = response.mediaType,
      voteAverage = response.voteAverage.toString(),
      adult = response.adult ?: false,
      knownFor = response.knownFor?.let {
        toKnownForString(it)
      }
  )

  private fun toKnownForString(knownForList: List<KnownFor>) = knownForList.filter {
    listOfNotNull(it.name, it.title).isNotEmpty()
  }.joinToString { knownFor ->
    knownFor.title ?: knownFor.name.orEmpty()
  }
}