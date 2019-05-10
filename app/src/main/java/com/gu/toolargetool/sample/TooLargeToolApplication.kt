package com.gu.toolargetool.sample

import android.app.Application

import com.gu.toolargetool.TooLargeTool

@Suppress("unused")
class TooLargeToolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TooLargeTool.startLogging(this)
    }
}
