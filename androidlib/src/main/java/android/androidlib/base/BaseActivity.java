package android.androidlib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author liuml
 * @explain 与业务无关的 Activity 基类
 * @time 2017/12/5 09:52
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //这里setContentView 必须放在最前面
        setContentView(setLayoutId());
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        BaseApplication.getInstance().addActivity(this);
        mContext = this;
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * 初始化Layout
     */
    protected abstract int setLayoutId();

    /**
     * 初始化变量，包括 Intent 带的数据和 Activity 内的变量。
     */
    protected abstract void initVariables();

    /**
     * 加载 layout 布局文件，初始化控件，为控件挂上事件方法。
     *
     * @param savedInstanceState
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 调用 MobileAPI 获取数据。
     */
    protected abstract void loadData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().finishActivity(this);
    }
}
