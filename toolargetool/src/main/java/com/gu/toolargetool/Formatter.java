package com.gu.toolargetool;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by sfriedenberg on 09/20/2017.
 */

public abstract class Formatter {
    public abstract String format(Activity activity, Bundle bundle);
    public abstract String format(FragmentManager fragmentManager, Fragment fragment, Bundle bundle);

    public static Formatter DEFAULT_FORMATTER = new Formatter() {
        @Override
        public String format(Activity activity, Bundle bundle) {
            return activity.getClass().getSimpleName() + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(bundle);
        }

        @Override
        public String format(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
            String message = fragment.getClass().getSimpleName() + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(bundle);
            Bundle fragmentArguments = fragment.getArguments();
            if (fragmentArguments != null) {
                message += "\n* fragment arguments = " + TooLargeTool.bundleBreakdown(fragmentArguments);
            }

            return message;
        }
    };
}
