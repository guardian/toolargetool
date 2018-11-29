package com.gu.toolargetool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks} implementation that
 * logs information about the saved state of Fragments.
 */
public class FragmentSavedStateLogger extends FragmentManager.FragmentLifecycleCallbacks {
    @NonNull private Formatter formatter;
    @NonNull private Logger logger;
    @NonNull private final Map<Fragment, Bundle> savedStates = new HashMap<>();
    private boolean isLogging = true;

    public FragmentSavedStateLogger(@NonNull Formatter formatter, @NonNull Logger logger) {
        this.formatter = formatter;
        this.logger = logger;
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        if (isLogging) {
            savedStates.put(f, outState);
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        Bundle savedState = savedStates.remove(f);
        if (savedState != null) {
            try {
                String message = formatter.format(fm, f, savedState);
                logger.log(message);
            } catch (RuntimeException e) {
                logger.logException(e);
            }
        }
    }

    void startLogging() {
        isLogging = true;
    }

    void stopLogging() {
        isLogging = false;
    }
}
