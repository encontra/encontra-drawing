package pt.inevo.encontra.drawing;

/**
* This class represents the configuration settings to be employed in the
* drawing simplification procedure.
* @author Gabriel
*/
public class Simplification {

    private double tolerance = .0075; //tollerance for inner vertices relative to length of the main-diagonal, lucky number (the bigger the simpeler)
    private double areaTolerance = .004; //tollerance for area size, lucky number
    private double lineTolerance = .2; //total line length relative to length of the main-diagonal, lucky number
    private double preTolerance = .005; //tollerance for inner vertices relative to length of the main-diagonal, lucky number (the bigger the simpeler) Used to simplify before clipping, should not exceed TOL
    private double overTolerance = 2; //set <=1 to disable, this makes it possible to over optimize object wich exist of more then MAX_VERTICES vectices
    private int maxCountOptimize = 20; //sets modulo for when to pre-optimize
    private int maxVertices = 1000; //maximum number of vertices per Primitive
    private double surroundTolDist = .006; //tollerance for distance between two points of different vertices, lucky number (this tells something about how near the two points should be next to each other)
    private double surroundTolPoints = .9;  //tollerance for the number of points that should be within range of the outter vertices, lucky number (this tells something about how many of the points should match the max_distance-requirement)
    private double hueTolerance = 5; //tollerance for hue of color [0-360], lucky number
    private double saturationTolerance = .2; //tollerance for saturation of color [0-1], lucky number
    private double intensityToleration = .4; //tollerance for intensity of color [0-1], lucky number

    public static final short NONE = 0;
    public static final short COLOR_CONCAT_PRIMITIVES = 1;
    public static final short REMOVE_SMALL_PRIMITIVES = 2;
    public static final short REDUCE_VERTEX_COUNT = 3;
    public static final short SIMPLIFICATION_COUNT = 3;

    private short[] order = new short[] {0,0,0};

    public Simplification() {
    }

    /**
     * @return the tolerance
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * @param tolerance the tolerance to set
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * @return the areaTolerance
     */
    public double getAreaTolerance() {
        return areaTolerance;
    }

    /**
     * @param areaTolerance the areaTolerance to set
     */
    public void setAreaTolerance(double areaTolerance) {
        this.areaTolerance = areaTolerance;
    }

    /**
     * @return the lineTolerance
     */
    public double getLineTolerance() {
        return lineTolerance;
    }

    /**
     * @param lineTolerance the lineTolerance to set
     */
    public void setLineTolerance(double lineTolerance) {
        this.lineTolerance = lineTolerance;
    }

    /**
     * @return the preTolerance
     */
    public double getPreTolerance() {
        return preTolerance;
    }

    /**
     * @param preTolerance the preTolerance to set
     */
    public void setPreTolerance(double preTolerance) {
        this.preTolerance = preTolerance;
    }

    /**
     * @return the overTolerance
     */
    public double getOverTolerance() {
        return overTolerance;
    }

    /**
     * @param overTolerance the overTolerance to set
     */
    public void setOverTolerance(double overTolerance) {
        this.overTolerance = overTolerance;
    }

    /**
     * @return the maxCountOptimize
     */
    public int getMaxCountOptimize() {
        return maxCountOptimize;
    }

    /**
     * @param maxCountOptimize the maxCountOptimize to set
     */
    public void setMaxCountOptimize(int maxCountOptimize) {
        this.maxCountOptimize = maxCountOptimize;
    }

    /**
     * @return the maxVertices
     */
    public int getMaxVertices() {
        return maxVertices;
    }

    /**
     * @param maxVertices the maxVertices to set
     */
    public void setMaxVertices(int maxVertices) {
        this.maxVertices = maxVertices;
    }

    /**
     * @return the surroundTolDist
     */
    public double getSurroundTolDist() {
        return surroundTolDist;
    }

    /**
     * @param surroundTolDist the surroundTolDist to set
     */
    public void setSurroundTolDist(double surroundTolDist) {
        this.surroundTolDist = surroundTolDist;
    }

    /**
     * @return the surroundTolPoints
     */
    public double getSurroundTolPoints() {
        return surroundTolPoints;
    }

    /**
     * @param surroundTolPoints the surroundTolPoints to set
     */
    public void setSurroundTolPoints(double surroundTolPoints) {
        this.surroundTolPoints = surroundTolPoints;
    }

    /**
     * @return the hueTolerance
     */
    public double getHueTolerance() {
        return hueTolerance;
    }

    /**
     * @param hueTolerance the hueTolerance to set
     */
    public void setHueTolerance(double hueTolerance) {
        this.hueTolerance = hueTolerance;
    }

    /**
     * @return the saturationTolerance
     */
    public double getSaturationTolerance() {
        return saturationTolerance;
    }

    /**
     * @param saturationTolerance the saturationTolerance to set
     */
    public void setSaturationTolerance(double saturationTolerance) {
        this.saturationTolerance = saturationTolerance;
    }

    /**
     * @return the intensityToleration
     */
    public double getIntensityToleration() {
        return intensityToleration;
    }

    /**
     * @param intensityToleration the intensityToleration to set
     */
    public void setIntensityToleration(double intensityToleration) {
        this.intensityToleration = intensityToleration;
    }

    /**
     * @return the order
     */
    public short[] getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(short[] order) {
        if (this.order.length == order.length) {
            System.arraycopy(order, 0, this.order, 0, order.length);
        }
    }
}
