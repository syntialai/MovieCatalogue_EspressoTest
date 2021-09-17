package com.syntia.moviecatalogue

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AndroidTestAppJUnitRunner : AndroidJUnitRunner() {

  override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
    return super.newApplication(cl, AndroidTestApp::class.java.name, context)
  }
}