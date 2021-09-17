package com.syntia.moviecatalogue.features.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.core.router.Router
import com.syntia.moviecatalogue.core.utils.datamapper.DataMapper
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.FragmentFavoriteTvBinding
import com.syntia.moviecatalogue.features.base.view.BaseFragment
import com.syntia.moviecatalogue.features.favorite.adapter.FavoriteTvShowsAdapter
import com.syntia.moviecatalogue.features.favorite.viewmodel.FavoriteTvShowsViewModel

class FavoriteTvShowsFragment : BaseFragment<FragmentFavoriteTvBinding, FavoriteTvShowsViewModel>(
    FavoriteTvShowsViewModel::class) {

  companion object {
    fun newInstance() = FavoriteTvShowsFragment()
  }

  private val tvShowsAdapter by lazy {
    FavoriteTvShowsAdapter(::goToDetails)
  }

  override val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFavoriteTvBinding
    get() = FragmentFavoriteTvBinding::inflate

  override fun setupViews() {
    binding.recyclerViewFavoriteTv.apply {
      layoutManager = StaggeredGridLayoutManager(FavoriteMovieFragment.COLUMN_SPAN_COUNT,
          StaggeredGridLayoutManager.VERTICAL)
      adapter = tvShowsAdapter.withLoadStateHeaderAndFooter(header = loadStateAdapter,
          footer = loadStateAdapter)
      setHasFixedSize(false)
    }
  }

  override fun setupObserver() {
    viewModel.fetchFavoriteTvShows()
    incrementIdle()

    viewModel.tvShows.observe(viewLifecycleOwner, {
      launchLifecycleScope {
        tvShowsAdapter.submitData(it)
      }
    })
  }

  override fun setupAdapterLoadStateListener() {
    tvShowsAdapter.addLoadStateListener { loadState ->
      when (loadState.refresh) {
        is LoadState.NotLoading -> {
          showEmptyState(tvShowsAdapter.isEmpty())
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
      recyclerViewFavoriteTv.showOrRemove(isEmpty.not())
      groupTvEmptyState.showOrRemove(isEmpty)
    }
  }

  private fun goToDetails(id: Int) {
    Router.goToDetails(requireContext(), id, DataMapper.TV)
  }
}