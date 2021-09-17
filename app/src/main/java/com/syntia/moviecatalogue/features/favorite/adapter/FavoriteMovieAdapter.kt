package com.syntia.moviecatalogue.features.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.utils.ImageUtils
import com.syntia.moviecatalogue.core.utils.remove
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.LayoutMovieAndTvItemBinding
import com.syntia.moviecatalogue.features.base.adapter.BaseDiffCallback
import com.syntia.moviecatalogue.features.base.adapter.BasePagingDataAdapter

class FavoriteMovieAdapter(private val onClickListener: (Int) -> Unit) :
    BasePagingDataAdapter<MovieEntity, LayoutMovieAndTvItemBinding>(diffCallback) {

  companion object {
    val diffCallback = object : BaseDiffCallback<MovieEntity>() {
      override fun contentEquality(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }

  override val inflater: (LayoutInflater, ViewGroup?, Boolean) -> LayoutMovieAndTvItemBinding
    get() = LayoutMovieAndTvItemBinding::inflate

  override fun getViewHolder(binding: LayoutMovieAndTvItemBinding): BaseViewHolder {
    return FavoriteMovieViewHolder(binding)
  }

  inner class FavoriteMovieViewHolder(binding: LayoutMovieAndTvItemBinding) :
      BaseViewHolder(binding) {

    override fun bind(data: MovieEntity) {
      binding.apply {
        textViewMovieAndTvItemTitle.text = data.title

        chipMovieAndTvItemAdult.showOrRemove(data.adult)
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