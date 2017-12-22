package androidrn.androidlibtest;

import android.androidlib.net.XHttp;
import android.androidlib.utils.FragmentController;
import android.androidlib.utils.FragmentUtils;
import android.androidlib.utils.StringUtils;
import android.androidlib.utils.ThreadManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidrn.androidlibtest.base.AppBaseActivity;
import androidrn.androidlibtest.network.AndroidNetEngine;
import androidrn.androidlibtest.view.MyProgressBar;

/**
 * @author liuml.
 * @explain 重构 夜未眠
 * @time 2017/12/6 16:49
 */
public class MainActivity extends AppBaseActivity {

    private String firstLetter;
    private TextView tvStringSub;
    private TextView tvStringBuilder;
    private String str;
    private Button btStartProgress;
    private MyProgressBar myProgressbar;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private BlankFragment blankFragment;
    private Fragment blankFragment2;
    private Message message;
    private int progress = 0;
    private FrameLayout fragmentContent;

    private FragmentManager fm;
    private FragmentTransaction ft;
    FragmentManager fragmentManager;
    FragmentController fragmentController;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int p = msg.what;
            myProgressbar.setProgress(p);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始
        XHttp.init(new AndroidNetEngine());
//        fragmentController.add(true, "1", R.id.fragment_content, blankFragment);
        FragmentUtils.add(fragmentManager,blankFragment,R.id.fragment_content);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables() {
        fragmentController = new FragmentController(this);
        String cityName = "Tsdfasf";
        firstLetter = "";
        firstLetter = StringUtils.safeSubString(cityName, 3, 5);
        str = StringUtils.convertToString("10", "");

        blankFragment = new BlankFragment();
        blankFragment2 = BlankFragment2.newInstance("e", "e");
        fragmentManager =getSupportFragmentManager();
//        ft = fm.beginTransaction();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        fragmentContent = (FrameLayout) findViewById(R.id.fragment_content);
        btStartProgress = (Button) findViewById(R.id.bt_start_progress);
        tvStringSub = (TextView) findViewById(R.id.tv_string_sub);
        tvStringSub.setText(StringUtils.stringBulider("字符串截取使用: ", firstLetter));
        tvStringBuilder = (TextView) findViewById(R.id.tv_string_builder);
        tvStringBuilder.setText(StringUtils.stringBulider("字符串截取使用: ", str));

        myProgressbar = (MyProgressBar) findViewById(R.id.my_progressbar);
        btStartProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 线程池工具类使用
                 */
                ThreadManager.getNormalPool().execute(runnable);
            }
        });

//        FragmentUtils.add(fragmentManager,blankFragment,R.id.fragment_content);
    }

    @Override
    protected void loadData() {

    }


    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            message = handler.obtainMessage();
            // TODO Auto-generated method stub
            try {
                for (int i = 1; i <= 100; i++) {
                    int x = progress++;
                    message.what = x;
                    handler.sendEmptyMessage(message.what);
                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
