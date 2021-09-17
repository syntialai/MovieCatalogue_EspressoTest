package com.syntia.moviecatalogue.core.utils

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.syntia.moviecatalogue.R

object ImageUtils {

  private const val IMAGE_API_URL = "https://image.tmdb.org/t/p/w500"

  private const val PLACEHOLDER_TYPE_POSTER = 0
  const val PLACEHOLDER_TYPE_CAST = 1

  fun loadImage(context: Context, imagePath: String?, imageView: ImageView,
      placeholderType: Int = PLACEHOLDER_TYPE_POSTER) {

    Glide.with(context).load("$IMAGE_API_URL$imagePath").placeholder(
        getPlaceholderResourceId(placeholderType)).into(imageView).onLoadFailed(
        AppCompatResources.getDrawable(context, getPlaceholderResourceId(placeholderType)))
  }

  private fun getPlaceholderResourceId(placeholderType: Int) = when(placeholderType) {
    PLACEHOLDER_TYPE_POSTER -> R.drawable.drawable_placeholder_poster
    PLACEHOLDER_TYPE_CAST -> R.drawable.drawable_placeholder_person
    else -> -1
  }
}