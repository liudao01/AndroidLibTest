package android.androidlib.net;

/**
 * @author liuml
 * @explain http回调
 * @time 2017/12/16 19:31
 */


public interface  BaseHttpCallBack<Result> {

    void progress(int progress);
    void onSuccess(Result result);
    void onFailed(int errorCode, String error);

}