package com.syntia.moviecatalogue.main

import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.base.BaseInstrumentationTest
import com.syntia.moviecatalogue.core.network.api.ApiPath
import com.syntia.moviecatalogue.features.home.adapter.HomeMovieAdapter
import com.syntia.moviecatalogue.features.home.adapter.HomeTvShowsAdapter
import com.syntia.moviecatalogue.features.main.view.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest : BaseInstrumentationTest() {

  private lateinit var activityScenario: ActivityScenario<MainActivity>

  override fun tearDown() {
    super.tearDown()
    activityScenario.close()
  }

  @Test
  fun performClickOnOptionsMenu_Success() {
    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(withId(R.id.menu_show_favorites)).perform(click())
    onView(isRoot()).perform(ViewActions.pressBack())

    onView(withId(R.id.menu_search)).perform(click())
  }

  @Test
  fun showTrendingItems_SuccessFetch_ViewPagerDisplayed() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
          "/${ApiPath.TRENDING_ALL_WEEK}" -> getSuccessResponse(
              "getTrendingItem_success_response.json")
          "/${ApiPath.GET_MOVIE_POPULAR}" -> getSuccessResponse(
              "getPopularMovies_success_response.json")
          "/${ApiPath.GET_TV_TOP_POPULAR}" -> getSuccessResponse(
              "getPopularTvShows_success_response.json")
          else -> getErrorResponse()
        }
      }
    }

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(withId(R.id.view_pager_carousel_trending)).isVisible()
    onView(withId(R.id.text_view_trending_title)).isVisible()
    onView(withId(R.id.spin_kit_carousel_trending_load)).isGone()
  }

  @Test
  fun showTrendingItems_SuccessFetch_EmptyResult_ViewPagerNotDisplayed() {
    mockWebServer.enqueue(getEmptyResponse())
    mockWebServer.enqueue(getEmptyResponse())

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(withId(R.id.view_pager_carousel_trending)).isGone()
    onView(withId(R.id.text_view_trending_title)).isGone()
    onView(withId(R.id.spin_kit_carousel_trending_load)).isGone()
  }

  @Test
  fun showTrendingItems_FailedFetch_ViewPagerNotDisplayed() {
    mockWebServer.enqueue(getErrorResponse())
    mockWebServer.enqueue(getErrorResponse())

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(withId(R.id.view_pager_carousel_trending)).isGone()
    onView(withId(R.id.text_view_trending_title)).isVisible()
    onView(withId(R.id.spin_kit_carousel_trending_load)).isGone()
  }

  @Test
  fun showPopularMovies_SuccessFetch_RecyclerViewDisplayed() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
          "/${ApiPath.TRENDING_ALL_WEEK}" -> getSuccessResponse(
              "getTrendingItem_success_response.json")
          "/${ApiPath.GET_MOVIE_POPULAR}?${ApiPath.PAGE}=1" -> getSuccessResponse(
              "getPopularMovies_success_response.json")
          "/${ApiPath.GET_TV_TOP_POPULAR}?${ApiPath.PAGE}=1" -> getSuccessResponse(
              "getPopularTvShows_success_response.json")
          else -> getErrorResponse()
        }
      }
    }

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(allOf(withText(R.string.movie_label),
        isDescendantOfA(withId(R.id.tab_layout_movie_and_tv_shows)))).perform(click()).check(
        matches(isDisplayed()))

    onView(withId(R.id.recycler_view_home_movie)).isVisible()
    onView(withId(R.id.group_movie_empty_state)).isGone()

    onView(withId(android.R.id.content)).perform(ViewActions.swipeUp())
    onView(withId(R.id.recycler_view_home_movie)).perform(
        RecyclerViewActions.actionOnItemAtPosition<HomeMovieAdapter.HomeMovieViewHolder>(2,
            click()))
  }

  @Test
  fun showPopularMovies_SuccessFetch_Empty_RecyclerViewDisplayed() {
    mockWebServer.enqueue(getEmptyResponse())
    mockWebServer.enqueue(getEmptyResponse())

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(allOf(withText(R.string.movie_label),
        isDescendantOfA(withId(R.id.tab_layout_movie_and_tv_shows))))
        .perform(click())
        .check(matches(isDisplayed()))

    onView(withId(R.id.recycler_view_home_movie)).isGone()
    onView(withId(R.id.group_movie_empty_state)).isVisible()
  }

  @Test
  fun showPopularMovies_FailedFetch_RecyclerViewNotDisplayed() {
    mockWebServer.enqueue(getErrorResponse())
    mockWebServer.enqueue(getErrorResponse())

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(allOf(withText(R.string.movie_label),
        isDescendantOfA(withId(R.id.tab_layout_movie_and_tv_shows))))
        .perform(click())
        .check(matches(isDisplayed()))

    onView(withId(R.id.recycler_view_home_movie)).isGone()
    onView(withId(R.id.group_movie_empty_state)).isVisible()
  }

  @Test
  fun showPopularTvShows_SuccessFetch_RecyclerViewDisplayed() {
    mockWebServer.dispatcher = object : Dispatcher() {
      override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
          "/${ApiPath.TRENDING_ALL_WEEK}" -> getSuccessResponse(
              "getTrendingItem_success_response.json")
          "/${ApiPath.GET_MOVIE_POPULAR}?${ApiPath.PAGE}=1" -> getSuccessResponse(
              "getPopularMovies_success_response.json")
          "/${ApiPath.GET_TV_TOP_POPULAR}?${ApiPath.PAGE}=1" -> getSuccessResponse(
              "getPopularTvShows_success_response.json")
          else -> getErrorResponse()
        }
      }
    }

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(allOf(withText(R.string.tv_shows_label),
        isDescendantOfA(withId(R.id.tab_layout_movie_and_tv_shows))))
        .perform(click())
        .check(matches(isDisplayed()))

    onView(withId(R.id.recycler_view_home_tv)).isVisible()
    onView(withId(R.id.group_tv_empty_state)).isGone()

    onView(withId(android.R.id.content)).perform(ViewActions.swipeUp())
    onView(withId(R.id.recycler_view_home_tv)).perform(
        RecyclerViewActions.actionOnItemAtPosition<HomeTvShowsAdapter.HomeTvShowsViewHolder>(2,
            click()))
  }

  @Test
  fun showPopularTvShows_SuccessFetch_Empty_RecyclerViewDisplayed() {
    mockWebServer.enqueue(getEmptyResponse())
    mockWebServer.enqueue(getEmptyResponse())
    mockWebServer.enqueue(getEmptyResponse())

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(allOf(withText(R.string.tv_shows_label),
        isDescendantOfA(withId(R.id.tab_layout_movie_and_tv_shows))))
        .perform(click())
        .check(matches(isDisplayed()))

    onView(withId(R.id.recycler_view_home_tv)).isGone()
    onView(withId(R.id.group_tv_empty_state)).isVisible()
  }

  @Test
  fun showPopularTvShows_FailedFetch_RecyclerViewNotDisplayed() {
    mockWebServer.enqueue(getErrorResponse())
    mockWebServer.enqueue(getErrorResponse())
    mockWebServer.enqueue(getErrorResponse())

    activityScenario = ActivityScenario.launch(MainActivity::class.java)

    onView(allOf(withText(R.string.tv_shows_label),
        isDescendantOfA(withId(R.id.tab_layout_movie_and_tv_shows))))
        .perform(click())
        .check(matches(isDisplayed()))

    onView(withId(R.id.recycler_view_home_tv)).isGone()
    onView(withId(R.id.group_tv_empty_state)).isVisible()
  }
}