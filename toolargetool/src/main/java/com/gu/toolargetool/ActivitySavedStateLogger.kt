package com.gu.toolargetool

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import java.util.*

/**
 * [android.app.Application.ActivityLifecycleCallbacks] implementation that logs information
 * about the saved state of Activities.
 */
class ActivitySavedStateLogger(
        private val formatter: Formatter,
        private val logger: Logger,
        logFragments: Boolean
) : Application.ActivityLifecycleCallbacks {

    private val fragmentLogger = if (logFragments) FragmentSavedStateLogger(formatter, logger) else null
    private val savedStates = HashMap<Activity, Bundle>()
    var isLogging: Boolean = false
        private set

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity && fragmentLogger != null) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(fragmentLogger, true)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        logAndRemoveSavedState(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        if (isLogging && (outState != null)) {
            savedStates[activity] = outState
        }
    }

    override fun onActivityStopped(activity: Activity) {
        logAndRemoveSavedState(activity)
    }

    private fun logAndRemoveSavedState(activity: Activity) {
        val savedState = savedStates.remove(activity)
        if (savedState != null) {
            try {
                val message = formatter.format(activity, savedState)
                logger.log(message)
            } catch (e: RuntimeException) {
                logger.logException(e)
            }

        }
    }

    fun startLogging() {
        isLogging = true
        fragmentLogger?.startLogging()
    }

    fun stopLogging() {
        isLogging = false
        savedStates.clear()
        fragmentLogger?.stopLogging()
    }

    override fun onActivityStarted(activity: Activity) {
        // Unused
    }

    override fun onActivityResumed(activity: Activity) {
        // Unused
    }

    override fun onActivityPaused(activity: Activity) {
        // Unused
    }
}
