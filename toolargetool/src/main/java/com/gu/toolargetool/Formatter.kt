package com.gu.toolargetool

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Interface that allows flexibility in how TooLargeTool's output is formatted. The default
 * implementation [DefaultFormatter] should be suitable in most cases.
 */
interface Formatter {
    fun format(activity: Activity, bundle: Bundle): String
    fun format(fragmentManager: androidx.fragment.app.FragmentManager, fragment: androidx.fragment.app.Fragment, bundle: Bundle): String
}

/**
 * The default implementation of [Formatter].
 *
 * @author [@sfriedenberg](https://github.com/friedenberg)
 */
class DefaultFormatter: Formatter {
    override fun format(activity: Activity, bundle: Bundle): String {
        return activity.javaClass.simpleName + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(bundle)
    }

    override fun format(fragmentManager: androidx.fragment.app.FragmentManager, fragment: androidx.fragment.app.Fragment, bundle: Bundle): String {
        var message = fragment.javaClass.simpleName + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(bundle)
        val fragmentArguments = fragment.arguments
        if (fragmentArguments != null) {
            message += "\n* fragment arguments = " + TooLargeTool.bundleBreakdown(fragmentArguments)
        }

        return message
    }
}
