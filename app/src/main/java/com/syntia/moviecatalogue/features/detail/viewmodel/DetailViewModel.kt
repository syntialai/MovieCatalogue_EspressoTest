package com.syntia.moviecatalogue.features.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.model.detail.Cast
import com.syntia.moviecatalogue.core.model.detail.DetailUiModel
import com.syntia.moviecatalogue.core.model.loading.LoadingState
import com.syntia.moviecatalogue.core.repository.DetailRepository
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.utils.datamapper.DetailMapper
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class DetailViewModel(private val detailRepository: DetailRepository,
    private val favoriteItemRepository: FavoriteItemRepository) : BaseViewModel() {

  companion object {
    private const val MOVIE = "movie"
  }

  private var _casts = MutableLiveData<MutableList<Cast>>()
  val casts: LiveData<MutableList<Cast>>
    get() = _casts

  private var _details = MutableLiveData<DetailUiModel>()
  val details: LiveData<DetailUiModel>
    get() = _details

  private var _loadCasts = MutableLiveData<LoadingState>()
  val loadCasts: LiveData<LoadingState>
    get() = _loadCasts

  private var _isFavoriteItem = MutableLiveData<Boolean>()
  val isFavoriteItem: LiveData<Boolean>
    get() = _isFavoriteItem

  private var _typeAndId: Pair<String, Int>? = null

  private var movieEntity: MovieEntity? = null

  private var tvShowsEntity: TvShowsEntity? = null

  fun setIdAndType(id: Int, type: String) {
    _typeAndId = Pair(type, id)
  }

  fun fetchDetails() {
    _typeAndId?.let { typeAndId ->
      launchViewModelScope {
        detailRepository.getDetails(typeAndId.first, typeAndId.second).runFlow {
          _details.value = it
          setMovieOrTvEntity()
        }
      }
    }
  }

  fun fetchCasts() {
    _typeAndId?.let { typeAndId ->
      launchViewModelScope {
        detailRepository.getDetailCasts(typeAndId.first, typeAndId.second).onStart {
          _loadCasts.value = LoadingState.LOADING
        }.collectLatest {
          checkResponse(it, ::onSuccessFetchCasts, ::onFailFetchCasts)
        }
      }
    }
  }

  fun getIsFavoriteItem() {
    _typeAndId?.let { typeAndId ->
      launchViewModelScope {
        if (typeAndId.first == MOVIE) {
          favoriteItemRepository.getIsMovieExist(typeAndId.second).collectLatest {
            _isFavoriteItem.value = it
          }
        } else {
          favoriteItemRepository.getIsTvShowExist(typeAndId.second).collectLatest {
            _isFavoriteItem.value = it
          }
        }
      }
    }
  }

  fun updateFavoriteItem() {
    val isAdded = _isFavoriteItem.value ?: false
    _typeAndId?.let { typeAndId ->
      if (typeAndId.first == MOVIE) {
        updateMovieItem(isAdded)
      } else {
        updateTvItem(isAdded)
      }
      _isFavoriteItem.value = isAdded.not()
    }
  }

  private fun setMovieOrTvEntity() {
    _typeAndId?.let { typeAndId ->
      if (typeAndId.first == MOVIE) {
        movieEntity = DetailMapper.toMovieEntity(_details.value!!)
      } else {
        tvShowsEntity = DetailMapper.toTvShowsEntity(_details.value!!)
      }
    }
  }

  private fun updateMovieItem(isAdded: Boolean) {
    movieEntity?.let { movie ->
      launchViewModelScope {
        if (isAdded) {
          favoriteItemRepository.deleteMovieById(movie.id)
        } else {
          favoriteItemRepository.addMovie(movie)
        }
      }
    }
  }

  private fun updateTvItem(isAdded: Boolean) {
    tvShowsEntity?.let { tvShow ->
      launchViewModelScope {
        if (isAdded) {
          favoriteItemRepository.deleteTvShowsById(tvShow.id)
        } else {
          favoriteItemRepository.addTvShows(tvShow)
        }
      }
    }
  }

  private fun onFailFetchCasts() {
    _loadCasts.value = LoadingState.error()
  }

  private fun onSuccessFetchCasts(castsData: MutableList<Cast>) {
    _loadCasts.value = LoadingState.LOADED
    _casts.value = castsData
  }
}