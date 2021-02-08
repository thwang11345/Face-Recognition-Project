package algorithms;


public class PCAMap {
    Integer M;
    double [] Ht;  // (xdim, m), xdim is the length of feature vector or image size, m is the effective mef feature length
    double [] Yt;  // (noData, m) noData is the number of training samples
    double [] Mean;
    public PCAMap(Integer m, double []ht, double [] yt, double [] mean) {
        M = m;
        Ht = ht;
        Yt = yt;
        Mean = mean;
    }
    
    public int getM() { 
        return M;
    }
    
    public double [] getHt() {
        return Ht;
    }
    
    public double [] getYt() {
        return Yt;
    }
    
    public double [] getMean() {
        return Mean;
    }
}
