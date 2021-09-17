package com.syntia.moviecatalogue.core.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.syntia.moviecatalogue.core.local.dao.FavoriteMoviesDAO
import com.syntia.moviecatalogue.core.local.dao.FavoriteTvShowsDAO
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@InternalCoroutinesApi
@Database(entities = [MovieEntity::class, TvShowsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

  abstract fun favoriteMoviesDAO(): FavoriteMoviesDAO

  abstract fun favoriteTvShowsDAO(): FavoriteTvShowsDAO

  companion object {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    @JvmStatic
    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: run {
        synchronized(AppDatabase::class.java) {
          INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
              DatabaseConstants.DB_NAME).build()
        }
        INSTANCE as AppDatabase
      }
    }
  }
}