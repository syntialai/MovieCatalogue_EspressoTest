package com.syntia.moviecatalogue.core.repository.impl

import androidx.paging.PagingData
import com.syntia.moviecatalogue.core.local.dao.FavoriteMoviesDAO
import com.syntia.moviecatalogue.core.local.dao.FavoriteTvShowsDAO
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavoriteItemRepositoryImpl(private val repositoryHelper: RepositoryHelper,
    private val favoriteMoviesDAO: FavoriteMoviesDAO,
    private val favoriteTvShowsDAO: FavoriteTvShowsDAO,
    private val ioDispatcher: CoroutineDispatcher) : FavoriteItemRepository {

  override suspend fun getFavoriteMovies(): Flow<PagingData<MovieEntity>> {
    return repositoryHelper.createLocalPagingFlow(favoriteMoviesDAO::getAllFavoriteMovies)
  }

  override suspend fun getFavoriteTvShows(): Flow<PagingData<TvShowsEntity>> {
    return repositoryHelper.createLocalPagingFlow(favoriteTvShowsDAO::getAllFavoriteTvShows)
  }

  override suspend fun getIsMovieExist(id: Int): Flow<Boolean> {
    return favoriteMoviesDAO.getIsMovieExists(id).flowOn(ioDispatcher)
  }

  override suspend fun getIsTvShowExist(id: Int): Flow<Boolean> {
    return favoriteTvShowsDAO.getIsTvShowExists(id).flowOn(ioDispatcher)
  }

  override suspend fun addMovie(movie: MovieEntity) {
    favoriteMoviesDAO.addMovie(movie)
  }

  override suspend fun addTvShows(tvShows: TvShowsEntity) {
    favoriteTvShowsDAO.addTvShow(tvShows)
  }

  override suspend fun deleteMovieById(id: Int) {
    favoriteMoviesDAO.deleteMovieById(id)
  }

  override suspend fun deleteTvShowsById(id: Int) {
    favoriteTvShowsDAO.deleteTvShowById(id)
  }
}