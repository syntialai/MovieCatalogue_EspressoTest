package com.syntia.moviecatalogue.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.repository.TrendingRepository
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel

class HomeMovieViewModel(private val trendingRepository: TrendingRepository) : BaseViewModel() {

  private var _movies = MutableLiveData<PagingData<MovieUiModel>>()
  val movies: LiveData<PagingData<MovieUiModel>>
    get() = _movies

  fun fetchMovies() {
    launchViewModelScope {
      trendingRepository.getPopularMovies().runPagingFlow {
        _movies.value = it
      }
    }
  }
}