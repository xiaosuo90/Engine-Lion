package com.work.gongchenglion.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


/**
 * 屏幕分辨率等信息处理
 * <p/>
 * Created by apple on 14-8-20.
 */
public class ScreenHelper {

    /**
     * dp转换为px
     *
     * @param context 上下文
     * @param dp      dip
     * @return px
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 截屏
     *
     * @param v 视图对象
     * @return 图片
     */
    public synchronized static Bitmap screenShot(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        try {
            v.draw(canvas);
        } catch (Exception e) {
            if (bitmap != null && !bitmap.isRecycled())
                bitmap.recycle();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 关闭输入法
     */
    public static void closeInputMethod(View view) {
        try {
            if (((Activity) view.getContext()).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(((ViewGroup) view).getFocusedChild().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception ignored) {
        }
    }

}