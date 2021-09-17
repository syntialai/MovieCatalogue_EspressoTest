package com.syntia.moviecatalogue.features.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.core.router.Router
import com.syntia.moviecatalogue.core.utils.datamapper.DataMapper
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.FragmentHomeTvBinding
import com.syntia.moviecatalogue.features.base.view.BaseFragment
import com.syntia.moviecatalogue.features.home.adapter.HomeTvShowsAdapter
import com.syntia.moviecatalogue.features.home.viewmodel.HomeTvShowsViewModel

class HomeTvShowsFragment :
    BaseFragment<FragmentHomeTvBinding, HomeTvShowsViewModel>(HomeTvShowsViewModel::class) {

  companion object {
    fun newInstance() = HomeTvShowsFragment()
  }

  private val tvShowsAdapter by lazy {
    HomeTvShowsAdapter(::goToDetails)
  }

  override val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeTvBinding
    get() = FragmentHomeTvBinding::inflate

  override fun setupViews() {
    binding.recyclerViewHomeTv.apply {
      layoutManager = StaggeredGridLayoutManager(HomeMovieFragment.COLUMN_SPAN_COUNT,
          StaggeredGridLayoutManager.VERTICAL)
      adapter = tvShowsAdapter.withLoadStateHeaderAndFooter(header = loadStateAdapter,
          footer = loadStateAdapter)
      setHasFixedSize(false)
    }
  }

  override fun setupObserver() {
    viewModel.fetchTvShows()
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
      recyclerViewHomeTv.showOrRemove(isEmpty.not())
      groupTvEmptyState.showOrRemove(isEmpty)
    }
  }

  private fun goToDetails(id: Int) {
    Router.goToDetails(requireContext(), id, DataMapper.TV)
  }
}