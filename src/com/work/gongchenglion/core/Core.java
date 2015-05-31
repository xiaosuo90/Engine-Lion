package com.work.gongchenglion.core;

import com.work.gongchenglion.app.BaseActivity;

import android.content.ContentValues;

public class Core extends BaseActivity {

	ContentValues resultValues;
	/*
	 * 初始化界面的成员变量
	 */
	// 全局变量
	public static int sum_node, nm_of_support_node, nm_of_element;

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

	/*
	 * 功能类的实例对象
	 */
	BOUND bound;

	/*
	 * 输出结果数组
	 */
	// 力偏移的XY一组
	double[] node_Disp;
	// 点力的XY一组
	double[] node_FORCE;
	// 杆力一个X一组
	double[] element_FORCE;

	/*
	 * 类方法
	 */
	public Core() {
		resultValues = new ContentValues();
		getValuesByApp();
	}

	public ContentValues run() {

		bound = new BOUND(X, Y, E, node_ID, node_F_Disp,retrain_matrix);
		bound.truss_BOUND();
		double[][] total_stiffness_matrix = bound.getTotal_stiffness_matrix();
		node_F_Disp = bound.getNode_F_Disp();
		SLBI slbi = new SLBI();
		node_Disp = slbi.truss_SLBI(total_stiffness_matrix, node_F_Disp, 4, 4);// N,half_band_width
		FORCE force = new FORCE(node_ID, E, X, Y, node_Disp);
		node_FORCE = force.truss_FORCE();
		for (int i = 0; i < node_FORCE.length; i++) {
			System.out.println(node_FORCE[i]);
		}
		element_FORCE = force.getElement_FORCE();
		for (int i = 0; i < element_FORCE.length; i++) {
			System.out.println(element_FORCE[i]);
		}

		// 处理和封装结果值
		// 点的结果
		for (int i = 0; i < sum_node; i++) {
			resultValues.put("node_Disp_X" + i + 1, node_Disp[2 * i]);
			resultValues.put("node_Disp_Y" + i + 1, node_Disp[2 * i + 1]);
			resultValues.put("node_FORCE_X" + i + 1, node_FORCE[2 * i]);
			resultValues.put("node_FORCE_Y" + i + 1, node_FORCE[2 * i + 1]);
		}
		// 杆的结果
		for (int i = 0; i < nm_of_element; i++) {
			resultValues.put("element_FORCE" + i + 1, element_FORCE[i]);
		}

		return resultValues;
	}

	/*
	 * 内部私有功能方法
	 */
	// 从app内传值
	private void getValuesByApp() {

		sum_node = fatherContext.getSum_node();
		nm_of_support_node = fatherContext.getNm_of_element();
		nm_of_element = fatherContext.getNm_of_element();
		X = fatherContext.getX();
		Y = fatherContext.getY();
		node_F_Disp = fatherContext.getNode_F_Disp();
		// retrain_matrix = fatherContext.getRetrain_matrix();
		retrain_matrix = new int[] { 1, 0, 0, 2, 0, 0 };
		BoundDisp_NodeForce = fatherContext.getBoundDisp_NodeForce();
		node_ID = fatherContext.getNode_ID();
		E = fatherContext.getE();
	}

}
