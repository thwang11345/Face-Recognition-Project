package algorithms;


import java.util.*;
// https://www.cec.uchile.cl/cinetica/pcordero/MC_libros/NumericalRecipesinC.pdf 
/* Computing the eigenvalues and eigenvectors of a real symmetric matrix,
 * rewritten from C recipes for ease of use and efficiency.
 *
 * W.H. Press, S.A. Teukolsky, W.T. Vettterling and B.P. Flannery,
 * "Numerical Recipes in C", 2nd Edition, Cambridge University Press, 1992.
 *
 * 1. Householder tridiagonalization of a real symmetric Matrix:
 *    s_tred2() is adopted from tred2() (pp. 469-475).
 * 2. Compute eigenvalues and eigenvectors of a tridiagonal matrix:
 *    s_tqli() is adopted from tqli() (pp. 475-481).
 *
 * NOTE:
 *
 * 1. Solving eigensystem of a real symmetric matrix using Householder
 *    tridiagonalization and QL method is much more effiecient than
 *    Jacobi's method (7 to 8 times faster).
 *
 * 2. Example of solving eigensystem of a real symmetric matrix W:
 *
 *     s_tred2(W,n,D,E);         / Solve eigensystem using QR method    /
 *     s_tqli(D,E,n,W);
 *     s_eigensort(D,W,n);       / Sort eigenvalues and their associated
 *                                 eigenvectors in decreasing order     /
 */


class EigenV implements Comparable<EigenV> {
    
    double lambda;
    double [] vec;
    public EigenV(double l, double [] v) {
        lambda = l;
        vec = v ;
    }
    
    public double getLambda() {
        return lambda;
    }
    
    public double [] getV() {
        return vec;
    }
    
    public int compareTo(EigenV o) {
         if ( this.getLambda() > o.getLambda()) 
            return -1;
         else if ( this.getLambda() < o.getLambda()) 
            return 1;
         else
            return 0;
     }

    
}

public class QRSolver {

	public static double SQR(double a)
	{
		if ( a == 0.0 ) 
			return 0.0;
		else
			return a*a;
	}
	
	public static double SIGN(double a, double b)
	{
		if ( b >= 0 )
			return Math.abs(a);
		else
			return -Math.abs(a);
	}
	
	public static double pythag(double a, double b)
	{
		double absa,absb;
		absa=Math.abs(a);
		absb=Math.abs(b);
		if (absa > absb) {
			return (absa*Math.sqrt(1.0f+SQR(absb/absa)));
		}
		else {
			if ( absb == 0.0 )
				return 0.0;
			else
				return absb*Math.sqrt(1.0+SQR(absa/absb));
		}
	}

	/*----------------------------------------------------------------------
	 *                ***Householder's Tridiagonalization***
	 * a[] is the input nxn matrix represented by 1D vector. The outputs are 
	 * d[] and e[], which are diagonal elements and off-diagonal elements of 
	 * tridiagonal form of a[].  
	 */
	public static void s_tred2(double []a, int n, double [] d, double [] e)
	{
		int l,k,j,i,i_times_n, j_times_n, k_times_n;
		double scale,hh,h,g,f;

		for (i=n-1,i_times_n=(n-1)*n;i>=1;i--,i_times_n-=n) {
			l=i-1;
			h=scale=0.0;
			if (l > 0) {
				for (k=0;k<=l;k++)
					scale += (float) Math.abs(a[i_times_n+k]);
				if (scale == 0.0)
					e[i]=a[i_times_n+l];
				else {
					for (k=0;k<=l;k++) {
						a[i_times_n+k] /= scale;
						h += a[i_times_n+k]*a[i_times_n+k];
					}
					f=a[i_times_n+l];
					g=(float)(f >= 0.0f ? -Math.sqrt(h) : Math.sqrt(h));
					e[i]=scale*g;
					h -= f*g;
					a[i_times_n+l]=f-g;
					f=0.0;
					for (j=0,j_times_n=0;j<=l;j++,j_times_n+=n) {
						a[j_times_n+i]=a[i_times_n+j]/h;
						g=0.0;
						for (k=0;k<j;k++)
							g += a[j_times_n+k]*a[i_times_n+k];
						for (k=j,k_times_n=k*n;k<=l;k++,k_times_n+=n)
							g += a[k_times_n+j]*a[i_times_n+k];
						e[j]=g/h;
						f += e[j]*a[i_times_n+j];
					}
					hh=f/(h+h);
					for (j=0,j_times_n=0;j<=l;j++,j_times_n+=n) {
						f=a[i_times_n+j];
						e[j]=g=e[j]-hh*f;
						for (k=0;k<=j;k++)
							a[j_times_n+k] -= (f*e[k]+g*a[i_times_n+k]);
					}
				}
			} else
				e[i]=a[i_times_n+l];
			d[i]=h;
		}

		d[0]=0.0;
		e[0]=0.0;
		/* Contents of this loop can be omitted if eigenvectors not
				wanted except for statement d[i]=a[i_times_n+i]; */
		for (i=0,i_times_n=0;i<n;i++,i_times_n+=n) {
			l=i-1;
			if (d[i] != 0.0) {
				for (j=0;j<=l;j++) {
					g=0.0;
					for (k=0,k_times_n=0;k<=l;k++,k_times_n+=n)
						g += a[i_times_n+k]*a[k_times_n+j];
					for (k=0,k_times_n=0;k<=l;k++,k_times_n+=n)
						a[k_times_n+j] -= g*a[k_times_n+i];
				}
			}
			d[i]=a[i_times_n+i];
			a[i_times_n+i]=1.0;
			for (j=0,j_times_n=0;j<=l;j++,j_times_n+=n) a[j_times_n+i]=a[i_times_n+j]=0.0;
		}
	}
	

	/*------------------------------------------------------------------------*/
	/*           ***Eigensystem of a Tridiagonal Matrix***                  
	 * tqli stand for Tridiagonal QL implicit method.
	 * The inputs are d[] and e[], diagonal and off-diagonal elements of a 
	 * tridiagonal matrix. z[] stores the output eigenvectors, d[] is replaced 
	 * by eigenvalues, while e[] is detroyed. 
	 */
	
	public static void s_tqli(double [] d, double [] e , int n, double [] z)
	{
	
		int m,l,iter,i,k,k_times_n;
		double s,r,p,g,f,dd,c,b;
	
		for (i=1;i<n;i++) e[i-1]=e[i];
		e[n-1]=0.0;
		for (l=0;l<n;l++) {
	
			iter=0;
			do {
				for (m=l;m<n-1;m++) {
					dd=(float)( Math.abs(d[m])+Math.abs(d[m+1]));
					if ((float)(Math.abs(e[m])+dd) == dd) break;
				}
				if (m != l) {
					if (iter++ == 30) {
						break;
					}
					g=(d[l+1]-d[l])/(2.0*e[l]);
					r=pythag(g,1.0);
					g=d[m]-d[l]+e[l]/(g+SIGN(r,g));
					s=c=1.0f;
					p=0.0f;
					for (i=m-1;i>=l;i--) {
						f=s*e[i];
						b=c*e[i];
						e[i+1]=(r=pythag(f,g));
						if (r == 0.0) {
							d[i+1] -= p;
							e[m]=0.0;
							break;
						}
						s=f/r;
						c=g/r;
						g=d[i+1]-p;
						r=(d[i]-g)*s+2.0f*c*b;
						d[i+1]=g+(p=s*r);
						g=c*r-b;
						for (k=0,k_times_n=0;k<n;k++,k_times_n+=n) {
							f=z[k_times_n+i+1];
							z[k_times_n+i+1]=s*z[k_times_n+i]+c*f;
							z[k_times_n+i]=c*z[k_times_n+i]-s*f;
						}
					}
					if (r == 0.0 && i >= l) continue;
					d[l] -= p;
					e[l]=g;
					e[m]=0.0;
				}
			} while (m != l);
		}
	}


        // sort according to the lambda
        public static void  s_eigensort(double [] Lambda, double [] Cov, int n) {
            List<EigenV> list = new ArrayList<EigenV>();

            for(int i = 0; i<n; i++) {
                double [] v = new double[n];
                double l = Lambda[i];
                int k = i;
                for(int j = 0; j<n; j++, k+=n)
                    v[j] = Cov[k];
                EigenV eV = new EigenV(l,v);
                list.add(eV);
            }
            Collections.sort(list);
            // put it back
            for(int i = 0; i<n; i++) {
                EigenV eV = list.get(i);
                Lambda[i] = eV.getLambda();
                double [] v = eV.getV();
                int k = i;
                for(int j = 0; j<n; j++, k+=n)
                    Cov[k] = v[j];
            }
        }
}
