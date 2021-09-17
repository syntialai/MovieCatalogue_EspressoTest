package com.syntia.moviecatalogue.features.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel

class FavoriteTvShowsViewModel(private val favoriteItemRepository: FavoriteItemRepository) :
    BaseViewModel() {

  private var _tvShows = MutableLiveData<PagingData<TvShowsEntity>>()
  val tvShows: LiveData<PagingData<TvShowsEntity>>
    get() = _tvShows

  fun fetchFavoriteTvShows() {
    launchViewModelScope {
      favoriteItemRepository.getFavoriteTvShows().runPagingFlow {
        _tvShows.value = it
      }
    }
  }
}