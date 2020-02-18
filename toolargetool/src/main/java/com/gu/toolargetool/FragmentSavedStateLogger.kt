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
            if (sizeAsParcel(outState) > TooLargeTool.MAXIMUM_SIZE_IN_BYTES) {
                logTransactionTooLargeDetected(f, fm, outState)
            }
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        logAndRemoveSavedState(f, fm)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        logAndRemoveSavedState(f, fm)
    }

    private fun logTransactionTooLargeDetected(fragment: Fragment, fragmentManager: FragmentManager, bundle: Bundle) {
        try {
            val message = formatter.format(fragmentManager, fragment, bundle)
            logger.logTransactionTooLargeDetected(message)
        } catch (e: RuntimeException) {
            logger.logException(e)
        }
    }

    private fun logAndRemoveSavedState(fragment: Fragment, fragmentManager: FragmentManager) {
        val savedState = savedStates.remove(fragment)
        if (savedState != null) {
            try {
                val message = formatter.format(fragmentManager, fragment, savedState)
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
