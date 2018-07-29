package com.gu.toolargetool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link android.app.Application.ActivityLifecycleCallbacks} implementation that logs information
 * about the saved state of Activities.
 */
public class ActivitySavedStateLogger extends EmptyActivityLifecycleCallbacks {

    private final int priority;
    @NonNull private final String tag;
    @Nullable private final FragmentSavedStateLogger fragmentLogger;
    @NonNull private final Map<Activity, Bundle> savedStates = new HashMap<>();
    @NonNull private final List<FragmentActivity> createdActivities = new ArrayList<>();

    public ActivitySavedStateLogger(int priority, @NonNull String tag, boolean logFragments) {
        this.priority = priority;
        this.tag = tag;
        fragmentLogger = logFragments ? new FragmentSavedStateLogger(priority, tag) : null;
    }

    private void log(String msg) {
        Log.println(priority, tag, msg);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof FragmentActivity && fragmentLogger != null) {
            final FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity
                    .getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(fragmentLogger, true);
            createdActivities.add(fragmentActivity);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof FragmentActivity && fragmentLogger != null) {
            final FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity
                    .getSupportFragmentManager()
                    .unregisterFragmentLifecycleCallbacks(fragmentLogger);
            createdActivities.remove(fragmentActivity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        savedStates.put(activity, outState);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Bundle savedState = savedStates.remove(activity);
        if (savedState != null) {
            log(activity.getClass().getSimpleName() + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(savedState));
        }
    }

    public void stopLogging() {
        if (fragmentLogger == null) {
            return;
        }

        for (FragmentActivity activity : createdActivities) {
            activity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentLogger);
        }
    }
}
