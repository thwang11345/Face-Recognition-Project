package algorithms;
/**
 * Contains matrix operations for flattened matrices.
 * @author Thomas Hwang
 *
 */
public class Matrix {
	public Matrix() {

	}

	// Given a matrix a in 1D with dimension (m,n) return it's transpose with
	// dimension (n, m)
	/**
	 * Returns a transposed matrix with dimensions (n x m) given a matrix with dimensions (m x n). The input and output
	 * matrices are flattened. 
	 * @param a m x n matrix flattened to a 1d array of length m x n
	 * @param m number of rows in 2d matrix representation
	 * @param n number of columns in 2d matrix representation
	 * @return a transposed matrix with dimensions (n x m) in a 1d array
	 */
	public static double[] d1Transpose(double[] a, int m, int n) {
		double[] retval = new double[m * n];
		for (int i = 0; i < m; i++) {
			int ii = i * n;
			int jj = i;
			for (int j = 0; j < n; j++, ii++, jj += m) {
				retval[jj] = a[ii];
			}
		}
		return retval;
	}

	// Given a matrix a in 1D with dimension (m1,n1) and b in 1D with (m2,n2),
	// return the multiplication
	/**
	 * Returns a resulting matrix by multiplying matrices (m1 x n1) x (m2 x n2) = (m1 x n2) where n2=m1. If n2!=m1, returns null.
	 * The input and output matrices are flattened. So the output matrix is a 1d array of length (m1 x n2).
	 * @param a first matrix
	 * @param m1 rows of first matrix
	 * @param n1 columns of second matrix
	 * @param b second matrix
	 * @param m2 rows of second matrix
	 * @param n2 columns of second matrix
	 * @return if n1=n2, a (m1 x n2) matrix in a 1d array, otherwise null.
	 */
	public static double[] d1Mult(double[] a, int m1, int n1, double[] b, int m2, int n2) {
		double[] retval = null;
		if (n1 != m2)
			return retval;

		retval = new double[m1 * n2];
		for (int i = 0; i < m1; i++) {
			for (int j = 0; j < n2; j++) {
				for (int k = 0; k < n1; k++) {
					retval[i * n2 + j] += a[i * n1 + k] * b[k * n2 + j];
				}
			}
		}
		return retval;
	}

	// Given a matrix a in 1D with dimension (m1, n1), normalize the first n2 column
	// return the (m1,n2) matrix
	// returns a unit vector of a
	/**
	 * 
	 * @param a
	 * @param m1
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static double[] d1ColumnNorm(double[] a, int m1, int n1, int n2) {
		double[] retval = new double[m1 * n2];

		for (int i = 0; i < n2; i++) {
			double sum = 0;
			int k = i;
			for (int j = 0; j < m1; j++, k += n1) {
				double tmp = a[k];
				sum += tmp * tmp;
			}
			sum = Math.sqrt(sum);
			k = i;
			int l = i;
			for (int j = 0; j < m1; j++, k += n1, l += n2) {
				retval[l] = a[k] / sum;
			}
		}
		return retval;
	}

	// convert 1D array to 2D with dimension (m, 2)
	/**
	 * Converts matrix (m x n) in 1d array to 2d array.
	 * @param a the given matrix
	 * @param m the number of rows
	 * @param n the number of columns 
	 * @return a matrix (m x n) in a 2d array
	 */
	public static double[][] d1ToD2(double[] a, int m, int n) {
		double[][] retval = null;
		if (a.length != (m * n))
			return retval;

		retval = new double[m][n];
		for (int i = 0; i < m; i++) {
			int k = i * n;
			for (int j = 0; j < n; j++, k++)
				retval[i][j] = a[k];
		}

		return retval;
	}
}
