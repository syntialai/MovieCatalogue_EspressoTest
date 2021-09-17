package com.syntia.moviecatalogue.core.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTvShowsDAO {

  @Query("SELECT * FROM tvShows ORDER BY insertedAt DESC")
  fun getAllFavoriteTvShows(): PagingSource<Int, TvShowsEntity>

  @Query("SELECT EXISTS(SELECT * FROM tvShows WHERE id = :id)")
  fun getIsTvShowExists(id: Int): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addTvShow(tvShows: TvShowsEntity)

  @Query("DELETE FROM tvShows WHERE id = :id")
  suspend fun deleteTvShowById(id: Int)
}