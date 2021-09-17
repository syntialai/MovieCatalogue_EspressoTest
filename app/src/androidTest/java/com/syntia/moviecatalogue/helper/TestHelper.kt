package com.syntia.moviecatalogue.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import java.io.InputStreamReader
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matcher

@ExperimentalCoroutinesApi
fun readStringFromFile(fileName: String): String {
  try {
    val inputStream = InstrumentationRegistry.getInstrumentation().context.resources.assets.open(
        fileName)
    val stringBuilder = StringBuilder()
    val reader = InputStreamReader(inputStream, "UTF-8")
    reader.readLines().forEach {
      stringBuilder.append(it)
    }
    return stringBuilder.toString()
  } catch (exception: IOException) {
    throw exception
  }
}

fun withImageDrawable(resourceId: Int): Matcher<View?> {
  return object : BoundedMatcher<View?, ImageView>(ImageView::class.java) {
    override fun describeTo(description: Description) {
      description.appendText("has image drawable resource $resourceId")
    }

    override fun matchesSafely(imageView: ImageView): Boolean {
      return sameBitmap(imageView.context, imageView.drawable, resourceId)
    }
  }
}

private fun sameBitmap(context: Context, drawable: Drawable?, resourceId: Int): Boolean {
  var drawableInView: Drawable? = drawable
  var drawableToMatch: Drawable? = ContextCompat.getDrawable(context, resourceId)
  return if (listOfNotNull(drawable, drawableToMatch).isEmpty()) {
    false
  } else {
    if (drawable is StateListDrawable && drawableToMatch is StateListDrawable) {
      drawableInView = drawable.current
      drawableToMatch = drawableToMatch.current
    }
    return getBitmap(drawableInView).sameAs(getBitmap(drawableToMatch))
  }
}

private fun getBitmap(drawable: Drawable?) = if (drawable is VectorDrawable) {
  vectorToBitmap(drawable)
} else {
  (drawable as BitmapDrawable).bitmap
}

private fun vectorToBitmap(vectorDrawable: VectorDrawable): Bitmap {
  val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight,
      Bitmap.Config.ARGB_8888)
  vectorDrawable.apply {
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
  }
  return bitmap
}
