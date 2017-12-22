package androidrn.androidlibtest;

import android.androidlib.net.HttpCallBack;
import android.androidlib.net.XHttp;
import android.androidlib.utils.CleanUtils;
import android.androidlib.utils.ToastUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btClearCache;
    private Button btGetTest;
    private Button btPostTest;
    private TextView tvMsg;

    private String secret = "56a1c454a9b946e3a70a1069e21d038c";
    private String appid = "38002";


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {

        btClearCache = (Button) view.findViewById(R.id.bt_clear_cache);
        btGetTest = (Button) view.findViewById(R.id.bt_get_test);
        btPostTest = (Button) view.findViewById(R.id.bt_post_test);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        btClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CleanUtils.cleanInternalDbs();
                CleanUtils.cleanInternalSP();
                if (CleanUtils.cleanExternalCache()) {
                    ToastUtils.showShort("清除成功");
                }
            }
        });

        btGetTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
            }
        });
        btPostTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });

    }


    private void get(){
        //        time	String		2015-07-10	否	从这个时间以来最新的笑话.
//        格式：yyyy-MM-dd
//        page	String	1	1	否	第几页。
//        maxResult	String	20	20	否	每页最大记录数。其值为1至50。
        String secret = "56a1c454a9b946e3a70a1069e21d038c";
        String appid = "38002";
        String time = getTimeDay();
        String page = "1";
        String maxResult = "30";

        Map map = new HashMap();
        map.put("showapi_appid",secret);
        map.put("showapi_sign",appid);
        map.put("time",time);
        map.put("page",page);
        map.put("maxResult",maxResult);
        XHttp.getInstance().get(getActivity(), "http://route.showapi.com/341-1", map, new HttpCallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject o) {
                tvMsg.setText("get请求: "+o.toString());
            }
        });



    }
    private void post(){
        String secret = "56a1c454a9b946e3a70a1069e21d038c";
        String appid = "38002";

        Map map = new HashMap();
        map.put("showapi_appid",secret);
        map.put("showapi_sign",appid);
        map.put("q","这是翻译的英文");
        XHttp.getInstance().post(getActivity(), "http://route.showapi.com/32-9", map, new HttpCallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject o) {
                tvMsg.setText("post请求 : "+o.toString());
            }
        });



    }

    public static String getTimeDay(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());

        return date;

    }
}