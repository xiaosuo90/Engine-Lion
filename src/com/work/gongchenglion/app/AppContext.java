package com.work.gongchenglion.app;

import android.app.Application;
import android.content.ContentValues;
import android.util.DisplayMetrics;

public class AppContext extends Application {

	private AppConfig config;
	private int screenWidth;
	private int screenHeight;

	// 三个编号变量
	int sum_node, nm_of_support_node, nm_of_element;

	/*
	 * 点界面的成员变量，被节点编号唯一确定
	 */
	// 节点编号，在Activity界面辅助生成数组
	// 点的X，y坐标值
	double[] X;
	double[] Y;
	// 受力的XY的坐标值，在一个数组里
	double[] node_F_Disp;
	// 约束的数组三个值为一组，第一个是节点编号，第二三个是是否XY约束。
	int[] retrain_matrix;
	// 约束位移
	double[] BoundDisp_NodeForce;

	/*
	 * 杆界面的成员变量
	 */
	// 左右节点编号
	int[] node_ID;
	// 杆单元的截面积
	double[] E;

	ContentValues coreValues;

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化应用配置对象
		config = AppConfig.init(this);
		// 初始化屏幕长宽度
		getScreenSize();
		// 得到三个初始化编号的值

	}

	/*
	 * @ 重置按钮或者返回按钮时调用
	 */
	public void stop() {
		node_ID = null;
		X = null;
		Y = null;
		node_F_Disp = null;
		retrain_matrix = null;
		E = null;
		BoundDisp_NodeForce = null;
		coreValues = null;
	}

	private void getScreenSize() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
	}

	/**
	 * 获取屏幕的宽度
	 *
	 * @return screenWidth
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 获取屏幕的高度
	 *
	 * @return screenHeight
	 */
	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * 获取配置对象
	 *
	 * @return config
	 */
	public AppConfig getConfig() {
		return config;
	}

	public int getSum_node() {
		return sum_node;
	}

	public void setSum_node(int sum_node) {
		this.sum_node = sum_node;
	}

	public int getNm_of_support_node() {
		return nm_of_support_node;
	}

	public void setNm_of_support_node(int nm_of_support_node) {
		this.nm_of_support_node = nm_of_support_node;
	}

	public int getNm_of_element() {
		return nm_of_element;
	}

	public void setNm_of_element(int nm_of_element) {
		this.nm_of_element = nm_of_element;
	}

	public double[] getX() {
		return X;
	}

	public void setX(double[] x) {
		X = x;
	}

	public double[] getY() {
		return Y;
	}

	public void setY(double[] y) {
		Y = y;
	}

	public double[] getNode_F_Disp() {
		return node_F_Disp;
	}

	public void setNode_F_Disp(double[] node_F_Disp) {
		this.node_F_Disp = node_F_Disp;
	}

	public int[] getRetrain_matrix() {
		return retrain_matrix;
	}

	public void setRetrain_matrix(int[] retrain_matrix) {
		this.retrain_matrix = retrain_matrix;
	}

	public double[] getBoundDisp_NodeForce() {
		return BoundDisp_NodeForce;
	}

	public void setBoundDisp_NodeForce(double[] boundDisp_NodeForce) {
		BoundDisp_NodeForce = boundDisp_NodeForce;
	}

	public int[] getNode_ID() {
		return node_ID;
	}

	public void setNode_ID(int[] node_ID) {
		this.node_ID = node_ID;
	}

	public double[] getE() {
		return E;
	}

	public void setE(double[] e) {
		E = e;
	}

	public ContentValues getCoreValues() {
		return coreValues;
	}

	public void setCoreValues(ContentValues coreValues) {
		this.coreValues = coreValues;
	}

}