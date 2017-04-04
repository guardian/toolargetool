package com.gu.toolargetool.sample;

import android.app.Application;
import android.util.Log;

import com.gu.toolargetool.TooLargeTool;

/**
 * TODO
 */

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TooLargeTool.logEverything(this, "CustomTag", Log.DEBUG);
    }
}
