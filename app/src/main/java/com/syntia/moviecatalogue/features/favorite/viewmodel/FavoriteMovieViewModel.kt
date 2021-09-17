package com.syntia.moviecatalogue.features.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel

class FavoriteMovieViewModel(private val favoriteItemRepository: FavoriteItemRepository) :
    BaseViewModel() {

  private var _movies = MutableLiveData<PagingData<MovieEntity>>()
  val movies: LiveData<PagingData<MovieEntity>>
    get() = _movies

  fun fetchFavoriteMovies() {
    launchViewModelScope {
      favoriteItemRepository.getFavoriteMovies().runPagingFlow {
        _movies.value = it
      }
    }
  }
}