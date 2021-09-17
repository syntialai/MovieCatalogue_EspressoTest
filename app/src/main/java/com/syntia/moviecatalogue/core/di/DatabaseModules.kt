package com.syntia.moviecatalogue.core.di

import android.app.Application
import com.syntia.moviecatalogue.core.local.dao.FavoriteMoviesDAO
import com.syntia.moviecatalogue.core.local.dao.FavoriteTvShowsDAO
import com.syntia.moviecatalogue.core.local.db.AppDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.dsl.module

@InternalCoroutinesApi
val daoModule = module {

  fun provideFavoriteMoviesDAO(appDatabase: AppDatabase): FavoriteMoviesDAO {
    return appDatabase.favoriteMoviesDAO()
  }

  fun provideFavoriteTvShowsDAO(appDatabase: AppDatabase): FavoriteTvShowsDAO {
    return appDatabase.favoriteTvShowsDAO()
  }

  single { provideFavoriteMoviesDAO(get()) }
  single { provideFavoriteTvShowsDAO(get()) }
}

@InternalCoroutinesApi
val databaseModule = module {

  fun provideAppDatabase(applicationContext: Application): AppDatabase {
    return AppDatabase.getDatabase(applicationContext)
  }

  single { provideAppDatabase(get()) }
}