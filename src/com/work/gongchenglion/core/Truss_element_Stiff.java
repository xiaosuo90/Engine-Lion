package com.work.gongchenglion.core;
//
class Truss_element_Stiff{

		public static int sum_node=4,nm_of_element=4,nm_of_load_node=2,nm_of_support_node=2,nm_node_DOF=2,nm_element_node=2,nm_element_DOF=4,N=8;
		double E=300000.;
		double[] X=new double[sum_node];
		double[] Y=new double[sum_node];
		double[] PROP=new double[nm_of_element];
		double[] element_FORCE=new double[nm_of_element];
		int[] node_ID = new int[sum_node*2];

		double[][] element_stiffness_matrix=new double[4][4];
		int NEL;
//设法通过变量声明，让上面的数据在整个包中公用
		public Truss_element_Stiff(double[] X, double[] Y, double[] PROP,
				int[] node_ID,int NEL) {
			super();
			this.X = X;
			this.Y = Y;
			this.PROP = PROP;
			this.node_ID = node_ID;
			this.NEL=NEL;
			sum_node=Core.sum_node;
			nm_of_element=Core.nm_of_element;
			nm_of_support_node=Core.nm_of_support_node;
		}
		
		
	public  double[][] truss_element_stiffness_matrix() {
			
			
		/*NCO（节点编号阵，按照单元编号顺序存放，反映杆端节点编号）,IB（支座结点位移状态，按照支座结点标号顺序存放，受约束为0,）
		 * ,X（结点位置）,Y,PROP,AL（节点荷载、结点位移
		 * ）,TK（总刚度矩阵）,ELST(单元刚阵)，FORC,REAC(支座已知位移，支反力)
		 * NN=结点总数；NE=单元总数；NLN=受荷载结点总数；NBN=支座结点总数；MS半带宽；NDF=每个结点的自由度；NNE每个单元的结点数；NDFEL=每个单元的自由读数；
		 * */

		int L,N1,N2,I,J,K1,K2;
		double D,CO,SI;
		double[] COEF= new double[nm_of_element];
//		int[] node_ID = { 0, 1, 3, 2, 3, 4, 3, 2, 4 };

		L=nm_element_node*NEL;
		N1=node_ID[L]-1;
		N2=node_ID[L+1]-1;
		D=StrictMath.sqrt(StrictMath.pow(X[N2]-X[N1],2)+StrictMath.pow(Y[N2]-Y[N1],2));
		CO=(X[N2]-X[N1])/D;
		SI=(Y[N2]-Y[N1])/D;
		COEF[NEL]=E*PROP[NEL]/D;
		element_stiffness_matrix[0][0]=COEF[NEL]*CO*CO;
		element_stiffness_matrix[0][1]=COEF[NEL]*CO*SI;
		element_stiffness_matrix[1][1]=COEF[NEL]*SI*SI;
		for(I=0;I<=1;I++){
			for(J=0;J<=1;J++){
				K1=I+nm_node_DOF;
				K2=J+nm_node_DOF;
				element_stiffness_matrix[K1][K2]=element_stiffness_matrix[I][J];
				element_stiffness_matrix[I][K2]=-element_stiffness_matrix[I][J];
				element_stiffness_matrix[K2][I]=element_stiffness_matrix[I][K2];
			}
		}
		element_stiffness_matrix[1][2]=-element_stiffness_matrix[0][1];
		element_stiffness_matrix[2][0]=element_stiffness_matrix[0][2];
		element_stiffness_matrix[1][0]=element_stiffness_matrix[0][1];
		element_stiffness_matrix[2][1]=element_stiffness_matrix[1][2];
		element_stiffness_matrix[3][2]=element_stiffness_matrix[2][3];
		return element_stiffness_matrix;
	}
}