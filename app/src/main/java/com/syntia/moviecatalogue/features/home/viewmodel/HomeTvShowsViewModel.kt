package com.syntia.moviecatalogue.features.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.tvshow.TvShowsUiModel
import com.syntia.moviecatalogue.core.repository.TrendingRepository
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel

class HomeTvShowsViewModel(private val trendingRepository: TrendingRepository) : BaseViewModel() {

  private var _tvShows = MutableLiveData<PagingData<TvShowsUiModel>>()
  val tvShows: LiveData<PagingData<TvShowsUiModel>>
    get() = _tvShows

  fun fetchTvShows() {
    launchViewModelScope {
      trendingRepository.getPopularTvShows().runPagingFlow {
        _tvShows.value = it
      }
    }
  }
}