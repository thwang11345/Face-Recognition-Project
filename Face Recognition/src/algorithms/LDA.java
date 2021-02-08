package algorithms;
//import Jama.EigenvalueDecomposition;
//import Jama.Matrix;

public class LDA {
	
    static final double PCA_PERCENTAGE = 0.99 ;
    static final int  TOTAL_CLASS      = 50;  // maximum number of classes
    static final double TINY           = 1.0e-10;
    
    public LDA() {
        
    }
    static final double EPSILON = 1.0e-6;
    
    static public double [] zeroMeanNorm(double [] x) {
        double [] y = null;
        if (x == null || x.length <= 0)
            return y;
        int size = x.length;
        y = new double[x.length];
        double mean = 0.0;
        for(double d : x)
            mean += d;
        mean /= size;
        double norm = 0;
        for(int i=0;i<size; i++) {
            double tmp = x[i] - mean;
            norm += tmp*tmp;
            y[i] = tmp;
        }
        norm = Math.sqrt(norm);
        if ( norm < EPSILON )
            return null;
        for(int i=0;i<size; i++) {
            y[i] /= norm;
        }         
        return y;
    }
    
    /*
    ********************************************************************
    PCA (Principal Component Analysis) Procedure:
      Calculate eigenvalue of A At(nxn), instead of At A(dxd), usually 
      n<<d, wi is eigenvector of At A, the 

            vi=At wi (dx1) is eigenvector of At A
      
      1.  M;                                          -- Mean, dx1
      2.  A                                           -- A nxd, M,X dx1
          A[k] = Xt[k] - M;   k=1,2,..., n             -- 
          At = transpose of U;                        -- At dxn
      3   Cov=1/n At A                                -- 
      4.  Call jacobi(Cov, d, Lambda, W, &IterationNo)-- W is dxd
          Call eigensort(Lambda, W);                  --  
      5.  Find sum_lambda = sum of all Lambda         -- denominator
          percent = 0
          m = 0;
          partial_sum = 0;
          while (percent < Mef_PerCentage) {
            partial_sum += lambda[m];
            percent = partial_sum/sum_lambda;
            m++;
            }
      6.  Ht first m column of W                       -- Ht dxm
      7.  Yt = A Ht                                    -- Yt nxm
     ********************************************************************
    */
    
    static public PCAMap PCA(double [] Xt, int n,  int d )
    {
        Integer m;
        double [] Ht;
        double [] Yt;
        double [] M;
        
        double [] A, At, Cov, Lambda, e;
        
        double partial_sum, sum_lambda, percent;
        int k,j1,j2,i,j;

        M = new double[d];

        for (j=0;j<n;j++) {
                j1=j*d;
            for (i=0;i<d;i++) 
                M[i] += Xt[j1+i]; 
        }
        for (i=0;i<d;i++) 
                M[i] /= n; 

        // A: each row of A: at=xt-M, A nxd
        A = new double[n*d];
        for (j=0;j<n;j++) {
                j1=j*d;
                for (i=0;i<d;i++) 
                        A[j1+i]=Xt[j1+i]-M[i];
        }

        // At: transpose of A, At dxn 
        At = new double[d*n];
        for (i=0;i<d;i++) {
          for (j=0;j<n;j++) 
                  At[i*n+j]=A[j*d+i]; 
        }

        // Calculation of Covariance Matrix, Cov nxn
        Cov = new double[n*n];
        for (j=0;j<n;j++)
        { 
        	j1=j*n; j2=j*d;
        	for (i=j;i<n;i++) {
        		for (k=0;k<d;k++)
        			Cov[j1+i] += A[j2+k]*At[k*n+i];

        	}
        }
   
        // Use the property of symmetry
        for (j=0;j<n;j++) {
        	for (i=0;i<j;i++) 
        		Cov[j*n+i]=Cov[i*n+j];
        }
        
        for (j=0;j<n;j++) {
        	for (i=0;i<n;i++) 
        		Cov[j*n+i] /= n;
        }
        
        /* Compared with JAMA
        // using JAMA
        Matrix mat = new Matrix(Cov,n);   // this conversion is column majored, however, Cov is sysmetric so it's ok
        EigenvalueDecomposition eigenvalueDecomposition = new  EigenvalueDecomposition(mat);
        double [] lamda1 = eigenvalueDecomposition.getRealEigenvalues();
        Matrix EigV = eigenvalueDecomposition.getV();
        Matrix EigD = eigenvalueDecomposition.getD();
        Matrix EigVT = EigV.transpose();
        Matrix K = EigVT.times(EigV);

        int idx = n-3;
        Matrix V1 = EigV.getMatrix(0,n-1,idx,idx); // last column
        V1 = V1.times(lamda1[idx]);
        
        Matrix AV = mat.times(EigV);
        Matrix V11 = AV.getMatrix(0,n-1,idx,idx); 
        double dist = V11.minus(V1).norm2();
*/
        
        // Calculation of eigenvalues and eigenvectors
        Lambda = new double[n]; 
        e = new double[n]; 
        QRSolver.s_tred2(Cov, n, Lambda, e);
        QRSolver.s_tqli(Lambda,e,n,Cov); 
        QRSolver.s_eigensort(Lambda, Cov, n); 

        
        /* Compared with JAMA
        for( i=0;i<n;i++)
        {
            for(j=0;j<n;j++)
                System.out.print(Cov[i*n+j] +" ");
            System.out.println("");
        }
        System.out.println("");
        
        for( i=0;i<n;i++)
        {
            for(j=0;j<n;j++)
                System.out.print(EigV.get(i,j)+ " ");
            System.out.println("");
        }
        System.out.println("");
        
        Matrix EigV2 = new Matrix(Cov,n); // this conversion is column majored
        EigV2 = EigV2.transpose(); // need to transpoose to be row majored
        
        for( i=0;i<n;i++)
        {
            for(j=0;j<n;j++)
                System.out.print(EigV2.get(i,j)+ " ");
            System.out.println("");
        }
        
        
        for(i = 0; i<n; i++) {
            double l1 = Lambda[i];
            double l2 = lamda1[n-1-i];
            
            double delta1 = Math.abs(l1-l2);
            
            Matrix VV1 = EigV2.getMatrix(0,n-1,i, i);
            Matrix VV2 = EigV.getMatrix(0,n-1,n-1-i, n-1-i);
            
            double delta2 = (VV1.minus(VV2)).norm2();
            double delta3 = (VV1.plus(VV2)).norm2();

            if ( delta1 > 1.0e-6 || ( delta2> 1.0e-6 && delta3 > 1.0e-6 ) ) {
                int debug =1;
            }
        }
        */
        

        //Find 
        sum_lambda=0;
        for (j=0;j<n;j++) 
        	sum_lambda += Lambda[j];
        m=0;
        percent = partial_sum = 0;
        while (percent < PCA_PERCENTAGE) {
            partial_sum += Lambda[m];
            percent = partial_sum/sum_lambda;
            m++;
        }


        //Form Ht, Ht dxm: Ht is the first m columns of WA
        Ht = new double[d*m];
        for (j=0;j<d;j++) { 
            j1 = j*m;
            j2 = j*n;
            for (i=0;i<m;i++) {
                for (k=0;k<n;k++) 
                    Ht[j1+i] += At[j2+k]*Cov[k*n+i];
            }
	}
        

       // Normalize each column of Ht, s.t. the norm of each column will be unit
       Ht = Matrix.d1ColumnNorm(Ht, d, m, m);

        // Calculate Yt, Yt nxm  Yt = A*Ht
        Yt = Matrix.d1Mult(A,n,d,Ht,d,m);
        
        PCAMap retval = new PCAMap(m, Ht, Yt, M );
    	
    	return retval;
    }
    
/*
********************************************************************
LEARN_MDF:
1.  Calculate within-class scatter matrix Sw          -- note Sw is mxm.
2.  d[i] = m[i] - M, i=0..c                           -- note mx1
    Sb = 0;                                           -- note mxm
    for (i=0; i<c; i++) {
      Sb += scalarmatrixmult(
        p[i], 
        matrixmult(d[i], m, 1, trans(d[i], m, 1), 1, m))); -- from utils
3.  SbInv = matrixinv(Sb, m, m);                      -- from NR p. 45
4.  L = matrixmult(SwInv, m, m, Sb, m, m);            -- from utils
5.  From Eispack, p. 26:  
      call balanc, elmhes, eltran, hqr2, balbak.
      These routines were written in FORTRAN and need to be
      converted to C.  Note the NSWC code may have alternative
      solutions, but I have not found documentation for them yet.
6.  Call sort(Lambda, W);                     -- from utils
********************************************************************/
    static public LDAMap Learn(double [] set, int [] class_labels, int len) {

        // double  []set;                    set of training vectors 
        // int     []class_labels;           list of training labels 
        // int     num;                      number of training vectors in set 
        // int     len;                      length of input vectors 
        // double  [][]Mat;                  output MDF projection matrix OUT 
        // int     []no_classes;             #_of_classes in input data set OUT
        
        int     i,j,k,ii,jj,d;            // loop index 
        int     [] clas;
        int     c_num;
        double  [][] m;                   // mean vector 
        int     [] per_class;             // number of images per class 
        double  [] Sw;                    // within-class scatter matrix 
        double  [] Sb;                    // between-class scatter matrix 
        double  [] Gm;                    // grand mean vector 
        double  lambda;                   // single eigenvalue 
        double  [] Lambda;                // eigenvalues from num recipies 
        double  [] e;                     
        double  [] H;                     // eigenvectors Sw 
        double  [] splitB;                // matrix split by W 
        double  [] U;                     // eigenvectors of splitB 
        double  [] W;                     // mdf projection matrix 
        double  [] sigma;                 // eigenvalues of splitB 
        
        Integer no_classes;
        int num = class_labels.length;

        clas = new int[TOTAL_CLASS];
        for(i=0;i<TOTAL_CLASS;i++)
                clas[i]=(-1);
        c_num=0;
        for(i=0;i<num;i++) {
            if(clas[class_labels[i]]==(-1)) {
                clas[class_labels[i]]=c_num;
                c_num++;
            }
        }

        no_classes = c_num;
        d = c_num-1;
        
        m = new double[c_num][len];
        Gm = new double[len]; 
        per_class= new int[c_num ];

        for(i=0;i<num;i++) {
            int label = clas[class_labels[i]];
            for(j=0;j<len;j++) 
                    m[label][j]+=set[i*len+j];
            per_class[label]++;
        }
        for(i=0;i<c_num;i++) {
                for(j=0;j<len;j++)
                        m[i][j]/=per_class[i];
                }
        for(i=0;i<len;i++) {
                for(j=0;j<c_num;j++) 
                        Gm[i]+=(per_class[j]*m[j][i]);
                Gm[i]/=num;
                }
                
        Sw = new double[len*len];
        Sb = new double[len*len];

        for(i=0;i<num;i++) {
            for(j=0;j<len;j++) {
                    for(k=0;k<len;k++) {
                            Sw[j*len+k]+=(set[i*len+j]-m[clas[class_labels[i]]][j])*
                                    (set[i*len+k]-m[clas[class_labels[i]]][k]);
                    }
            }
        }
        for(i=0;i<c_num;i++) {
            for(j=0;j<len;j++) {
                for(k=0;k<len;k++) {
                    Sb[j*len+k]+=(m[i][j]-Gm[j])*(m[i][k]-Gm[k]);
                }
            }
        }

       /********
        Finding the eigenvectors/eigenvalues of the matrix
        Sw-inverse * SB

        1.  Compute H, Lambda: eigenvectors/eigenvalues of Sw.
        2.  Sort the eigensystem.
        2.5.Add the median eigenvalue to Lambda
        3.  Set Lambda = 1/(sqrt(Lambda)).
        4.  Compute H = H Lambda.
        5.  Compute splitB = H-transpose * Sb * H
        6.  Compute U, sigma: eigenvectors/eigenvalues of splitB.
        7.  Sort the eigensystem.
        8.  Compute W = H U
        9.  Normalize the columns of W.

        W is the MDF projection matrix.  Note for now, W should be float because
        the other programs are expecting it to be.
      *******/
     
        Lambda = new double[len];                                             
        e      = new double[len];
        QRSolver.s_tred2(Sw, len, Lambda, e);
        QRSolver.s_tqli(Lambda,e,len,Sw);
        QRSolver.s_eigensort(Lambda, Sw, len); 
        H=Sw; 
  
        for (i=0; i<len; i++) {
            if (Lambda[i] <= 0) {
                Lambda[i] = TINY;
            }
            Lambda[i] = (1.0 / Math.sqrt(Lambda[i]));
        }

        for (jj=0; jj<len; jj++) { /* do for each column */
            k = jj;
            lambda = Lambda[jj];
            for (ii=0; ii<len; ii++, k+=len) { /* do for each row of column jj */
                H[k] = H[k]*lambda;     
            }
        }
        
        double [] Ht =Matrix.d1Transpose(H, len, len);
        Sb = Matrix.d1Mult(Ht, len, len, Sb, len, len);
        splitB = Matrix.d1Mult(Sb, len, len, H, len, len);
        
        sigma = new double[len];
        QRSolver.s_tred2(splitB, len, sigma, e);
        QRSolver.s_tqli(sigma,e,len,splitB);
        QRSolver.s_eigensort(sigma, splitB, len); 
        U = splitB;
        W = Matrix.d1Mult(H, len, len, U, len, len);
        
        // normalize the columns of W and store in a float matrix to be compatible 
        // with the old way of computing the eigensystem 
        double [] Mat = Matrix.d1ColumnNorm(W, len, len, d);
        LDAMap ldaMap = new LDAMap(no_classes, Mat);
        
        return ldaMap;
          
    } // mdf_learn 
    
}
