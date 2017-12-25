package androidrn.androidlibtest.network;

import android.androidlib.base.BaseApplication;
import android.androidlib.net.HttpCallBack;
import android.androidlib.net.HttpEngine;
import android.androidlib.net.XHttp;
import android.androidlib.utils.AppUtils;
import android.androidlib.utils.JSONUtils;
import android.androidlib.utils.LogUtil;
import android.androidlib.utils.NetworkUtils;
import android.androidlib.utils.SystemUtils;
import android.androidlib.utils.ToastUtils;
import android.content.Context;
import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * @author liuml
 * @explain 网络引擎 具体实现
 * @time 2017/12/16 19:44
 */

public class AndroidNetEngine implements HttpEngine {
    private OkHttpClient mOkHttpClient = null;
    private static final int DEFAULT_TIMEOUT = 10;
    private Context mContext;

    public void AndroidNetEngine() {

    }

    @Override
    public void get(Context context, String url, Map<String, String> params, final HttpCallBack callBack) {
        if (callBack == null) {
            return;
        }
        if (!befoRequest(context)) {
            return;
        }

        //判断是否自己的接口
        boolean isMyUrl = true;

        //初始化okhttp
        initOkhttp();
        //获取Class
        Class<?> aClass = XHttp.analysisClassInfo(callBack);

        if (null == params) {
            params = new HashMap<>();
        }
        ANRequest.GetRequestBuilder get = AndroidNetworking.get(url);
        //自己接口

        //请求头
        Map<String, String> header = new HashMap<>();
        //默认的header头
        get.addHeaders(header);
        get.setTag(context);
        get.setOkHttpClient(mOkHttpClient);
        ANRequest build = get.addQueryParameter(params).build();

        //字符串类型
        if (String.class.getName().equals(aClass.getName())) {
            commonGetString(isMyUrl, build, callBack);
        } else if (JSONObject.class.getName().equals(aClass.getName())) {
            //返回JsonObject类型
            commonGetJsonObject(build, callBack);
        } else {
            commonGetJsonObject(build, callBack);
            //返回引用类型
//            commonGetObject(build, callBack);
        }


    }

    /**
     * 判断网络是否正常  添加对话框
     *
     * @param context
     * @return
     */
    private boolean befoRequest(Context context) {
        mContext = context;
//        if (!NetworkUtil.isNetworkAvailable(context)) {
//            ToastUtils.showCenter("网络不可用,请检查网络设置");
//            return false;
//        } else {
        return true;

//        }
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, final HttpCallBack callBack) {
        if (callBack == null) {
            return;
        }
        if (!befoRequest(context)) {
            return;
        }
        initOkhttp();
        //获取Class
        Class<?> aClass = XHttp.analysisClassInfo(callBack);
        //判断是否自己的接口
        boolean isMyUrl = true;

        ANRequest.PostRequestBuilder post = AndroidNetworking.post(url);
        //签名
        if (null == params) {
            params = new HashMap<>();
        }
        //自己接口
        //请求头
        Map<String, String> header = new HashMap<>();
        post.addHeaders(header);

        post.setOkHttpClient(mOkHttpClient);

        ANRequest build = post.addBodyParameter(params).build();

        //字符串类型
        if (String.class.getName().equals(aClass.getName())) {
            //实际请求
            commonGetString(isMyUrl, build, callBack);
        } else if (JSONObject.class.getName().equals(aClass.getName())) {
            //返回JsonObject类型
            commonGetJsonObject(build, callBack);
        } else {
            ////返回引用类型
            commonGetJsonObject(build, callBack);
//            commonGetObject(build, callBack);
        }

    }

    @Override
    public void download(Context context, String url, Map<String, String> params, HttpCallBack callBack) {

    }


    @Override
    public void removeAll() {
        AndroidNetworking.cancelAll();
    }

    @Override
    public void removeTag(Object obj) {
        LogUtil.d("removeTag get teg context = "+obj);
        AndroidNetworking.cancel(obj);
    }



    /**
     * 获取通用的JsonObject类型
     *
     * @param build
     * @param callBack
     */
    private void commonGetJsonObject(ANRequest build, final HttpCallBack callBack) {

        build.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.optInt("code");
                    String msg = response.optString("msg");
                    JSONObject object = response;
                    if (JSONObject.class.getName().equals(XHttp.analysisClassInfo(callBack).getName())) {
                        //JsonObject
                        callBack.onSuccess(object);
                    } else {
                        //引用类型
                        callBack.onSuccess(JSONUtils.parseObject(object, XHttp.analysisClassInfo(callBack)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFailed(-500, "数据解析错误");
                }
            }

            @Override
            public void onError(ANError anError) {
                //com.androidnetworking.error.ANError: java.net.SocketTimeoutException
                callBack.onFailed(anError.getErrorCode(), anError.getMessage());
                showError(anError);
            }
        });

    }

    /**
     * 通用的获取String数据
     *
     * @param build
     * @param callBack
     */
    private void commonGetString(boolean isMyurl, ANRequest build, final HttpCallBack callBack) {

        build.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                callBack.onSuccess(response);
//                LoadingFragment.dismiss();
            }

            @Override
            public void onError(ANError anError) {
//                LoadingFragment.dismiss();
                showError(anError);
                callBack.onFailed(anError.getErrorCode(), anError.getMessage());

            }
        });
    }

    /**
     * 通用的获取Object数据  引用类型  暂时不用
     *
     * @param build
     * @param callBack
     */
    private void commonGetObject(ANRequest build, final HttpCallBack callBack) {

        build.getAsObject(XHttp.analysisClassInfo(callBack), new ParsedRequestListener() {
            @Override
            public void onResponse(Object response) {
//                try {
//                Object result = response1.getResult();
//                callBack.onSuccess(response);
//                    JSONObject object =new JSONObject(str);
//                    JSONUtils.parseObject(object, XHttp.analysisClassInfo(callBack));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                callBack.onSuccess(response);
            }

            @Override
            public void onError(ANError anError) {
                callBack.onFailed(anError.getErrorCode(), anError.getMessage());
            }
        });
    }


    private void initOkhttp() {
        if (mOkHttpClient == null) {
            if (LogUtil.DEBUG) {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                        .build();
                AndroidNetworking.initialize(BaseApplication.getInstance(), mOkHttpClient);
            } else {
                mOkHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                        .build();
                AndroidNetworking.initialize(BaseApplication.getInstance(), mOkHttpClient);

            }
        }
    }


    /**
     * 网络请求成功 但是code不为200错误
     *
     * @param errorCode
     * @param errorMsg
     */
    private void successError(int errorCode, String errorMsg) {
        if (errorCode == -501) {
        } else if (errorCode == 11014) {

        } else if (errorCode == 500 || errorCode == 502) {
            ToastUtils.showCenter("服务异常,请稍后重试");
        } else {
            ToastUtils.showCenter(errorMsg);
        }
    }

    /**
     * 网络请求失败调用
     *
     * @param anError
     */
    private void showError(ANError anError) {
        String message = anError.getMessage();
        int errorCode = anError.getErrorCode();

        if (!TextUtils.isEmpty(message)) {
            if (message.contains("UnknownHostException") || message.contains("ConnectException")) {
                ToastUtils.showCenter("服务请求超时");
            } else if (message.contains("SocketTimeoutException")) {
                ToastUtils.showCenter("网络请求超时");
            } else if (errorCode == 500 || errorCode == 502) {
                ToastUtils.showCenter("服务异常,请稍后重试");
            } else if (errorCode == 404) {
                ToastUtils.showCenter("找不到地址");
            } else {
                ToastUtils.showCenter("网络错误" + anError.getMessage());
            }
        }
    }


    /**
     * 默认添加的header信息
     *
     * @return
     */
    private String getDefaultHeaderValue() {
        String value = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client", "android");//接入方
            jsonObject.put("version", AppUtils.getAppVersionName());//app版本
            jsonObject.put("os", SystemUtils.getMobileSysVersion());//系统版本
            jsonObject.put("uuid", AppUtils.getUUID());//设备唯一标识
            jsonObject.put("resolution", SystemUtils.getScreenWidth(mContext) + "*" + SystemUtils.getScreenHeight(mContext));//宽高
            jsonObject.put("timestamp", System.currentTimeMillis());//时间戳
            jsonObject.put("channelCode", AppUtils.getAppMetaData(mContext, "UMENG_CHANNEL_VALUE"));//渠道
            /**
             *  NETWORK_WIFI,
             NETWORK_4G,
             NETWORK_3G,
             NETWORK_2G,
             NETWORK_UNKNOWN,
             NETWORK_NO
             */
            jsonObject.put("network", NetworkUtils.getNetworkType());//网络制式
            value = jsonObject.toString();
            LogUtil.d("测试值 : " + value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
}
