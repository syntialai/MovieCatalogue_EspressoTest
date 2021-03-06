package com.syntia.moviecatalogue.features.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.syntia.moviecatalogue.core.model.movie.MovieUiModel
import com.syntia.moviecatalogue.core.utils.ImageUtils
import com.syntia.moviecatalogue.core.utils.remove
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.LayoutMovieAndTvItemBinding
import com.syntia.moviecatalogue.features.base.adapter.BaseDiffCallback
import com.syntia.moviecatalogue.features.base.adapter.BasePagingDataAdapter

class HomeMovieAdapter(private val onClickListener: (Int) -> Unit) :
    BasePagingDataAdapter<MovieUiModel, LayoutMovieAndTvItemBinding>(diffCallback) {

  companion object {
    val diffCallback = object : BaseDiffCallback<MovieUiModel>() {
      override fun contentEquality(oldItem: MovieUiModel, newItem: MovieUiModel): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }

  override val inflater: (LayoutInflater, ViewGroup?, Boolean) -> LayoutMovieAndTvItemBinding
    get() = LayoutMovieAndTvItemBinding::inflate

  override fun getViewHolder(binding: LayoutMovieAndTvItemBinding): BaseViewHolder {
    return HomeMovieViewHolder(binding)
  }

  inner class HomeMovieViewHolder(binding: LayoutMovieAndTvItemBinding) : BaseViewHolder(binding) {

    override fun bind(data: MovieUiModel) {
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