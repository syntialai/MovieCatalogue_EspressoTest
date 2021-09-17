package com.syntia.moviecatalogue.favorite

import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.base.BaseInstrumentationTest
import com.syntia.moviecatalogue.core.local.db.AppDatabase
import com.syntia.moviecatalogue.core.local.entity.movie.MovieEntity
import com.syntia.moviecatalogue.core.local.entity.tvShows.TvShowsEntity
import com.syntia.moviecatalogue.features.favorite.view.FavoriteItemActivity
import com.syntia.moviecatalogue.features.home.adapter.HomeMovieAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class FavoriteItemActivityTest : BaseInstrumentationTest() {

  private lateinit var activityScenario: ActivityScenario<FavoriteItemActivity>

  private lateinit var appDatabase: AppDatabase

  override fun setUp() {
    super.setUp()
    appDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java).allowMainThreadQueries().build()
  }

  override fun tearDown() {
    super.tearDown()
    activityScenario.close()
    appDatabase.close()
  }

  @Test
  fun showFavoriteMovies_Success_RecyclerViewDisplayed() {
    loadMockDatabase()

    activityScenario = ActivityScenario.launch(FavoriteItemActivity::class.java)

    onView(allOf(withText(R.string.movie_label),
        isDescendantOfA(withId(R.id.tab_layout_favorite_item)))).perform(click()).check(
        matches(isDisplayed()))

    onView(withId(R.id.recycler_view_favorite_movie)).isVisible()
    onView(withId(R.id.group_movie_empty_state)).isGone()
    onView(withId(R.id.recycler_view_favorite_movie)).perform(
        RecyclerViewActions.actionOnItemAtPosition<HomeMovieAdapter.HomeMovieViewHolder>(0,
            click()))

    onView(withId(R.id.button_back_from_detail)).perform(click())

    onView(allOf(withText(R.string.tv_shows_label),
        isDescendantOfA(withId(R.id.tab_layout_favorite_item)))).perform(click()).check(
        matches(isDisplayed()))

    onView(withId(R.id.recycler_view_favorite_tv)).isVisible()
    onView(withId(R.id.group_tv_empty_state)).isGone()

    onView(withId(R.id.recycler_view_favorite_tv)).perform(
        RecyclerViewActions.actionOnItemAtPosition<HomeMovieAdapter.HomeMovieViewHolder>(0,
            click()))
  }

  @Test
  fun showFavoriteMovies_Success_IsEmpty_RecyclerViewDisplayed() {
    loadEmptyDatabase()

    activityScenario = ActivityScenario.launch(FavoriteItemActivity::class.java)

    onView(allOf(withText(R.string.movie_label),
        isDescendantOfA(withId(R.id.tab_layout_favorite_item)))).perform(click()).check(
        matches(isDisplayed()))

    onView(withId(R.id.recycler_view_favorite_movie)).isGone()
    onView(withId(R.id.group_movie_empty_state)).isVisible()
  }

  @Test
  fun showFavoriteTvShows_Success_IsEmpty_RecyclerViewDisplayed() {
    loadEmptyDatabase()

    activityScenario = ActivityScenario.launch(FavoriteItemActivity::class.java)

    onView(allOf(withText(R.string.tv_shows_label),
        isDescendantOfA(withId(R.id.tab_layout_favorite_item)))).perform(click()).check(
        matches(isDisplayed()))

    onView(withId(R.id.recycler_view_favorite_tv)).isGone()
    onView(withId(R.id.group_tv_empty_state)).isVisible()
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

  private fun loadEmptyDatabase() {
    runBlocking {
      appDatabase.clearAllTables()
    }

    loadKoinModules(module(override = true) {
      single { appDatabase }
    })
  }
}