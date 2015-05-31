package com.work.gongchenglion.app;

import com.work.gongchenglion.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	// 每个Activity的RootView
	public View view;
	// 重写Application的类，用来存放全局变量的类。
	public static AppContext fatherContext;
	public static boolean running = false;// 是否正在当前activity
	public boolean isLoading = false;// 是否正在进行耗时操作

	private long touchTime = 0;

	/*
	 * Activity的生命周期方法
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fullscreen();
		fatherContext = (AppContext) getApplication();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - touchTime > 1500) {
				Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
				touchTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void activityPushAnim() {
		overridePendingTransition(R.anim.activity_anim_push_in,
				R.anim.activity_anim_push_out);
	}

	public void activityPopAnim() {
		overridePendingTransition(R.anim.activity_anim_pop_in,
				R.anim.activity_anim_pop_out);
	}

	@Override
	protected void onResume() {
		super.onResume();
		running = true;
	}

	@Override
	protected void onPause() {
		super.onStop();
		running = false;
	}

	/*
	 * 功能方法
	 */

	// 关闭输入框
	public void closeInputMethod() {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getBaseContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception ignored) {
		}
	}

	// 设置返回按钮的处理事件
	public void setOnBackClick() {
		// View backView = view.findViewById(R.id.back);
		// if (backView != null)
		// backView.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// back();
		// }
		// });
	}

	// 返回到前一activity，带有动画
	public void back() {
		closeInputMethod();
		finish();
		activityPopAnim();
	}

	// 全屏显示
	public void fullscreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

}
