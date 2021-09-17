package com.syntia.moviecatalogue.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.core.router.Router
import com.syntia.moviecatalogue.core.utils.datamapper.DataMapper
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.FragmentHomeMovieBinding
import com.syntia.moviecatalogue.features.base.view.BaseFragment
import com.syntia.moviecatalogue.features.home.adapter.HomeMovieAdapter
import com.syntia.moviecatalogue.features.home.viewmodel.HomeMovieViewModel

class HomeMovieFragment :
    BaseFragment<FragmentHomeMovieBinding, HomeMovieViewModel>(HomeMovieViewModel::class) {

  companion object {
    const val COLUMN_SPAN_COUNT = 2

    fun newInstance() = HomeMovieFragment()
  }

  private val movieAdapter by lazy {
    HomeMovieAdapter(::goToDetails)
  }

  override val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeMovieBinding
    get() = FragmentHomeMovieBinding::inflate

  override fun setupViews() {
    binding.recyclerViewHomeMovie.apply {
      layoutManager = StaggeredGridLayoutManager(COLUMN_SPAN_COUNT,
          StaggeredGridLayoutManager.VERTICAL)
      adapter = movieAdapter.withLoadStateHeaderAndFooter(header = loadStateAdapter,
          footer = loadStateAdapter)
      setHasFixedSize(false)
    }
  }

  override fun setupObserver() {
    viewModel.fetchMovies()
    incrementIdle()

    viewModel.movies.observe(viewLifecycleOwner, {
      launchLifecycleScope {
        movieAdapter.submitData(it)
      }
    })
  }

  override fun setupAdapterLoadStateListener() {
    movieAdapter.addLoadStateListener { loadState ->
      when (loadState.refresh) {
        is LoadState.NotLoading -> {
          showEmptyState(movieAdapter.isEmpty())
          decrementIdle()
        }
        is LoadState.Error -> showErrorState((loadState.refresh as LoadState.Error).error.message,
            R.string.no_connection_message)
        else -> {
        }
      }
    }
  }

  override fun showEmptyState(isEmpty: Boolean) {
    super.showEmptyState(isEmpty)
    binding.apply {
      recyclerViewHomeMovie.showOrRemove(isEmpty.not())
      groupMovieEmptyState.showOrRemove(isEmpty)
    }
  }

  private fun goToDetails(id: Int) {
    Router.goToDetails(requireContext(), id, DataMapper.MOVIE)
  }
}