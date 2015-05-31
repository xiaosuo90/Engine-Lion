package com.work.gongchenglion.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户配置信息
 */
public class AppConfig {

    public static final String USED = "used";//客户端已经使用中
    private SharedPreferences preferences;

    private AppConfig(Context context) {
        preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    /**
     * 初始化一个对象
     *
     * @param context 上下文
     * @return 一个新的AppConfig对象
     */
    public static AppConfig init(Context context) {
        return new AppConfig(context);
    }

    private void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    private boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    private void putInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    private void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

//    public void saveUID(String uid) {
//        putString(ApiNode.UID, uid);
//    }
//
//    public String getUID() {
//        return getString(ApiNode.UID, "");
//    }
//
//    public void saveResultPassword(String password) {
//        putString(ApiNode.PASSWORD, password);
//    }
//
//    public String getPassword() {
//        return getString(ApiNode.PASSWORD, "");
//    }
//
//    public void saveEmail(String email) {
//        putString(ApiNode.EMAIL, email);
//    }
//
//    public String getEmail() {
//        return getString(ApiNode.EMAIL, "");
//    }

    public void saveLastRefreshTime(String key, String time) {
        putString(key, time);
    }

    public String getLastRefreshTime(String key) {
        return getString(key, "");
    }


    /**
     * 设置客户端已经使用
     */
    public void used() {
        putBoolean(USED, true);
    }

    /**
     * 查看客户端是否被使用
     *
     * @return 是否被使用
     */
    public boolean isUsed() {
        return getBoolean(USED, false);
    }

    /**
     * 移除某个配置
     *
     * @param key key
     */
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }
}