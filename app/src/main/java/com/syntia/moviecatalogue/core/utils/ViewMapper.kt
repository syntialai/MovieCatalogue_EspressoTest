package com.syntia.moviecatalogue.core.utils

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import com.google.android.material.progressindicator.CircularProgressIndicator

fun View.show() {
  this.visibility = View.VISIBLE
}

fun View.remove() {
  this.visibility = View.GONE
}

fun View.hide() {
  this.visibility = View.INVISIBLE
}

fun View.showOrRemove(show: Boolean) {
  if (show) {
    show()
  } else {
    remove()
  }
}

fun CircularProgressIndicator.showView() {
  this.visibility = View.VISIBLE
}

@ColorRes
fun Context.getColorResFromAttrs(@AttrRes attr: Int): Int {
  val attrs = intArrayOf(attr)
  val typedArray: TypedArray = obtainStyledAttributes(attrs)
  val colorRes: Int = typedArray.getResourceId(0, android.R.color.black)
  typedArray.recycle()
  return colorRes
}