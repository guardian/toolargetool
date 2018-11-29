package com.gu.toolargetool;

import android.util.Log;

/**
 * Created by sfriedenberg on 09/20/2017.
 */

public class Logger {
    protected int priority;
    protected String tag;

    public Logger(int priority, String tag) {
        this.priority = priority;
        this.tag = tag;
    }

    /**
     * override this method if you wish to use your own logging facilities
     *
     * @param msg
     */
    public void log(String msg) {
        Log.println(priority, tag, msg);
    }

    /**
     * In the case that the formatter throws an exception during formatting,
     * this method is called to log the exception. Like the above log method, you can override this too
     *
     * @param e
     */
    public void logException(Exception e) {
        Log.println(priority, tag, e.getMessage());
    }

    public static Logger DEFAULT_LOGGER = new Logger(Log.DEBUG, "TooLargeTool");
}
