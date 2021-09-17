package com.syntia.moviecatalogue.core.repository

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteItemRepository {

  suspend fun getFavoriteMovies(): Flow<PagingData<MovieEntity>>

  suspend fun getFavoriteTvShows(): Flow<PagingData<TvShowsEntity>>

  suspend fun getIsMovieExist(id: Int): Flow<Boolean>

  suspend fun getIsTvShowExist(id: Int): Flow<Boolean>

  suspend fun addMovie(movie: MovieEntity)

  suspend fun addTvShows(tvShows: TvShowsEntity)

  suspend fun deleteMovieById(id: Int)

  suspend fun deleteTvShowsById(id: Int)
}