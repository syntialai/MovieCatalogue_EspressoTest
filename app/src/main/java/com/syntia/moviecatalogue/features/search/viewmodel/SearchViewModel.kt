package com.syntia.moviecatalogue.features.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.repository.SearchRepository
import com.syntia.moviecatalogue.features.base.viewmodel.BaseViewModel

class SearchViewModel(private val searchRepository: SearchRepository) : BaseViewModel() {

  private var _searchResults = MutableLiveData<PagingData<SearchResultUiModel>>()
  val searchResult: LiveData<PagingData<SearchResultUiModel>>
    get() = _searchResults

  fun searchQuery(query: String) {
    launchViewModelScope {
      searchRepository.searchByQuery(query).runPagingFlow {
        _searchResults.value = it
      }
    }
  }
}