package android.androidlib.base;

import android.app.Application;

/**
 * @author liuml
 * @explain
 * @time 2017/12/8 14:12
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public MyApplication() {
        myApplication = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}
