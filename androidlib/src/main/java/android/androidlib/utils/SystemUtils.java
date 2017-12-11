package android.androidlib.utils;

import android.androidlib.ui.view.alertview.AlertView;
import android.androidlib.ui.view.alertview.OnAlertItemClickListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统相关
 */
public class SystemUtils {


    /**
     * 发送短信
     *
     * @param context
     * @param number
     * @param content
     */
    public static void sendSmsWithAskBuy(Context context, String number, String content) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", content);
        context.startActivity(sendIntent);
    }

    public static void sendSmsWithAskBuy(Context context, String number, int stringResId) {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        sendIntent.putExtra("sms_body", context.getResources().getString(stringResId));
        context.startActivity(sendIntent);
    }

    /**
     * 到拨号盘
     *
     * @param context
     * @param phoneNumber
     */
    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    public static void phoneNumberAlertDef(final Context context, final String phoneNumber) {
        AlertView mAlertView = new AlertView("拨打电话", "客服电话: " + phoneNumber, "取消", new String[]{"确认"},
                null, context, AlertView.Style.Alert, new OnAlertItemClickListener() {

            @Override
            public void onAlertItemClick(Object o, int position) {
                if (position == 0) {

                    phoneNumberDef(context, phoneNumber);
                }
            }
        }).setCancelable(true);
        mAlertView.show();

    }

    /**
     * 直接拨打 默认的不请求网络
     *
     * @param context
     * @param phoneNumber
     */
    public static void phoneNumberDef(Context context, String phoneNumber) {
        String userid = String.valueOf(sharedUtils.getUserId());
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    public interface getAllCall {
        void success(String tell, String isDirectSeed);
    }


    /**
     * 弹出键盘
     *
     * @param phoneEdt
     */
    public static void showSoftMode(final EditText phoneEdt) {
        phoneEdt.setFocusable(true);
        phoneEdt.setFocusableInTouchMode(true);
        phoneEdt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               InputMethodManager inputManager = (InputMethodManager) phoneEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(phoneEdt, 0);
                           }
                       },
                500);
    }

    /**
     * 收起键盘
     *
     * @param phoneEdt
     */
    public static void closeSoftMode(final EditText phoneEdt) {
        InputMethodManager imm = (InputMethodManager) phoneEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(phoneEdt.getWindowToken(), 0);
    }


    public static int getScreen(Activity con) {
        WindowManager wm = con.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        return width;
    }


    public static String getTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date = sDateFormat.format(new java.util.Date());

        return date;

    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {

        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;

    }


    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    //安装apk
    public static void installApk(File file, Context content) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        content.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    /**
     * 从本地相册获取
     */
    public static void takePhotosFromLocal(Fragment context) {

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
        }


        context.startActivityForResult(intent, 10);
    }

    /**
     * 从本地相册获取
     */
    public static void takePhotosFromLocal(Activity context) {

        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
        }
        context.startActivityForResult(intent, 10);


    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }


    /**
     * 从缓存获取可以进入卖车tab的城市
     *
     * @return
     */
    public static List<String> getConsignedCity() {
        List<String> citys = new ArrayList<String>();
        String cache = sharedUtils.getCache();
        if (!TextUtils.isEmpty(cache)) {
            try {
                JSONObject jsonObject = new JSONObject(cache);
                JSONArray jsonArraySix = jsonObject.optJSONArray("citykeys");//车型6
                for (int i = 0; i < jsonArraySix.length(); i++) {
                    String cityName = jsonArraySix.optJSONObject(i).optString("name");
                    citys.add(cityName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return citys;
    }

    public static boolean isStartGps(Context context) {

        LocationManager locationManager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    public static boolean startActivityIsExist(Context context) {
      /*  Intent intent = new Intent();
        intent.setClassName("com.guangan.woniu", "MainPageActivity");
        if(intent.resolveActivity(con.getPackageManager()) == null) {
            // 说明系统中不存在这个activity
            return false;
        }else{
            return true;
        }
*/
        //判断应用是否在运行
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        String MY_PKG_NAME = "com.guangan.woniu";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;

    }

    public static void customTitle(Activity welcomeActivity) {

        welcomeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //   welcomeActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }


/*    */

    /**
     * 自动填写验证码
     *//*
    public static String getSmsFromPhone(long sendtime,Context context) {
        String[] projection = new String[]{"date", "body"};
        String where = " date >  " + (sendtime - 60 * 1000);//60s以内的短信
        //先记录发送命令的时间，找这时间以后收到的短信  ，按时间降序排序
        Cursor cur = context.getContentResolver().query(Uri.parse("content://sms/"), projection, where, null, "date desc");

        if (null == cur) {

            return "";
        }
        // 1458371840372   1458365678000
        //只要一条，使用if,如果是读取所有的短信，使用while
        if (cur.moveToNext()) {
            String date = cur.getString(cur.getColumnIndex("date"));
            String body = cur.getString(cur.getColumnIndex("body"));

            //这里我是要获取短信中的验证码
            Pattern pattern = Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group(0).substring(0, 4);
                ToastUtils.showCenter(res);
                return res;
            }
        }
        return "";
    }*/
    public static String onSmsReceive(Context context, Intent intent) {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            byte[] pdu = (byte[]) obj;
            SmsMessage sms = SmsMessage.createFromPdu(pdu);
            String message = sms.getMessageBody();

            // 短息的手机号。。+86开头？
            String from = sms.getOriginatingAddress();

            if (!TextUtils.isEmpty(from)) {
                //这里我是要获取短信中的验证码
                Pattern pattern = Pattern.compile("(?<![0-9])([0-9]{" + 4 + "})(?![0-9])");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    String res = matcher.group(0).substring(0, 4);
                    ToastUtils.showCenter(res);
                    return res;
                }
            }
        }

        return "";
    }


    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileModel() {
        return android.os.Build.MODEL;
    }

    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getMobileSysVersion() {
        return Build.VERSION.RELEASE;
    }

}