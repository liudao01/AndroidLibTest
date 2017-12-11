package android.androidlib.base;

import android.androidlib.utils.Utils;
import android.app.Application;

/**
 * @author liuml
 * @explain
 * @time 2017/12/8 14:12
 */

public class BaseApplication extends Application {
    private static BaseApplication baseApplication;

    public BaseApplication() {
        baseApplication = this;
    }

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
