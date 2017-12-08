package android.androidlib.utils;

import android.androidlib.base.MyApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

public class sharedUtils {

    private static SharedPreferences mySharedPreferences = MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE);
    private static Editor editor = mySharedPreferences.edit();

    private static final String CHANNELSET = "channelSet";
    public static final String USER_SP = "user";

    /**
     * 存储搜索历史记录
     *
     * @param str
     */
    public static void setHistData(String str) {
        editor.putString("hist", str);
        editor.commit();
    }

    public static String getHistData() {

        return mySharedPreferences.getString("hist", "");

    }

    /**
     * 用户登录成功之后存储用户手机号
     *
     * @param userTell
     */
    public static void setUserTell(String userTell) {
        editor.putString("userTell", userTell);
        editor.commit();
    }

    /**
     * 得到用户登录成功之后存储用户手机号
     */
    public static String getUserTell() {
        return mySharedPreferences.getString("userTell", "");
    }


    /**
     * 用户登录成功之后存储用户手机号
     *
     * @param userTell
     */
    public static void setloginName(String userTell) {
        editor.putString("loginName", userTell);
        editor.commit();
    }

    /**
     * 得到用户登录成功之后存储用户手机号
     */
    public static String getloginName() {
        return mySharedPreferences.getString("loginName", "");
    }


    /**
     * 清除用户登录成功之后存储的手机号
     *
     * @return
     */
    public static void clearUserLoginName() {
        editor.remove("loginName");
        editor.commit();
    }


    /**
     * 清除存储的手机号
     *
     * @return
     */
    public static void clearUserTell() {
        editor.remove("userTell");
        editor.commit();
    }

    /**
     * 存储登录状态
     */
    public static void setIsLogin(boolean islogin) {
        editor.putBoolean("isLogin", islogin);
        editor.commit();
    }

    /**
     * 获取登录状态
     */

    public static Boolean getIsLogin() {
        return mySharedPreferences.getBoolean("isLogin", false);
    }

    /**
     * 存储是否需要新手引导
     */
    public static void setGuiDeui(boolean islogin) {
        editor.putBoolean("GuiDeui", islogin);
        editor.commit();
    }

    /**
     * 获取是否需要新手引导
     */
    public static Boolean getGuiDeui() {
        return mySharedPreferences.getBoolean("GuiDeui", true);
    }


    /**
     * 清除用户登录状态
     */
    public static void clearUserIsLogin() {
        editor.remove("isLogin");
        editor.commit();
    }


    /**
     * 缓存车列表数据
     *
     * @param cache
     */
    public static void setCache(String cache) {
        editor.putString("cache", cache);
        editor.commit();
    }

    /**
     * 得到缓存数据
     */
    public static String getCache() {
        return mySharedPreferences.getString("cache", "");
    }


    /**
     * 存储用户Id
     */
    public static void setUserId(int id) {
        editor.putInt("userId", id);
        editor.commit();
    }

    /**
     * 清除用户ID
     */
    public static void clearUserId() {
        editor.remove("userId");
        editor.commit();
    }

    /**
     * 存储是否需要下载欢迎界面的图片
     */
    public static void setBitmapId(int id) {
        editor.putInt("bitmapid", id);
        editor.commit();
    }

    /**
     * 获取用户Id
     */
    public static int getUserId() {
        return mySharedPreferences.getInt("userId", 0);
    }

    /**
     * 获取是否需要下载欢迎界面的图片
     */
    public static int getBitmapId() {
        return mySharedPreferences.getInt("bitmapid", 0);
    }


    /**
     * 存储是否需要下载欢迎界面的图片
     */
    public static void setIsfull(int id) {
        editor.putInt("isfull", id);
        editor.commit();
    }

    /**
     * 获取是否需要下载欢迎界面的图片
     */
    public static int getIsfull() {
        return mySharedPreferences.getInt("isfull", 0);
    }


    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public static Long getLong(String key) {
        return mySharedPreferences.getLong(key, 0);
    }

    public static String getString(String key) {
        return mySharedPreferences.getString(key, "");
    }

    /**
     * 储存经度
     *
     * @param
     */
    public static void setLongitude(float Longitude) {
        editor.putFloat("Longitude", Longitude);
        editor.commit();
    }

    public static float getLongitude() {
        return mySharedPreferences.getFloat("Longitude", 0f);
    }


    /**
     * 储存纬度
     *
     * @param
     */
    public static void setLatitude(float Latitude) {
        editor.putFloat("Latitude", Latitude);
        editor.commit();
    }


    public static void setSzImei(String sz) {
        editor.putString("sz", sz);
        editor.commit();
    }

    public static String getSzImei() {
        return mySharedPreferences.getString("sz", "");
    }


    public static void addSet(String string) {


        HashSet set = getSet();
        HashSet channelSet;
        if (null != set) {
            channelSet = new HashSet<String>(set);
        } else {
            channelSet = new HashSet<String>();
        }
        channelSet.add(string);
        editor.putStringSet(CHANNELSET, channelSet);
        editor.commit();
    }

    public static HashSet getSet() {
        Set<String> stringSet = mySharedPreferences.getStringSet(CHANNELSET, null);
        return (HashSet) stringSet;
    }
}
