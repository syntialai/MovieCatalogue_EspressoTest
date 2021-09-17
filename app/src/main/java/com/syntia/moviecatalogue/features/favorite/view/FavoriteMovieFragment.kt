package com.syntia.moviecatalogue.features.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.core.router.Router
import com.syntia.moviecatalogue.core.utils.datamapper.DataMapper
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.FragmentFavoriteMovieBinding
import com.syntia.moviecatalogue.features.base.view.BaseFragment
import com.syntia.moviecatalogue.features.favorite.adapter.FavoriteMovieAdapter
import com.syntia.moviecatalogue.features.favorite.viewmodel.FavoriteMovieViewModel

class FavoriteMovieFragment : BaseFragment<FragmentFavoriteMovieBinding, FavoriteMovieViewModel>(
    FavoriteMovieViewModel::class) {

  companion object {
    const val COLUMN_SPAN_COUNT = 2

    fun newInstance() = FavoriteMovieFragment()
  }

  private val movieAdapter by lazy {
    FavoriteMovieAdapter(::goToDetails)
  }

  override val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteMovieBinding
    get() = FragmentFavoriteMovieBinding::inflate

  override fun setupViews() {
    binding.recyclerViewFavoriteMovie.apply {
      layoutManager = StaggeredGridLayoutManager(COLUMN_SPAN_COUNT,
          StaggeredGridLayoutManager.VERTICAL)
      adapter = movieAdapter.withLoadStateHeaderAndFooter(header = loadStateAdapter,
          footer = loadStateAdapter)
      setHasFixedSize(false)
    }
  }

  override fun setupObserver() {
    viewModel.fetchFavoriteMovies()
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
      recyclerViewFavoriteMovie.showOrRemove(isEmpty.not())
      groupMovieEmptyState.showOrRemove(isEmpty)
    }
  }

  private fun goToDetails(id: Int) {
    Router.goToDetails(requireContext(), id, DataMapper.MOVIE)
  }
}