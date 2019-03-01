package com.i605.eightball

import android.app.Application
import android.content.Context
import com.i605.eightball.helper.ExceptionHelper

class App : Application() {
    companion object {
        private lateinit var mInstance: App

        fun getAppContext(): Context = mInstance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        ExceptionHelper
    }
}