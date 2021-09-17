package com.syntia.moviecatalogue.base

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import com.syntia.moviecatalogue.AndroidTestApp
import com.syntia.moviecatalogue.core.utils.IdlingResourceHelper
import com.syntia.moviecatalogue.helper.readStringFromFile
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseInstrumentationTest {

  companion object {
    const val ID = 123
    const val MEDIA_TYPE_MOVIE = "movie"
    const val MEDIA_TYPE_TV = "tv"
    const val YEAR =  "2021"
    const val TITLE = "Finding Nemo"
    const val IMAGE = "/abc.jpg"
    const val VOTE_AVERAGE_STRING = "8.8"
  }

  protected val mockWebServer = MockWebServer()

  @Before
  open fun setUp() {
    mockWebServer.start(AndroidTestApp.PORT)
    IdlingRegistry.getInstance().register(IdlingResourceHelper.idlingResource)
  }

  @After
  open fun tearDown() {
    IdlingRegistry.getInstance().unregister(IdlingResourceHelper.idlingResource)
    mockWebServer.shutdown()
  }

  protected fun getSuccessResponse(fileName: String) = MockResponse().setResponseCode(
      HttpURLConnection.HTTP_OK).setBody(readStringFromFile(fileName))

  protected fun getEmptyResponse() = getSuccessResponse("listItem_empty_response.json")

  protected fun getErrorResponse() = MockResponse().throttleBody(1024, 100, TimeUnit.SECONDS)

  protected fun ViewInteraction.isVisible() {
    check(matches(
        ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
  }

  protected fun ViewInteraction.isGone() {
    check(
        matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
  }

  protected fun closeKeyboard() {
    onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
  }
}