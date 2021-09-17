package com.syntia.moviecatalogue.search

import android.view.View
import android.widget.SearchView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.syntia.moviecatalogue.R
import com.syntia.moviecatalogue.base.BaseInstrumentationTest
import com.syntia.moviecatalogue.features.search.adapter.SearchResultAdapter
import com.syntia.moviecatalogue.features.search.view.SearchActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.any
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchActivityTest : BaseInstrumentationTest() {

  companion object {
    private const val QUERY = "QUERY"
  }

  private lateinit var activityScenario: ActivityScenario<SearchActivity>

  override fun tearDown() {
    super.tearDown()
    activityScenario.close()
  }

  @Test
  fun searchByQuery_SuccessFetch_ComponentsDisplayed() {
    mockWebServer.enqueue(getSuccessResponse("searchQuery_success_response.json"))

    activityScenario = ActivityScenario.launch(SearchActivity::class.java)

    onView(withId(R.id.search_view)).perform(typeAndSubmitSearchViewText(QUERY))
    closeKeyboard()

    onView(withId(R.id.group_search_empty_state)).isGone()
    with(onView(withId(R.id.recycler_view_search))) {
      isVisible()
      perform(
          RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(1,
              ViewActions.click()))
    }
  }

  @Test
  fun searchByQuery_SuccessFetch_EmptyResult_RecyclerViewNotDisplayed() {
    mockWebServer.enqueue(getEmptyResponse())

    activityScenario = ActivityScenario.launch(SearchActivity::class.java)

    onView(withId(R.id.search_view)).perform(typeAndSubmitSearchViewText(QUERY))
    closeKeyboard()

    onView(withId(R.id.group_search_empty_state)).isVisible()
    onView(withId(R.id.recycler_view_search)).isGone()
  }

  @Test
  fun searchByQuery_FailedFetch_RecyclerViewNotDisplayed() {
    mockWebServer.enqueue(getErrorResponse())

    activityScenario = ActivityScenario.launch(SearchActivity::class.java)

    onView(withId(R.id.search_view)).perform(typeAndSubmitSearchViewText(QUERY))
    closeKeyboard()

    onView(withId(R.id.group_search_empty_state)).isGone()
    onView(withId(R.id.recycler_view_search)).isGone()
  }

  private fun typeAndSubmitSearchViewText(text: String): ViewAction {
    return object : ViewAction {
      override fun getConstraints(): Matcher<View> = allOf(isDisplayed(),
          isAssignableFrom(SearchView::class.java))

      override fun getDescription(): String = "type search view text"

      override fun perform(uiController: UiController?, view: View?) {
        (view as SearchView).setQuery(text, true)
      }
    }
  }
}