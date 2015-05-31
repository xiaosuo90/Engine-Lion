package com.work.gongchenglion.app;

import com.work.gongchenglion.R;
import com.work.gongchenglion.ui.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 每次的启动界面
 * Created By Wom
 */
public class App_Start extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_start);
		initView();
	}

	private void initView() {
		new Handler(){  
            @Override
			public void handleMessage(android.os.Message msg) {  
              if(msg.what==1){  
                  Intent intent = new Intent(App_Start.this,MainActivity.class);  
                  startActivity(intent);  
                  finish();
              }  
            };  
        }.sendEmptyMessageDelayed(1, 2000); 
	}
	
	

	

}
