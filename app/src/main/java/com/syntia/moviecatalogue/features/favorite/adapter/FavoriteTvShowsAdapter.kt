package com.syntia.moviecatalogue.features.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.utils.ImageUtils
import com.syntia.moviecatalogue.core.utils.remove
import com.syntia.moviecatalogue.databinding.LayoutMovieAndTvItemBinding
import com.syntia.moviecatalogue.features.base.adapter.BaseDiffCallback
import com.syntia.moviecatalogue.features.base.adapter.BasePagingDataAdapter

class FavoriteTvShowsAdapter(private val onClickListener: (Int) -> Unit) :
    BasePagingDataAdapter<TvShowsEntity, LayoutMovieAndTvItemBinding>(diffCallback) {

  companion object {
    val diffCallback = object : BaseDiffCallback<TvShowsEntity>() {
      override fun contentEquality(oldItem: TvShowsEntity, newItem: TvShowsEntity): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }

  override val inflater: (LayoutInflater, ViewGroup?, Boolean) -> LayoutMovieAndTvItemBinding
    get() = LayoutMovieAndTvItemBinding::inflate

  override fun getViewHolder(binding: LayoutMovieAndTvItemBinding): BaseViewHolder {
    return FavoriteTvShowsViewHolder(binding)
  }

  inner class FavoriteTvShowsViewHolder(binding: LayoutMovieAndTvItemBinding) :
      BaseViewHolder(binding) {

    override fun bind(data: TvShowsEntity) {
      binding.apply {
        textViewMovieAndTvItemTitle.text = data.title

        chipMovieAndTvItemYearReleased.text = data.releasedYear
        chipMovieAndTvItemPopularity.text = data.voteAverage

        if (data.releasedYear.isBlank()) {
          chipMovieAndTvItemYearReleased.remove()
        } else {
          chipMovieAndTvItemYearReleased.text = data.releasedYear
        }

        ImageUtils.loadImage(root.context, data.image, imageViewMovieAndTvItem)

        root.setOnClickListener {
          onClickListener.invoke(data.id)
        }
      }
    }
  }
}