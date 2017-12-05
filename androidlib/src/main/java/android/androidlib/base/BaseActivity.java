package android.androidlib.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author liuml
 * @explain 与业务无关的 Activity 基类
 * @time 2017/12/5 09:52
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

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
}
