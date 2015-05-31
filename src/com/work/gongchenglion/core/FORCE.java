package com.work.gongchenglion.core;

public class FORCE {
	/*
	 * FORCE()计算轴向力，通过之前求出的结点位移AL，存放在FORCE,REAC(支反力)
	 */
	int I, L, N1, N2, K1, K2;
	double D, CO, SI, COEF;
	int sum_node, nm_of_element, nm_of_load_node, nm_of_support_node,
			nm_nodeDOF = 2, nm_element_node = 2, nm_element_DOF = 4, N;
	double E = 200000000.;

	double[] element_FORCE ;
	double[] X = new double[N];
	double[] Y = new double[N];
	double[] PROP;
	double[] node_F_Disp = new double[N];
	double[] node_FORCE ;
	int[] node_ID = new int[N];

	public FORCE(int node_ID[], double PROP[], double X[], double Y[],
			double node_Disp[]) {
		this.X = X;
		this.Y = Y;
		this.node_ID = node_ID;
		this.PROP = PROP;
		this.node_F_Disp = node_Disp;
		sum_node = Core.sum_node;
		nm_of_element = Core.nm_of_element;
		nm_of_support_node = Core.nm_of_support_node;
		N = X.length*nm_element_node;
		PROP = new double[nm_of_element];
		element_FORCE = new double[nm_of_element];
		node_F_Disp=node_Disp;
		node_FORCE = new double[N];
	}

	public double[] getElement_FORCE() {
		return element_FORCE;
	}

	public double[] getNode_FORCE() {
		return node_FORCE;
	}

	public double[] truss_FORCE() {
		int NEL = 0;
		while (NEL < nm_of_element) {
			/* 对每个单元循环 */
			L = nm_element_node * NEL;
			N1 = node_ID[L] - 1; /* 杆单元结点编码 */
			N2 = node_ID[L + 1] - 1;
			K1 = nm_nodeDOF * N1; /*
								 * K1,K2分别为杆单元前后结点的编号，在REAC中存储时，每个单元占四个单位
								 * ，包含每个节点的两个分量的结点力
								 */
			K2 = nm_nodeDOF * N2;
			D = StrictMath.sqrt(StrictMath.pow(X[N2] - X[N1], 2)
					+ StrictMath.pow(Y[N2] - Y[N1], 2));
			CO = (X[N2] - X[N1]) / D;
			SI = (Y[N2] - Y[N1]) / D;
			COEF = E * PROP[NEL] / D;
			/*
			 * FORCE存放轴力，REAC存放结点合力，对于受节点荷载的，合力为节点荷载，不受的为零 对于支座结点，结点合力即为支反力
			 */
			/*
			 * AL本来存储结点荷载，经过函数SLBI后，存放结点位移，F=E*AL 可得到轴力
			 */
			element_FORCE[NEL] = COEF
					* ((node_F_Disp[K2] - node_F_Disp[K1]) * CO + (node_F_Disp[K2 + 1] - node_F_Disp[K1 + 1])
							* SI);

			element_FORCE[NEL] = COEF
					* ((node_F_Disp[K2] - node_F_Disp[K1]) * CO + (node_F_Disp[K2 + 1] - node_F_Disp[K1 + 1])
							* SI);
			node_FORCE[K1] = node_FORCE[K1] - element_FORCE[NEL] * CO;
			node_FORCE[K1 + 1] = node_FORCE[K1 + 1] - element_FORCE[NEL] * SI;
			node_FORCE[K2] = node_FORCE[K2] - element_FORCE[NEL] * CO;
			node_FORCE[K2 + 1] = node_FORCE[K2 + 1] - element_FORCE[NEL] * SI;
			NEL++;
		}

		return node_FORCE;
	}

}
