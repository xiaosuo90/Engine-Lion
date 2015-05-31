package com.work.gongchenglion.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.work.gongchenglion.R;
import com.work.gongchenglion.app.AppContext;
import com.work.gongchenglion.app.BaseActivity;
import com.work.gongchenglion.core.Core;
import com.work.gongchenglion.util.TitlebarThreeLineView;

public class MainActivity extends BaseActivity {

	/*
	 * 点界面的成员变量，被节点编号唯一确定 节点编号，在Activity界面辅助生成数组 点的X，y坐标值 受力的XY的坐标值，在一个数组里 约束位移
	 * 约束的数组三个值为一组，第一个是节点编号，第二三个是是否XY约束。
	 */
	double[] node_F_Disp, X, Y, BoundDisp_NodeForce;
	int[] retrain_matrix;// 这里默认是无约束，2.0后进行修改
	/*
	 * 杆界面的成员变量 左右节点编号 杆单元的截面积
	 */
	int[] node_ID, node_ID_in, pole_ID_in;
	double[] E;
	//
	private AppContext app;
	private ViewPager mViewPager;
	private PagerAdapter mAdapter;
	private List<View> mViews = new ArrayList<View>();

	private LinearLayout mTabInit, mTabPoint, mTabBar;

	private ImageView mInitImg, mPointImg, mBarImg;

	private TitlebarThreeLineView buttonView;

	private DrawerLayout mDrawerLayout;

	private RelativeLayout projectLine;

	private EditText edit_node_sum, edit_pole_sum, edit_support_sum, et_nodeid,
			et_x, et_y, et_f_x, et_f_y, et_id_pole, et_node_left,
			et_node_right, et_prop, et_retrain_x, et_retrain_y;
	int mysum_node, mynm_of_support_node, mynm_of_element;

	View tabNew, tabPoint, tabBar;

	Button btn_sure_tabnew, btn_sure_tabpoint, btn_cul_tabpoint,
			btn_sure_tabbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (AppContext) getApplication();
		fullscreen();// 全屏显示
		setContentView(R.layout.activity_main);
		initView();// 初始化Activity
		initEvents();// 初始化事件
	}

	private void initView() {

		// lines
		projectLine = (RelativeLayout) findViewById(R.id.line_01);
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		// tabs
		mTabInit = (LinearLayout) findViewById(R.id.id_tab_init);
		mTabPoint = (LinearLayout) findViewById(R.id.id_tab_point);
		mTabBar = (LinearLayout) findViewById(R.id.id_tab_bar);
		// ImageButton
		mInitImg = (ImageView) findViewById(R.id.tab_init_img);
		mPointImg = (ImageView) findViewById(R.id.tab_point_img);
		mBarImg = (ImageView) findViewById(R.id.tab_bar_img);
		// 加载内容部分
		loadTab();

		buttonView = (TitlebarThreeLineView) findViewById(R.id.titlebar_menu);// TitlebarThreeLineView
		// DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	}

	/**
	 * 辅助initView的封装方法
	 * 
	 */
	// 加载内容的方法
	private void loadTab() {
		LayoutInflater mInflater = LayoutInflater.from(this);

		tabNew = mInflater.inflate(R.layout.tab_new,
				(ViewGroup) this.findViewById(R.id.id_viewpager), false);
		tabPoint = mInflater.inflate(R.layout.tab_point,
				(ViewGroup) this.findViewById(R.id.id_viewpager), false);
		tabBar = mInflater.inflate(R.layout.tab_bar,
				(ViewGroup) this.findViewById(R.id.id_viewpager), false);

		mViews.add(tabNew);
		mViews.add(tabPoint);
		mViews.add(tabBar);

		//
		btn_sure_tabnew = (Button) tabNew.findViewById(R.id.sure_btn_tabnew);
		btn_sure_tabpoint = (Button) tabPoint
				.findViewById(R.id.sure_btn_tabpoint);
		btn_cul_tabpoint = (Button) tabBar
				.findViewById(R.id.calculator_btn_tabpoint);
		btn_sure_tabbar = (Button) tabBar.findViewById(R.id.sure_btn_tabbar);
	}

	private void initEvents() {

		// 设置监听事件
		MyOnClickListener mListener = new MyOnClickListener();
		mTabInit.setOnClickListener(mListener);
		mTabPoint.setOnClickListener(mListener);
		mTabBar.setOnClickListener(mListener);

		btn_sure_tabnew.setOnClickListener(mListener);
		btn_sure_tabpoint.setOnClickListener(mListener);
		btn_cul_tabpoint.setOnClickListener(mListener);
		btn_sure_tabbar.setOnClickListener(mListener);
		// 三行按钮设置监听
		buttonView.setOnClickListener(mListener);
		// 抽屉侧滑栏的按钮设置监听
		projectLine.setOnClickListener(mListener);

		// ViewPager组件设置监听
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 重写ViewPager的适配器
		mAdapter = new PagerAdapter() {

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mViews.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = mViews.get(position);
				container.addView(view);
				return view;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mViews.size();
			}
		};

		mViewPager.setAdapter(mAdapter);

	}

	// 将ImageButton回复成未选中状态
	private void resetImg() {
		mInitImg.setImageResource(R.drawable.initialization_white);
		mPointImg.setImageResource(R.drawable.coordinate_icon);
		mBarImg.setImageResource(R.drawable.pole_icon_white);

	}

	// 得到三个初始化编号的值
	private void SetValuesAtNewTab(View newTab) {

		edit_node_sum = (EditText) newTab.findViewById(R.id.edit_node_sum);
		edit_pole_sum = (EditText) newTab.findViewById(R.id.edit_pole_sum);
		edit_support_sum = (EditText) newTab
				.findViewById(R.id.edit_support_sum);

		mysum_node = Integer.parseInt(edit_node_sum.getText().toString());
		mynm_of_element = Integer.parseInt(edit_pole_sum.getText().toString());
		mynm_of_support_node = Integer.parseInt(edit_support_sum.getText()
				.toString());

		X = new double[mysum_node];
		Y = new double[mysum_node];
		node_F_Disp = new double[2 * mysum_node];
		node_ID_in = new int[mysum_node];
		pole_ID_in = new int[mynm_of_element];

		node_ID = new int[2 * mynm_of_element];
		E = new double[mynm_of_element];
		retrain_matrix = new int[3 * mysum_node];
		app.setSum_node(mysum_node);
		app.setNm_of_element(mynm_of_element);
		app.setNm_of_support_node(mynm_of_support_node);
		Toast.makeText(this, "数据已保存", Toast.LENGTH_SHORT).show();

	}

	private void SetValuesAtPointTab(View tabPoint) {

		retrain_matrix = new int[3 * mysum_node];
		et_nodeid = (EditText) tabPoint.findViewById(R.id.et_nodeid);
		et_x = (EditText) tabPoint.findViewById(R.id.et_x_tabpoint);
		et_y = (EditText) tabPoint.findViewById(R.id.et_y_tabpoint);
		et_f_x = (EditText) tabPoint.findViewById(R.id.et_f_x);
		et_f_y = (EditText) tabPoint.findViewById(R.id.et_f_y);
		et_retrain_x = (EditText) tabPoint.findViewById(R.id.et_retrain_x);
		et_retrain_y = (EditText) tabPoint.findViewById(R.id.et_retrain_y);

		int node_id = Integer.parseInt(et_nodeid.getText().toString());
		// 判断数据是否重复和越值
		for (int i : node_ID_in) {
			if (node_id <= mysum_node) {
				if (i != node_id) {
					Toast.makeText(this, "新的节点", Toast.LENGTH_SHORT).show();
				} else {
					node_id = i;
					Toast.makeText(this, "更改节点" + i + "的数据", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "节点不存在", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		node_ID_in[node_id - 1] = node_id;
		X[node_id - 1] = Double.parseDouble(et_x.getText().toString());
		Y[node_id - 1] = Double.parseDouble(et_y.getText().toString());
		node_F_Disp[2 * node_id - 2] = Double.parseDouble(et_f_x.getText()
				.toString());
		node_F_Disp[2 * node_id - 1] = Double.parseDouble(et_f_y.getText()
				.toString());
		retrain_matrix[3 * node_id - 3] = node_id;
		retrain_matrix[3 * node_id - 2] = Integer.parseInt(et_retrain_x
				.getText().toString());
		retrain_matrix[3 * node_id - 1] = Integer.parseInt(et_retrain_y
				.getText().toString());

		// 传值
		app.setX(X);
		app.setY(Y);
		app.setNode_F_Disp(node_F_Disp);
		app.setRetrain_matrix(retrain_matrix);
		app.setBoundDisp_NodeForce(BoundDisp_NodeForce);
		Toast.makeText(this, "数据已保存，还需输入" + (mysum_node - node_id) + "次节点数据",
				Toast.LENGTH_SHORT).show();

	}

	private void SetValuesAtBarTab(View tabBar) {

		et_id_pole = (EditText) tabBar.findViewById(R.id.et_id_pole);
		et_node_left = (EditText) tabBar.findViewById(R.id.et_node_left);
		et_node_right = (EditText) tabBar.findViewById(R.id.et_node_right);
		et_prop = (EditText) tabBar.findViewById(R.id.et_prop);
		int id_pole = Integer.parseInt(et_id_pole.getText().toString());

		// 判断数据是否重复和越值
		for (int i : pole_ID_in) {
			if (id_pole <= mynm_of_element) {
				if (i != id_pole) {
					Toast.makeText(this, "新的节点", Toast.LENGTH_SHORT).show();
				} else {
					id_pole = i;
					Toast.makeText(this, "更改节点" + i + "的数据", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(this, "节点不存在", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		pole_ID_in[id_pole - 1] = id_pole;

		node_ID[2 * id_pole - 2] = Integer.parseInt(et_node_left.getText()
				.toString());
		node_ID[2 * id_pole - 1] = Integer.parseInt(et_node_right.getText()
				.toString());
		E[id_pole - 1] = Double.parseDouble(et_prop.getText().toString());

		app.setNode_ID(node_ID);
		app.setE(E);
		Toast.makeText(this,
				"数据已保存，还需输入" + (mynm_of_element - id_pole) + "次杆数据",
				Toast.LENGTH_SHORT).show();

	}

	// 清空EditText里的内容，重置按钮时调用
	private void clearEtAtNewTab() {

		edit_node_sum.setText("");
		edit_pole_sum.setText("");
		edit_support_sum.setText("");

	}

	private void clearEtAtPointTab() {

		et_nodeid.setText("");
		et_x.setText("");
		et_y.setText("");
		et_f_x.setText("");
		et_f_y.setText("");
		et_retrain_x.setText("");
		et_retrain_y.setText("");

	}

	private void clearEtAtBarTab() {

		et_id_pole.setText("");
		et_node_left.setText("");
		et_node_right.setText("");
		et_prop.setText("");

	}

	/*
	 * 下方切换栏的监听器、 侧滑栏上按钮的监听、 确定和计算按钮的监听器
	 */
	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_tab_init:
				resetImg();
				mViewPager.setCurrentItem(0);
				mInitImg.setImageResource(R.drawable.initialization_blue);
				break;
			case R.id.id_tab_point:
				resetImg();
				mViewPager.setCurrentItem(1);
				mPointImg.setImageResource(R.drawable.dot_blue);
				break;
			case R.id.id_tab_bar:
				resetImg();
				mViewPager.setCurrentItem(2);
				mBarImg.setImageResource(R.drawable.pole_icon_blue);
				break;
			case R.id.line_01:
				mDrawerLayout.closeDrawer(Gravity.LEFT);
				Intent intent_drawer = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent_drawer);
				break;
			case R.id.sure_btn_tabnew:
				SetValuesAtNewTab(tabNew);
				clearEtAtNewTab();
				break;
			case R.id.sure_btn_tabpoint:
				SetValuesAtPointTab(tabPoint);
				clearEtAtPointTab();
				break;
			case R.id.calculator_btn_tabpoint:
				Core core = new Core();
				ContentValues resultValues = core.run();
				app.setCoreValues(resultValues);
				Intent intent_result = new Intent(MainActivity.this,
						ResultActivity.class);
				MainActivity.this.startActivity(intent_result);
				break;
			case R.id.sure_btn_tabbar:
				SetValuesAtBarTab(tabBar);
				clearEtAtBarTab();
				break;
			case R.id.titlebar_menu:
				if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
					mDrawerLayout.openDrawer(Gravity.LEFT);
				} else {
					mDrawerLayout.closeDrawer(Gravity.LEFT);
				}
				break;
			default:
				break;
			}
		}
	}

	/*
	 * ViewPager的监听器
	 */
	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			int currentItem = mViewPager.getCurrentItem();
			resetImg();
			switch (currentItem) {
			case 0:
				mInitImg.setImageResource(R.drawable.initialization_blue);
				break;
			case 1:
				mPointImg.setImageResource(R.drawable.dot_blue);
				break;
			case 2:
				mBarImg.setImageResource(R.drawable.pole_icon_blue);
				break;

			}

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
}
