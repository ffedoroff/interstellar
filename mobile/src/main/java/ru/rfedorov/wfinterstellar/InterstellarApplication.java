package ru.rfedorov.wfinterstellar;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class InterstellarApplication extends Application {
    private static final String TAG = "RFApplication";

    private static Context context;

    public void onCreate() {
        Log.v(TAG, "RFApplication onCreate");
        super.onCreate();
        InterstellarApplication.context = getApplicationContext();
        Controller.getInstance();
    }

    public static Context getAppContext() {
        return InterstellarApplication.context;
    }


}
