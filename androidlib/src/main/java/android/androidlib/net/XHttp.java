package android.androidlib.net;


import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class XHttp implements HttpEngine {

    private static HttpEngine httpEngine;
    private static XHttp xHttp;
//    public static Handler handler = new Handler();

    public static void init(HttpEngine engine) {
        httpEngine = engine;
    }

    public static XHttp getInstance() {
        if (httpEngine == null) {
            throw new NullPointerException("Call XFrame.initXHttp(IHttpEngine httpEngine) within your Application onCreate() method." +
                    "Or extends XApplication");
        }
        if (xHttp == null) {
            xHttp = new XHttp();
        }
        return xHttp;
    }

    /**
     * 获取实体类的类型
     *
     * @param obj
     * @return
     */
    public static Class<?> analysisClassInfo(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void get(Context context, String url, Map<String, String> params, HttpCallBack callBack) {
        httpEngine.get(context, url, params, callBack);
    }

    @Override
    public void post(Context context, String url, Map<String, String> params, HttpCallBack callBack) {
        httpEngine.post(context, url, params, callBack);
    }

    @Override
    public void download(Context context, String url, Map<String, String> params, HttpCallBack callBack) {

    }

//    public void post(String url, Map<String, String> params, HttpCallBack callBack) {
//        httpEngine.post(url,params,callBack);
//    }
}
