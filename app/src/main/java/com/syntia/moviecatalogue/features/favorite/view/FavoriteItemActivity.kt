package com.syntia.moviecatalogue.features.favorite.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.databinding.ActivityFavoriteItemBinding
import com.syntia.moviecatalogue.features.favorite.adapter.FavoriteItemPagerAdapter

class FavoriteItemActivity : AppCompatActivity() {

  private lateinit var binding: ActivityFavoriteItemBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFavoriteItemBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupToolbar()
    setupTabLayoutAndViewPager()
  }

  private fun setupTabLayoutAndViewPager() {
    binding.apply {
      viewPagerFavoriteItems.adapter = FavoriteItemPagerAdapter(supportFragmentManager, lifecycle)
      TabLayoutMediator(tabLayoutFavoriteItem, viewPagerFavoriteItems) { tab, position ->
        tab.text = when (position) {
          FavoriteItemPagerAdapter.FAVORITE_MOVIE_FRAGMENT_INDEX -> getString(R.string.movie_label)
          FavoriteItemPagerAdapter.FAVORITE_TV_FRAGMENT_INDEX -> getString(R.string.tv_shows_label)
          else -> null
        }
      }.attach()
    }
  }

  private fun setupToolbar() {
    binding.toolbarFavoriteItem.apply {
      setSupportActionBar(this)
      setNavigationOnClickListener {
        onBackPressed()
      }
    }
  }
}