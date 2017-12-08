package android.androidlib.utils;

import android.androidlib.base.BaseApplication;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


/**
 * @author liuml.
 * @explain 吐司工具
 * @time 2017/12/8 17:12
 */
public class ToastUtils {


    public static void showShort(String str) {
        Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String str) {
        Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_LONG).show();
    }

    /**
     * toast居中显示
     *
     * @param
     */
    public static void showCenter(String str) {
        try {
            Toast toast = Toast.makeText(BaseApplication.getInstance(), str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void customToast(Context context, View view, int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
