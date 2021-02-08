package algorithms;


public class LDAMap {
    Integer noClasses; // number of classes
    double [] matrix;  // (m, c)  (m: effective dimension of MEF, c: noClasses-1 )
    public LDAMap(int nC, double [] mat) {
        noClasses = nC;
        matrix = mat;
    }
    
    public int getNoClasses() {
        return noClasses;
    }
    
    public double [] getMatrix() {
        return matrix;
    }
}
