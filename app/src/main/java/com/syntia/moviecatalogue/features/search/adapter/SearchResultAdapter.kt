package com.syntia.moviecatalogue.features.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.syntia.moviecatalogue.core.model.search.SearchResultUiModel
import com.syntia.moviecatalogue.core.utils.ImageUtils
import com.syntia.moviecatalogue.core.utils.remove
import com.syntia.moviecatalogue.core.utils.showOrRemove
import com.syntia.moviecatalogue.databinding.LayoutMovieAndTvItemBinding
import com.syntia.moviecatalogue.features.base.adapter.BaseDiffCallback
import com.syntia.moviecatalogue.features.base.adapter.BasePagingDataAdapter

class SearchResultAdapter(private val onClickListener: (Int, String) -> Unit) :
    BasePagingDataAdapter<SearchResultUiModel, LayoutMovieAndTvItemBinding>(diffCallback) {

  companion object {
    private const val TYPE_PERSON = "person"

    val diffCallback = object : BaseDiffCallback<SearchResultUiModel>() {
      override fun contentEquality(oldItem: SearchResultUiModel, newItem: SearchResultUiModel): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }

  override val inflater: (LayoutInflater, ViewGroup?, Boolean) -> LayoutMovieAndTvItemBinding
    get() = LayoutMovieAndTvItemBinding::inflate

  override fun getViewHolder(binding: LayoutMovieAndTvItemBinding): BaseViewHolder {
    return SearchResultViewHolder(binding)
  }

  inner class SearchResultViewHolder(binding: LayoutMovieAndTvItemBinding) :
      BaseViewHolder(binding) {

    override fun bind(data: SearchResultUiModel) {
      binding.apply {
        textViewMovieAndTvItemTitle.text = data.name
        textViewPeopleKnownFor.showOrRemove(data.knownFor.isNullOrBlank().not())
        textViewPeopleKnownFor.text = data.knownFor

        chipGroupMovieAndTvItem.showOrRemove(data.knownFor.isNullOrBlank())
        chipMovieAndTvItemAdult.showOrRemove(data.adult)
        chipMovieAndTvItemPopularity.text = data.voteAverage

        if (data.releasedYear.isBlank()) {
          chipMovieAndTvItemYearReleased.remove()
        } else {
          chipMovieAndTvItemYearReleased.text = data.releasedYear
        }

        if (data.type != TYPE_PERSON) {
          root.setOnClickListener {
            onClickListener.invoke(data.id, data.type)
          }
          ImageUtils.loadImage(root.context, data.image, imageViewMovieAndTvItem)
        } else {
          root.isClickable = false
          root.isFocusable = false
          ImageUtils.loadImage(root.context, data.image, imageViewMovieAndTvItem,
              ImageUtils.PLACEHOLDER_TYPE_CAST)
        }
      }
    }
  }
}