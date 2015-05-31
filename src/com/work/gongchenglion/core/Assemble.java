package com.work.gongchenglion.core;

//类Assemble构造方法参数为，结点坐标数组X[],Y[],单元（杆）截面面积PROP[],结点编号数组NCO[];
//用于集合单元刚度矩阵elemnet_stiffness_matrix，得到总刚度矩阵total_stiffness_matrix，通过方法truss_assemble()获得;

public class Assemble {
	
	//三个编号变量
	int sum_node,nm_of_support_node,nm_of_element,N;
	final int   
			 nm_node_DOF = 2, nm_element_node = 2,
			nm_element_DOF = 4;
	
	double[] X=new double[sum_node];
	double[] Y=new double[sum_node];
	double[] PROP=new double[nm_of_element];
	double[] element_FORCE=new double[nm_of_element];
	int[] node_ID = new int[sum_node*2];

	public static int half_band_width;



	//
	public Assemble(double[] X, double[] Y, double[] PROP, int[] node_ID) {		
		this.X = X;
		this.Y = Y;
		this.PROP = PROP;
		this.node_ID = node_ID;
		sum_node = Core.sum_node;
		nm_of_support_node = Core.nm_of_support_node;
		nm_of_element=Core.nm_of_element;
		N = sum_node*nm_node_DOF;
	}

	// 获得总刚度矩阵的带宽
	public int getHalfBandWidth() {
		return half_band_width;
	}

	public double[][] truss_assemble() {
		/*
		 * NCO（节点编号阵，按照单元编号顺序存放，反映杆端节点编号）,IB（支座结点位移状态，按照支座结点标号顺序存放，受约束为0,）
		 * ,X（结点位置）,Y,PROP,AL（节点荷载、结点位移
		 * ）,TK（总刚度矩阵）,ELST(单元刚阵)，FORC,REAC(支座已知位移，支反力)N=结点位移总数，
		 * NN=结点总数；NE=单元总数；
		 * NLN=受荷载结点总数；NBN=支座结点总数；MS半带宽；NDF=每个结点的自由度；NNE每个单元的结点数；
		 * NDFEL=每个单元的自由读数；
		 */
		int I, L1, J,  total_ZiKuai_Hang, K, L;
		int N1 = nm_element_node - 1;

		for (I = 0; I < nm_of_element; I++) {// 单元循环
			/*
			 * 计算半带宽MS
			 */
			L1 = nm_element_node * I ;
			
			for (J = 0; J <nm_element_node ; J++) {
				
				for (K = J + 1; K < nm_element_node; K++) {
					
					L = StrictMath.abs(node_ID[L1 + J] - node_ID[L1 + K]);
					
					if ((half_band_width - L) <= 0) {
						half_band_width = L;
					}
				}
			}
		}
		half_band_width = nm_node_DOF * (half_band_width + 1);// ////////
		double[][] total_stiffness_matrix = new double[N + 1][half_band_width + 2];

		int L4, N2, element_ZiKuai_Hang, element_ZiKuai_Lie, total_ZiKuai_Lie, KI, total_Hang, IC, element_Hang = 0, element_Lie = 0, total_Lie;

		double[][] elment_stiffness_matrix = new double[4][4];

		for (int NEL = 0; NEL < nm_of_element; NEL++) {// /////////nm_of_element

			// 每次循环到需要实例化一次类Element_Stiffness()
			// 获取单元刚度矩阵

			elment_stiffness_matrix = new Truss_element_Stiff(X, Y, PROP,
					node_ID, NEL).truss_element_stiffness_matrix();

			L4 = nm_element_node * NEL;// //////////// have changed !!

			for (I = 0; I < nm_element_node; I++) { /**/

				N1 = node_ID[L4 + I]; /* 结点码 */// ///////////////
				element_ZiKuai_Hang = nm_node_DOF * I ; /* element stiff row */
				total_ZiKuai_Hang = nm_node_DOF * (N1 - 1); /*
															 * TOTAL element
															 * stiff rank
															 */

				for (J = 0; J < nm_element_node; J++) { /* 只进行下三角字块中的元素 */

					N2 = node_ID[L4 + J];// ///////////////
					element_ZiKuai_Lie = nm_node_DOF * J ;

					total_ZiKuai_Lie = nm_node_DOF * (N2 - 1);
					for (K = 0; K < nm_node_DOF; K++) { /*
														 * /////////////////
														 * 对每个自由度循环，
														 * 即对每个位移分量产生的单刚元素循环
														 */
						KI = 0;// /////////////////
						if ((N1 - N2) == 0) {
							KI = K; // 对于主对角元N1=N2，则对单刚的循环L=KI,即对单刚主对角循环
						}
						/* 对于主对角元N1=N2，则对单刚的循环L=KI,即对单刚主对角循环 */
						/*
						 * 在推导单刚阵时，将局部坐标系原点设在单元i端，则得到的单刚字块相应的结点号总是i结点在前，j在后，与两者
						 * 大小无关
						 * ，但在总刚中，是按照结点码大小顺序排列的，故应分N1>N2;N1<N2两种情况，当N1>N2时，需要将
						 * 行列倒置即total_Hang
						 * =total_ZiKuai_Lie+K;IC=total_ZiKuai_Hang
						 * -total_Hang+1;
						 * element_Lie=element_ZiKuai_Lie+K;element_Hang
						 * =element_ZiKuai_Hang+L
						 * 反之，，，total_Hang=total_ZiKuai_Hang
						 * +K;IC=total_ZiKuai_Lie
						 * -total_Hang+1;element_Lie=element_ZiKuai_Lie
						 * +L;element_Hang=element_ZiKuai_Hang+K
						 */

						if ((N1 - N2) <= 0) {
							total_Hang = total_ZiKuai_Hang + K; /*
																 * total_Hang总刚行号，
																 * total_ZiKuai_Hang
																 * =
																 * NDF*(NCO[NNE*
																 * (NEL-1)]-1)
																 */
							IC = total_ZiKuai_Lie - total_Hang ; /*
																	 * 总刚中行号不变，
																	 * 只对列号变换。。。
																	 * 注意减去的是总刚的行号
																	 * ，
																	 * 而非总刚子块的行号
																	 * element_ZiKuai_Hang
																	 * ，
																	 * total_ZiKuai_Hang本为总刚行列号
																	 * ，
																	 * 但由于使用半带宽存储
																	 * （
																	 * I,J）在半带宽中为
																	 * （I,J-I+1）
																	 */

							// ////////////若改为IC=total_ZiKuai_Lie-total_Hang,则原矩阵对角元素，在二维半带宽存储矩阵的第0列

							element_Hang = element_ZiKuai_Hang + K; /*
																	 * element_Hang，
																	 * element_Lie为单刚行列号
																	 * ，
																	 */
						} else {
							total_Hang = total_ZiKuai_Lie + K;
							IC = total_ZiKuai_Hang - total_Hang ;

							// ////////////若改为IC=total_ZiKuai_Lie-total_Hang,则原矩阵对角元素，在二维半带宽存储矩阵的第0列

							element_Lie = element_ZiKuai_Lie + K;
						}

						for (L = KI; L < nm_node_DOF; L++) {
							total_Lie = IC + L;
							if ((N1 - N2) <= 0) {
								element_Lie = element_ZiKuai_Lie + L;
							}
							else {
								element_Hang = element_ZiKuai_Hang + L;
							}
							
							total_stiffness_matrix[total_Hang][total_Lie] = total_stiffness_matrix[total_Hang][total_Lie]
									+ elment_stiffness_matrix[element_Hang][element_Lie];
						}
					}
				}
			}

		}
		return total_stiffness_matrix;
	}

}
