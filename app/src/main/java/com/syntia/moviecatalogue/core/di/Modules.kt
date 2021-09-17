package com.syntia.moviecatalogue.core.di

import com.syntia.moviecatalogue.core.repository.DetailRepository
import com.syntia.moviecatalogue.core.repository.FavoriteItemRepository
import com.syntia.moviecatalogue.core.repository.SearchRepository
import com.syntia.moviecatalogue.core.repository.TrendingRepository
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelper
import com.syntia.moviecatalogue.core.repository.helper.RepositoryHelperImpl
import com.syntia.moviecatalogue.core.repository.impl.DetailRepositoryImpl
import com.syntia.moviecatalogue.core.repository.impl.FavoriteItemRepositoryImpl
import com.syntia.moviecatalogue.core.repository.impl.SearchRepositoryImpl
import com.syntia.moviecatalogue.core.repository.impl.TrendingRepositoryImpl
import com.syntia.moviecatalogue.features.detail.viewmodel.DetailViewModel
import com.syntia.moviecatalogue.features.favorite.viewmodel.FavoriteMovieViewModel
import com.syntia.moviecatalogue.features.favorite.viewmodel.FavoriteTvShowsViewModel
import com.syntia.moviecatalogue.features.home.viewmodel.HomeMovieViewModel
import com.syntia.moviecatalogue.features.home.viewmodel.HomeTvShowsViewModel
import com.syntia.moviecatalogue.features.main.viewmodel.MainViewModel
import com.syntia.moviecatalogue.features.search.viewmodel.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { MainViewModel(get()) }
  viewModel { HomeMovieViewModel(get()) }
  viewModel { HomeTvShowsViewModel(get()) }
  viewModel { DetailViewModel(get(), get()) }
  viewModel { FavoriteMovieViewModel(get()) }
  viewModel { FavoriteTvShowsViewModel(get()) }
  viewModel { SearchViewModel(get()) }
}

val repositoryModule = module {
  single { TrendingRepositoryImpl(get(), get()) } bind TrendingRepository::class
  single { DetailRepositoryImpl(get(), get()) } bind DetailRepository::class
  single {
    FavoriteItemRepositoryImpl(get(), get(), get(), get())
  } bind FavoriteItemRepository::class
  single { SearchRepositoryImpl(get(), get()) } bind SearchRepository::class
}

val helperModule = module {
  fun provideRepositoryHelper(ioDispatcher: CoroutineDispatcher): RepositoryHelperImpl {
    return RepositoryHelperImpl(ioDispatcher)
  }

  single { provideRepositoryHelper(get()) } bind RepositoryHelper::class
}

val dispatchersModule = module {
  fun provideIoDispatchers(): CoroutineDispatcher = Dispatchers.IO

  single { provideIoDispatchers() }
}