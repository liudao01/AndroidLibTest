package android.androidlib.net;

import android.content.Context;

import java.util.Map;

/**
 * @author liuml
 * @explain
 * @time 2017/12/16 19:37
 */

public interface HttpEngine {

    void get(Context context, String url, Map<String, String> params, HttpCallBack callBack);

    void post(Context context, String url, Map<String, String> params, HttpCallBack callBack);
    void download(Context context, String url, Map<String, String> params, HttpCallBack callBack);
}
