package com.syntia.moviecatalogue

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.syntia.moviecatalogue.core.di.daoModule
import com.syntia.moviecatalogue.core.di.dispatchersModule
import com.syntia.moviecatalogue.core.di.helperModule
import com.syntia.moviecatalogue.core.di.repositoryModule
import com.syntia.moviecatalogue.core.di.serviceModule
import com.syntia.moviecatalogue.core.di.viewModelModule
import com.syntia.moviecatalogue.core.local.db.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AndroidTestApp : Application() {

  companion object {
    const val PORT = 8888
  }

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      androidContext(this@AndroidTestApp)
      modules(listOf(getMockNetworkModule(), getDatabaseModule(), dispatchersModule, helperModule,
          serviceModule, daoModule, repositoryModule, viewModelModule))
    }
  }

  private fun getMockNetworkModule() = module {

    fun provideBaseUrl(): String = "http://127.0.0.1:$PORT/"

    fun provideRetrofit(baseUrl: String): Retrofit {
      val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
      }

      return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
              GsonConverterFactory.create()).client(
              OkHttpClient.Builder().addInterceptor(logging).build()).build()
    }

    single { provideBaseUrl() }
    single { provideRetrofit(get()) }
  }

  private fun getDatabaseModule() = module {
    fun provideDatabase(): AppDatabase {
      return Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
          AppDatabase::class.java).allowMainThreadQueries().build()
    }

    single { provideDatabase() }
  }
}