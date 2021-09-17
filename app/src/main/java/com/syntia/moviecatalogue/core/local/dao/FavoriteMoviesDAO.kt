package com.syntia.moviecatalogue.core.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMoviesDAO {

  @Query("SELECT * FROM movies ORDER BY insertedAt DESC")
  fun getAllFavoriteMovies(): PagingSource<Int, MovieEntity>

  @Query("SELECT EXISTS(SELECT * FROM movies WHERE id = :id)")
  fun getIsMovieExists(id: Int): Flow<Boolean>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addMovie(movie: MovieEntity)

  @Query("DELETE FROM movies WHERE id = :id")
  suspend fun deleteMovieById(id: Int)
}