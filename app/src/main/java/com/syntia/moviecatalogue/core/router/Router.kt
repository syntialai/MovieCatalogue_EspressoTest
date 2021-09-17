package com.syntia.moviecatalogue.core.router

import android.content.Context
import android.content.Intent
import com.syntia.moviecatalogue.features.detail.view.DetailActivity
import com.syntia.moviecatalogue.features.favorite.view.FavoriteItemActivity
import com.syntia.moviecatalogue.features.search.view.SearchActivity

object Router {

  const val PARAM_ID = "PARAM_ID"
  const val PARAM_TYPE = "PARAM_TYPE"

  fun goToDetails(context: Context, id: Int, type: String) {
    val intent = Intent(context, DetailActivity::class.java).apply {
      putExtra(PARAM_ID, id)
      putExtra(PARAM_TYPE, type)
    }
    context.startActivity(intent)
  }

  fun goToFavorites(context: Context) {
    val intent = Intent(context, FavoriteItemActivity::class.java)
    context.startActivity(intent)
  }

  fun goToSearch(context: Context) {
    val intent = Intent(context, SearchActivity::class.java)
    context.startActivity(intent)
  }
}