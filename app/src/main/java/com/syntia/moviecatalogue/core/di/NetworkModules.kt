package com.syntia.moviecatalogue.core.di

import com.syntia.moviecatalogue.BuildConfig
import com.syntia.moviecatalogue.core.network.service.DetailService
import com.syntia.moviecatalogue.core.network.service.SearchService
import com.syntia.moviecatalogue.core.network.service.TrendingService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_API_URL = "https://api.themoviedb.org/3/"

private const val API_KEY_QUERY = "api_key"

val serviceModule = module {

  fun provideDetailService(retrofit: Retrofit): DetailService {
    return retrofit.create(DetailService::class.java)
  }

  fun provideSearchService(retrofit: Retrofit): SearchService {
    return retrofit.create(SearchService::class.java)
  }

  fun provideTrendingService(retrofit: Retrofit): TrendingService {
    return retrofit.create(TrendingService::class.java)
  }

  single { provideDetailService(get()) }
  single { provideSearchService(get()) }
  single { provideTrendingService(get()) }
}

val networkModule = module {

  fun provideRetrofit(): Retrofit {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
      setLevel(HttpLoggingInterceptor.Level.BASIC)
    }
    val interceptor = Interceptor { chain ->
      val request = chain.request()

      val newUrl = request.url.newBuilder().addQueryParameter(API_KEY_QUERY, BuildConfig.GITHUB_API_KEY).build()
      val requestBuilder = request.newBuilder().url(newUrl)

      chain.proceed(requestBuilder.build())
    }

    val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(
        interceptor).build()

    return Retrofit.Builder().baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
  }

  single { provideRetrofit() }
}
