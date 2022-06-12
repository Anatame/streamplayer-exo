package com.anatame.exoplayer

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import timber.log.Timber

class ExoplayerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ":${element.methodName}:${element.lineNumber}"
                }
            })
        }
    }

}