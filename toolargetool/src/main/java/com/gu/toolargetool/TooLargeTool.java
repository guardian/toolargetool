package com.gu.toolargetool;

import android.app.Application;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A collection of helper methods to assist you in debugging crashes due to
 * {@link android.os.TransactionTooLargeException}.
 * <p>
 * The easiest way to use this class is to call {@link #startLogging(Application)} in your app's
 * {@link Application#onCreate()} method.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class TooLargeTool {

    private static ActivitySavedStateLogger logger;

    private TooLargeTool() {
        // Static only
    }

    /**
     * Helper method to print the result of {@link #bundleBreakdown(Bundle)} to ADB.
     * <p>
     * Logged at DEBUG priority.
     *
     * @param bundle to log the breakdown of
     * @param tag to log with
     */
    public static void logBundleBreakdown(String tag, @NonNull Bundle bundle) {
        Log.println(Log.DEBUG, tag, bundleBreakdown(bundle));
    }

    /**
     * Helper method to print the result of {@link #bundleBreakdown(Bundle)} to ADB.
     *
     * @param bundle to log the breakdown of
     * @param tag to log with
     * @param priority to log with
     */
    public static void logBundleBreakdown(String tag, int priority, @NonNull Bundle bundle) {
        Log.println(priority, tag, bundleBreakdown(bundle));
    }

    /**
     * Return a formatted String containing a breakdown of the contents of a {@link Bundle}.
     *
     * @param bundle to format
     * @return a nicely formatted string (multi-line)
     */
    @NonNull
    public static String bundleBreakdown(@NonNull Bundle bundle) {
        final SizeTree sizeTree = sizeTreeFromBundle(bundle);
        String result = String.format(
                Locale.UK,
                "%s contains %d keys and measures %,.1f KB when serialized as a Parcel",
                sizeTree.getKey(), sizeTree.getSubTrees().size(), KB(sizeTree.getSize())
        );
        for (SizeTree subTree: sizeTree.getSubTrees()) {
            result += String.format(
                    Locale.UK,
                    "\n* %s = %,.1f KB",
                    subTree.getKey(), KB(subTree.getSize())
            );
        }
        return result;
    }

    /**
     * Measure the sizes of all the values in a typed {@link Bundle} when written to a
     * {@link Parcel}. Returns a map from keys to the sizes, in bytes, of the associated values in
     * the Bundle.
     *
     * @param bundle to measure
     * @return a map from keys to value sizes in bytes
     */
    public static SizeTree sizeTreeFromBundle(@NonNull Bundle bundle) {
        final List<SizeTree> results = new ArrayList<>(bundle.size());
        // We measure the size of each value by measuring the total size of the bundle before and
        // after removing that value and calculating the difference. We make a copy of the original
        // bundle so we can put all the original values back at the end. It's not possible to
        // carry out the measurements on the copy because of the way Android parcelables work
        // under the hood where certain objects are actually stored as references.
        Bundle copy = new Bundle(bundle);
        try {
            int bundleSize = sizeAsParcel(bundle);
            // Iterate over copy's keys because we're removing those of the original bundle
            for (String key : copy.keySet()) {
                bundle.remove(key);
                int newBundleSize = sizeAsParcel(bundle);
                int valueSize = bundleSize - newBundleSize;
                results.add(new SizeTree(key, valueSize));
                bundleSize = newBundleSize;
            }
            return new SizeTree("Bundle" + System.identityHashCode(bundle), sizeAsParcel(bundle), results);
        } finally {
            // Put everything back into original bundle
            bundle.putAll(copy);
        }
    }


    /**
     * Measure the size of a typed {@link Bundle} when written to a {@link Parcel}.
     *
     * @param bundle to measure
     * @return size when written to parcel in bytes
     */
    public static int sizeAsParcel(@NonNull Bundle bundle) {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeBundle(bundle);
            return parcel.dataSize();
        } finally {
            parcel.recycle();
        }
    }

    /**
     * Measure the size of a {@link Parcelable} when written to a {@link Parcel}.
     *
     * @param parcelable to measure
     * @return size in parcel in bytes
     */
    public static int sizeAsParcel(@NonNull Parcelable parcelable) {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeParcelable(parcelable, 0);
            return parcel.dataSize();
        } finally {
            parcel.recycle();
        }
    }

    private static float KB(int bytes) {
        return ((float) bytes)/1000f;
    }

    /**
     * Start logging information about all of the state saved by Activities and Fragments. Logs are
     * written at {@link Log#DEBUG DEBUG} priority with the default tag: "TooLargeTool".
     *
     * @param application to log
     */
    public static void startLogging(Application application) {
        startLogging(application, Log.DEBUG, "TooLargeTool");
    }

    /**
     * Start logging information about all of the state saved by Activities and Fragments.
     *
     * @param application to log
     * @param priority to write log messages at
     * @param tag for log messages
     */
    public static void startLogging(Application application, int priority, @NonNull String tag) {
        if (logger == null) {
            logger = new ActivitySavedStateLogger(priority, tag, true);
        } else {
            logger.setPriority(priority);
            logger.setTag(tag);
        }

        if (logger.isLogging()) {
            return;
        }

        logger.startLogging();
        application.registerActivityLifecycleCallbacks(logger);
    }

    /**
     * Stop all logging.
     *
     * @param application to stop logging
     */
    public static void stopLogging(Application application) {
        if (!logger.isLogging()) {
            return;
        }

        logger.stopLogging();
        application.unregisterActivityLifecycleCallbacks(logger);
    }

    public static boolean isLogging() {
        return logger.isLogging();
    }
}
