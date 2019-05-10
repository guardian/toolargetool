package com.gu.toolargetool

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*

/**
 * [FragmentManager.FragmentLifecycleCallbacks] implementation that logs information about the
 * saved state of Fragments.
 */
class FragmentSavedStateLogger(private val formatter: Formatter, private val logger: Logger) : FragmentManager.FragmentLifecycleCallbacks() {
    private val savedStates = HashMap<Fragment, Bundle?>()
    private var isLogging = true

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        if (isLogging) {
            savedStates[f] = outState
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        val savedState = savedStates.remove(f)
        if (savedState != null) {
            try {
                val message = formatter.format(fm, f, savedState)
                logger.log(message)
            } catch (e: RuntimeException) {
                logger.logException(e)
            }

        }
    }

    internal fun startLogging() {
        isLogging = true
    }

    internal fun stopLogging() {
        isLogging = false
    }
}
