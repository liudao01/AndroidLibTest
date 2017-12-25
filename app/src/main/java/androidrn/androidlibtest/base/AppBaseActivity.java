package androidrn.androidlibtest.base;

import android.androidlib.base.BaseActivity;
import android.androidlib.net.XHttp;

/**
 * @author liuml
 * @explain 本项目的基类
 * @time 2017/12/6 15:50
 */

public abstract  class AppBaseActivity extends BaseActivity{


    @Override
    protected void onDestroy() {
        XHttp.getInstance().removeTag(this);
        super.onDestroy();
    }
}
