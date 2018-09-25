package com.gu.toolargetool;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link android.app.Application.ActivityLifecycleCallbacks} implementation that logs information
 * about the saved state of Activities.
 */
public class ActivitySavedStateLogger extends EmptyActivityLifecycleCallbacks {

    private int priority;
    @NonNull private String tag;
    @Nullable private final FragmentSavedStateLogger fragmentLogger;
    @NonNull private final Map<Activity, Bundle> savedStates = new HashMap<>();
    private boolean isLogging;

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
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof FragmentActivity && fragmentLogger != null) {
            final FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity
                    .getSupportFragmentManager()
                    .unregisterFragmentLifecycleCallbacks(fragmentLogger);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (isLogging) {
            savedStates.put(activity, outState);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Bundle savedState = savedStates.remove(activity);
        if (savedState != null) {
            log(activity.getClass().getSimpleName() + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(savedState));
        }
    }

    void setTag(@NonNull String tag) {
        this.tag = tag;
    }

    void setPriority(int priority) {
        this.priority = priority;
    }

    void startLogging() {
        isLogging = true;

        if (fragmentLogger != null) {
            fragmentLogger.startLogging();
        }
    }

    void stopLogging() {
        isLogging = false;
        savedStates.clear();

        if (fragmentLogger != null) {
            fragmentLogger.stopLogging();
        }
    }

    boolean isLogging() {
        return isLogging;
    }
}
