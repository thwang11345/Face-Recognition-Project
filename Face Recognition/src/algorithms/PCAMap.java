package algorithms;
/**
 * Used to store resulting information of a PCA operation. Contains the projected points, mean ect.
 * @author Thomas Hwang
 *
 */
public class PCAMap {
    Integer M;
    double [] Ht;  // (xdim, m), xdim is the length of feature vector or image size, m is the effective mef feature length
    double [] Yt;  // (noData, m) noData is the number of training samples
    double [] Mean;
    /**
     * Constructs a PCA with the information for the projected points.
     * @param m the number of dimensions in the projection
     * @param ht 
     * @param the projected points (n x m) matrix flattened to a 1d array
     * @param mean features of all samples which is a length d matrix where d = 128 x 128
     */
    public PCAMap(Integer m, double []ht, double [] yt, double [] mean) {
        M = m;
        Ht = ht;
        Yt = yt;
        Mean = mean;
    }
    /**
     * Returns the number of dimensions in the projection. 
     * @return the number of dimensions in the projection
     */
    public int getM() { 
        return M;
    }
    
    public double [] getHt() {
        return Ht;
    }
    /**
     * Returns the projection of the samples. The projection will an (n x m) matrix flattened to a 1d array, where n is the number of samples
     * and m is the number of dimensions in the projection. 
     * @return the projection of the samples
     */
    public double [] getYt() {
        return Yt;
    }
    /**
     * Returns the mean feature of the samples which is a length d matrix where d = 128 x 128.
     * @return the mean feature of the samples
     */
    public double [] getMean() {
        return Mean;
    }
}
