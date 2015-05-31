package com.work.gongchenglion.core;

public class BOUND {

	int sum_node, nm_of_element, nm_of_load_node, nm_node_DOF = 2,
			nm_element_node = 2, nm_element_DOF = 4;
	int nm_of_support_node;// 要从core获取
	int N = sum_node * 2;
	int half_band_width;
	final double E = 300000.;
	//double[] node_F_Disp = new double[3 * sum_node];
	double[][] total_stiffness_matrix = new double[N + 1][half_band_width + 1]; // 从Assemble中get
	double[] node_F_Disp = new double[sum_node * 2];
	int[] retrain_matrix;
	double[] X = new double[sum_node];
	double[] Y = new double[sum_node];
	double[] PROP = new double[nm_of_element];
	double[] element_FORCE = new double[nm_of_element];
	int[] node_ID = new int[sum_node * 2];

	public BOUND(double[] X, double[] Y, double[] PROP, int[] node_ID,
			double[] node_F_Disp,int[] retrain_matrix) {
		super();
		this.X = X;
		this.Y = Y;
		this.PROP = PROP;
		this.node_ID = node_ID;
		this.node_F_Disp = node_F_Disp;
		sum_node = Core.sum_node;
		nm_of_element = Core.nm_of_element;
		nm_of_support_node = Core.nm_of_support_node;
		this.retrain_matrix = retrain_matrix;
	}

	public void truss_BOUND() {
		Assemble assemble = new Assemble(X, Y, PROP, node_ID);
		total_stiffness_matrix = assemble.truss_assemble();

		half_band_width = assemble.getHalfBandWidth();

		int L, L1, NO, L2, K1, I, J, total_Hang, KV;
		for (L = 0; L < nm_of_support_node; L++) {
			L1 = (nm_node_DOF + 1) * L;
			NO = retrain_matrix[L1];
			K1 = nm_node_DOF * (NO - 1);

			for (I = 0; I < nm_node_DOF; I++) {
				L2 = L1 + I;

				if (retrain_matrix[L2] == 0) { /* 0表示受到约束，并进行相应的TM数组的处理 */
					total_Hang = K1 + I;// ////////////

					for (J = 0; J < half_band_width; J++) {
						KV = total_Hang + J - 1;

						if ((N - KV) >= 0) { /* N<KV超过最后一行，不再处理,N=NDF*NN(结点总数) */
							node_F_Disp[KV] = node_F_Disp[KV]
									- total_stiffness_matrix[total_Hang][J]
									* node_F_Disp[total_Hang];
							total_stiffness_matrix[total_Hang][J] = 0.0;
						}

						KV = total_Hang - J; /* 列处理，向右上移动 */

						if (KV > 0) { /* KV<0超过最后一列 */
							node_F_Disp[KV] = node_F_Disp[KV]
									- total_stiffness_matrix[KV][J]
									* node_F_Disp[total_Hang];
							total_stiffness_matrix[KV][J] = 0.0;
						}
					}

					total_stiffness_matrix[total_Hang][0] = 1.0;
					node_F_Disp[total_Hang] = node_F_Disp[total_Hang];

				}
			}
		}
		return;
	}

	public double[] getNode_F_Disp() {
		return node_F_Disp;
	}

	public double[][] getTotal_stiffness_matrix() {
		return total_stiffness_matrix;
	}

	public int getHalf_band_width() {
		return half_band_width;
	}

}
