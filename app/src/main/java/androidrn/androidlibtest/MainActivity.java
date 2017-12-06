package androidrn.androidlibtest;

import android.androidlib.utils.StringUtils;
import android.androidlib.utils.ThreadManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidrn.androidlibtest.base.AppBaseActivity;
import androidrn.androidlibtest.view.MyProgressBar;
/**
 * @explain 重构 夜未眠
 * @author liuml.
 * @time 2017/12/6 16:49
 */
public class MainActivity extends AppBaseActivity {

    private String firstLetter;
    private TextView tvStringSub;
    private TextView tvStringBuilder;
    private String str;
    private Button btStartProgress;
    private MyProgressBar myProgressbar;



    private Message message;
    private int progress = 0;
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
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables() {

        String cityName = "Tsdfasf";
        firstLetter = "";
        firstLetter = StringUtils.safeSubString(cityName, 3, 5);
        str = StringUtils.convertToString("10", "");

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
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
