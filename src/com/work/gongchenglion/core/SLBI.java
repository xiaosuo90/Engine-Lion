package com.work.gongchenglion.core;

public class SLBI {
	/*
	 * SLBI利用高斯消去法，解方程组， 要求方程组系数矩阵对称正定，且以半带宽方式存放上三角部分于TM（A）数组中，方程右端向量存放于AL(B)中
	 * 求解后，解向量存放在AL(B)中 要声明double D[]=new double [half_band_width]，
	 * 直接用类调用方法，返回B[]=AL[]；
	 */

	public double[] truss_SLBI(double total_stiffness_matrix[][],
			double node_F_Disp[], int N, int half_band_width) {
		/* 参数声明中D[],NRMX,NCMX,貌似没有用到 */
		int K, K1, NI, L, J, K2, I, K3, N1 = 0;
		double C;
		double[] D = new double[N * half_band_width];

		// double[][] total_stiffness_matrix = new double[N][half_band_width+1];

		for (K = 0; K < N - 1; K++) {
			C = total_stiffness_matrix[K][0];
			K1 = K + 1;

			/*
			 * 对A[N][1]是否行奇异进行判断，完了再运行
			 */
			/* 利用对角元消去，对角元即为半带宽存储的首列 */
			NI = K + half_band_width - 1;

			L = NI <= N ? NI : N;
			/* MIN(K1+half_band_width-2,N) */// /////////////

			for (J = 1; J < half_band_width; J++) {
				D[J] = total_stiffness_matrix[K][J];
			}
			for (J = K; J < L; J++) {
				K2 = J - K + 1;
				total_stiffness_matrix[K][K2] = total_stiffness_matrix[K][K2]
						/ C;

				/* 一行中的所有列进行处理，首列元素变为1，其他则除C=total_stiffness_matrix[K][1] */
			}
			/*
			 * 对A[N][1]是否行奇异进行判断，完了再运行
			 */

			node_F_Disp[K] = node_F_Disp[K] / C;

			for (I = K1 ; I < L; I++) {
				K2 = I - K1 + 1;
				C = D[K2];
				
				for (J = I; J < L; J++) {
					K2 = J - I ;
					K3 = J - K ;
					total_stiffness_matrix[I][K2] = total_stiffness_matrix[I][K2]
							- C * total_stiffness_matrix[K][K3];
				}
				node_F_Disp[I] = node_F_Disp[I] - C * node_F_Disp[K];
			}
		}
		/* 矩阵处理完毕，对角元全为1，可以直接回带 */

		node_F_Disp[N-1] = node_F_Disp[N-1] / total_stiffness_matrix[N-1][0];
		/* 回带计算 */
		for (I = 0; I < N ; I++) {
			K = N - I;
			K1 = K + 1;
			NI = K1 + half_band_width - 2;
			if (NI <= N) {
				L = N1;
			} else {
				L = N;
			}
			for (J = K1; J < L; J++) {
				K2 = J - K + 1;
				node_F_Disp[K] = node_F_Disp[K] - total_stiffness_matrix[K][K2]
						/ node_F_Disp[J];
			}
		}

		return node_F_Disp;
	}
}
