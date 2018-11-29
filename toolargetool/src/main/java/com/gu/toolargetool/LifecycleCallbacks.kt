package com.gu.toolargetool

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

fun fragmentStateSavedCallbacks(
        func: (Fragment, Bundle) -> Unit
) = object : FragmentManager.FragmentLifecycleCallbacks() {

    val savedStates = mutableMapOf<Fragment, Bundle?>()

    override fun onFragmentSaveInstanceState(fm: FragmentManager, fragment: Fragment, outState: Bundle?) {
        savedStates[fragment] = outState
    }

    override fun onFragmentStopped(fm: FragmentManager, fragment: Fragment) {
        func(fragment, savedStates.remove(fragment) ?: return)
    }
}

fun activityStateSavedCallbacks(
        func: (Activity, Bundle) -> Unit
): Application.ActivityLifecycleCallbacks = object : BaseActivityLifecycleCallbacks() {

    val savedStates = mutableMapOf<Activity, Bundle?>()

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        savedStates[activity] = outState
    }

    override fun onActivityStopped(activity: Activity) {
        func(activity, savedStates.remove(activity) ?: return)
    }
}

fun stateSavedCallbacks(
        func: (SavedState) -> Unit
): Application.ActivityLifecycleCallbacks = object : BaseActivityLifecycleCallbacks() {

    val activityCallbacks = activityStateSavedCallbacks { activity, bundle ->
        func(ActivitySavedState(activity, bundle))
    }

    val fragmentCallbacks = fragmentStateSavedCallbacks { fragment, bundle ->
        func(FragmentSavedState(fragment, bundle))
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, true)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        activityCallbacks.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityStopped(activity: Activity) {
        activityCallbacks.onActivityStopped(activity)
    }
}

fun Application.addOnStateSavedListener(func: (SavedState) -> Unit) {
    registerActivityLifecycleCallbacks(stateSavedCallbacks(func))
}

sealed class SavedState(val name: String, val state: Bundle)
class ActivitySavedState(val activity: Activity, state: Bundle): SavedState(activity.localClassName, state)
class FragmentSavedState(val fragment: Fragment, state: Bundle): SavedState(fragment::class.java.simpleName, state)

private open class BaseActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
}
