package com.syntia.moviecatalogue.detail

import android.content.Intent
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.base.BaseInstrumentationTest
import com.syntia.moviecatalogue.core.local.db.AppDatabase
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.core.router.Router
import com.syntia.moviecatalogue.features.detail.adapter.DetailCastAdapter
import com.syntia.moviecatalogue.features.detail.view.DetailActivity
import com.syntia.moviecatalogue.helper.withImageDrawable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class DetailActivityTest : BaseInstrumentationTest() {

  private val intent = Intent(ApplicationProvider.getApplicationContext(),
      DetailActivity::class.java).putExtra(Router.PARAM_ID, ID).putExtra(Router.PARAM_TYPE,
          MEDIA_TYPE_MOVIE)

  private lateinit var activityScenario: ActivityScenario<DetailActivity>

  private lateinit var appDatabase: AppDatabase

  override fun setUp() {
    super.setUp()
    appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java).allowMainThreadQueries().build()
  }

  override fun tearDown() {
    super.tearDown()
    activityScenario.close()
  }

  @Test
  fun showDetails_SuccessFetch_ComponentsDisplayed() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
          "/$MEDIA_TYPE_MOVIE/$ID" -> getSuccessResponse("getDetails_success_response.json")
          "/$MEDIA_TYPE_MOVIE/$ID/credits" -> getSuccessResponse("getDetailCasts_success_response.json")
          else -> getErrorResponse()
        }
      }
    }

    activityScenario = ActivityScenario.launch(intent)

    onView(withId(R.id.spin_kit_detail_load)).isGone()
    onView(withId(R.id.image_view_detail_error_state)).isGone()
    onView(withId(R.id.image_view_detail)).isVisible()
    onView(withId(R.id.layout_detail_main_info)).isVisible()
    onView(withId(R.id.group_detail_overview)).isVisible()
    onView(withId(R.id.group_detail_casts)).isVisible()

    onView(withId(android.R.id.content)).perform(swipeUp())
    onView(withId(R.id.recycler_view_detail_casts)).perform(
        RecyclerViewActions.actionOnItemAtPosition<DetailCastAdapter.DetailCastViewHolder>(1,
            click()))
  }

  @Test
  fun showDetails_FailedFetch_ComponentsNotDisplayed() {
    mockWebServer.enqueue(getErrorResponse())
    mockWebServer.enqueue(getErrorResponse())

    activityScenario = ActivityScenario.launch(intent)

    onView(withId(R.id.spin_kit_detail_load)).isGone()
    onView(withId(R.id.image_view_detail_error_state)).isVisible()
    onView(withId(R.id.image_view_detail)).isGone()
    onView(withId(R.id.layout_detail_main_info)).isGone()
    onView(withId(R.id.group_detail_overview)).isGone()
    onView(withId(R.id.group_detail_casts)).isGone()
  }

  @Test
  fun performClickUpdateFavorite_ShouldAddData() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
          "/$MEDIA_TYPE_MOVIE/$ID" -> getSuccessResponse("getDetails_success_response.json")
          "/$MEDIA_TYPE_MOVIE/$ID/credits" -> getSuccessResponse("getDetailCasts_success_response.json")
          else -> getErrorResponse()
        }
      }
    }

    activityScenario = ActivityScenario.launch(intent)

    with(onView(withId(R.id.button_update_favorite))) {
      check(matches(withImageDrawable(R.drawable.ic_favorite_border)))
      perform(click())
    }

    onView(withId(R.id.button_update_favorite)).check(
        matches(withImageDrawable(R.drawable.ic_favorite_red)))
  }

  @Test
  fun performClickUpdateFavorite_ShouldDeleteData() {
    loadMockDatabase()
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
          "/$MEDIA_TYPE_MOVIE/$ID" -> getSuccessResponse("getDetails_success_response.json")
          "/$MEDIA_TYPE_MOVIE/$ID/credits" -> getSuccessResponse("getDetailCasts_success_response.json")
          else -> getErrorResponse()
        }
      }
    }

    activityScenario = ActivityScenario.launch(intent)

    with(onView(withId(R.id.button_update_favorite))) {
      check(matches(withImageDrawable(R.drawable.ic_favorite_red)))
      perform(click())
    }

    onView(withId(R.id.button_update_favorite)).check(
        matches(withImageDrawable(R.drawable.ic_favorite_border)))
  }

  private fun loadMockDatabase() {
    runBlocking {
      appDatabase.favoriteMoviesDAO().addMovie(
          MovieEntity(id = ID, title = TITLE, image = IMAGE, releasedYear = YEAR,
              voteAverage = VOTE_AVERAGE_STRING, adult = false))
      appDatabase.favoriteTvShowsDAO().addTvShow(
          TvShowsEntity(id = ID, title = TITLE, image = IMAGE, releasedYear = YEAR,
              voteAverage = VOTE_AVERAGE_STRING))
    }
    loadKoinModules(module(override = true) {
      single { appDatabase }
    })
  }
}