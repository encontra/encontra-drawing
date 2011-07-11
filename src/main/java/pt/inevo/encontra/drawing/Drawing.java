package pt.inevo.encontra.drawing;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import pt.inevo.encontra.drawing.util.Color;
import pt.inevo.encontra.geometry.Point;
import pt.inevo.encontra.storage.IEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
* This class represents a format for vector-based images.
* @author Gabriel
*/
public class Drawing implements IEntity<Long> {
    private ArrayList<Primitive> primitives;
    private long id;

    /**
     * Creates a new Drawing.
     */
    public Drawing() {
        primitives = new ArrayList<Primitive>();
        id = -1;
    }

    /**
     * Creates a new Drawing.
     * @param id the drawing's identifier.
     */
    public Drawing(long id) {
        primitives = new ArrayList<Primitive>();
        this.id = id;
    }

    /**
     * Removes all primitives of the drawing.
     */
    public void removeAllPrimitives() {
        getAllPrimitives().clear();
    }

    /**
     * Returns all primitives of the drawing.
     * @return all primitives of the drawing.
     */
    public ArrayList<Primitive> getAllPrimitives() {
        return primitives;
    }

    /**
     * Replaces all primitives of the drawing.
     * @param primitives the primitives to set.
     */
    public void setPrimitives(ArrayList<Primitive> primitives) {
        this.primitives = primitives;
    }

    /**
     * Returns the primitives sorted on an axis (currently x).
     *
     * @return the primitives sorted on an axis (currently x).
     */
    public List<Primitive> getAllPrimitivesSorted() {
        // std::vector<Primitive*>

        List<Primitive> orderedPrimitives = new ArrayList<Primitive>();

        int size = primitives.size();

        Primitive [] primitiveArray = new Primitive[size];

        // Copy pointers to a temporary array
        for(int i = 0; i < size; i++) {
            primitiveArray[i] = primitives.get(i);
        }

        while(orderedPrimitives.size() != primitives.size()) {
            double xPointer = Double.MAX_VALUE;//std::numeric_limits<double>::max();
            int indexPointer = Integer.MIN_VALUE;//std::numeric_limits<int>::min();

            for (int j = 0; j < size; j++) {
                if(primitiveArray[j] != null) {
                    if(primitiveArray[j].getXmin() <= xPointer) {
                        xPointer = primitiveArray[j].getXmin();
                        indexPointer = j;
                    }
                }
            }
            orderedPrimitives.add(primitives.get(indexPointer)); // TODO check if it equals push_back();
            primitiveArray[indexPointer] = null;
        }
        return orderedPrimitives;
    }

    // Indagare
    /**
     * Returns the primitives sorted by area.
     *
     * @return the primitives sorted by area.
     * */
    List<Primitive> getAllPrimitivesSortedByArea() {
        // std::vector<Primitive*>
        List<Primitive> orderedPrimitives = new ArrayList<Primitive>();

        int size = primitives.size();

        Primitive [] primitiveArray = new Primitive[size];

        // Copy pointers to a temporary array
        for(int i = 0; i < size; i++) {
            primitiveArray[i] = primitives.get(i);
        }

        while(orderedPrimitives.size() != primitives.size()) {
            double aPointer = Double.MAX_VALUE;
            int indexPointer = Integer.MIN_VALUE;

            for (int j = 0; j < size; j++) {
                if(primitiveArray[j] != null) {
                    if(primitiveArray[j].getAreaSize() <= aPointer) {
                        aPointer = primitiveArray[j].getAreaSize();
                        indexPointer = j;
                    }
                }
            }
            orderedPrimitives.add(primitives.get(indexPointer));
            primitiveArray[indexPointer] = null;
        }
        return orderedPrimitives;
    }

    /**
     * Returns the id of the drawing.
     * @return the id of the drawing.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of the drawing.
     * @param id the id to set.
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the width of the drawing.
     * @return the width of the drawing.
     */
    public double getWidth() {
        if (getNumPrimitives() > 0) {
            double xmin = primitives.get(0).getXmin(), min;
            double xmax = primitives.get(0).getXmax(), max;
            for (int i=1; i<primitives.size(); i++) {
                    min = primitives.get(i).getXmin();
                    max = primitives.get(i).getXmax();
                    if (min<xmin) xmin=min;
                    if (max>xmax) xmax=max;
            }
            return Math.abs(xmax-xmin);
        }
        else
            return 0;
    }

    /**
     * Returns the height of the drawing.
     * @return the height of the drawing.
     */
    public double getHeight() {
        if (getNumPrimitives() > 0) {
            double ymin = primitives.get(0).getYmin(), min;
            double ymax = primitives.get(0).getYmax(), max;
            for (int i=1; i<primitives.size(); i++) {
                    min = primitives.get(i).getYmin();
                    max = primitives.get(i).getYmax();
                    if (min<ymin) ymin=min;
                    if (max>ymax) ymax=max;
            }
            return Math.abs(ymax-ymin);
        }
        else
            return 0;
    }

    /**
     * Returns the minimum value of the x coordinate for this drawing.
     * @return the minimum value of the x coordinate for this drawing.
     */
    public double getXmin() {
        if (getNumPrimitives() > 0) {
            double xmin = primitives.get(0).getXmin(), min;
            for (int i=1; i<primitives.size(); i++) {
                    min = primitives.get(i).getXmin();
                    if (min<xmin) xmin=min;
            }
            return xmin;
        }
        else
            return 0;
    }

    /**
     * Returns the maximum value of the x coordinate for this drawing.
     * @return the maximum value of the x coordinate for this drawing.
     */
    public double getXmax() {
        if (getNumPrimitives() > 0) {
            double xmax = primitives.get(0).getXmax(), max;
            for (int i=1; i<primitives.size(); i++) {
                    max = primitives.get(i).getXmax();
                    if (max>xmax) xmax=max;
            }
            return xmax;
        }
        else
            return 0;
    }

    /**
     * Returns the minimum value of the y coordinate for this drawing.
     * @return the minimum value of the y coordinate for this drawing.
     */
    public double getYmin() {
        if (getNumPrimitives() > 0) {
            double ymin = primitives.get(0).getYmin(), min;
            for (int i=1; i<primitives.size(); i++) {
                    min = primitives.get(i).getYmin();
                    if (min<ymin) ymin=min;
            }
            return ymin;
        }
        else
            return 0;
    }

    /**
     * Returns the maximum value of the y coordinate for this drawing.
     * @return the maximum value of the y coordinate for this drawing.
     */
    public double getYmax() {
        if (getNumPrimitives() > 0) {
            double ymax = primitives.get(0).getYmax(), max;
            for (int i=1; i<primitives.size(); i++) {
                    max = primitives.get(i).getYmax();
                    if (max>ymax) ymax=max;
            }
            return ymax;
        }
        else
            return 0;
    }

    /**
     * Returns the diagonal length of the drawing.
     * @return the diagonal length of the drawing.
     */
    public double getDiagonalLength() {
        return Math.sqrt((getWidth()*getWidth()) + (getHeight()*getHeight()));
    }

    /**
     * Returns the area of the drawing.
     * @return the area of the drawing.
     */
    public double getArea() {
        return getWidth()*getHeight();
    }

    /**
     * Adds a primitive to the drawing.
     * @param primitive the primitive to be added.
     */
    public void addPrimitive(Primitive primitive) {
        primitive.setId(new Long(primitives.size()+1));
        primitives.add(primitive);
    }

    /**
     * Adds a new primitive to this drawing.
     * @return the newly created primitive.
     */
    public Primitive createNewPrimitive() {
        Primitive primitive = new Primitive();
        addPrimitive(primitive);
        return primitive;
    }

    /**
     * Inserts a primitive to the drawing at the specified location.
     * @param primitive the primitive to be added.
     * @param index the position in the primitive list to which the primitive
     * will be added.
     * @return false if the location doesn't exist.
     */
    public boolean insertPrimitive(Primitive primitive, int index) {
        if ((index < 0) || (index > primitives.size()))
            return false;

        primitive.setId(new Long(primitives.size()+1));
        primitives.add(index, primitive);
        return true;
    }

    /**
     * Inserts a new primitive at the specified location.
     * @param index the position in the primitive list to which the new
     * primitive will be added.
     * @return the new primitive or null if the position doesn't exist.
     */
    public Primitive insertNewPrimitive(int index) {
        Primitive primitive = new Primitive();
        if (insertPrimitive(primitive, index))
            return primitive;
        else
            return null;
    }

    /**
     * Returns the primitive located at the supplied position.
     * @param index the index of the primitive to return.
     * @return the primitive at the specified position.
     */
    public Primitive getPrimitive(int index) {
        return primitives.get(index);
    }

    /**
     * Returns a list with all primitives of the drawing, sorted according
     * to the x axis.
     * @return all primitives in the drawing, sorted in the x axis.
     */
    public ArrayList<Primitive> getPrimitivesSortedX() {
        ArrayList<Primitive> sortedPrimitives = new ArrayList<Primitive>(primitives);
        Collections.sort(sortedPrimitives, new Comparator<Primitive>() {
            public int compare(Primitive a, Primitive b) {
                return Double.compare(a.getXmin(), b.getXmin());
            }
        });
        return sortedPrimitives;
    }

    /**
     * Returns a list with all primitives of the drawing, sorted by the following
     * criteria...
     * @return all primitives in the drawing, sorted.
     */
    public ArrayList<Primitive> getPrimitivesSorted() {
        // TODO Sandy: (4) A ideia é isto devolver uma lista de primitivas ordenada.
        // Deverá ser muito parecido ao metodo de cima.
        // Temos de discutir os 2.
        return new ArrayList<Primitive>();
    }

    /**
     * Returns the number of primitives in the drawing.
     * @return the number of primitives in the drawing.
     */
    public int getNumPrimitives() {
        return primitives.size();
    }

    /**
     * Simplifies the drawing.
     * by simplifying each primitive
     * then invokes other simplifying functions referring to the whole drawing
     */
    public void simplify(Simplification simplification) {
        // TODO Sandy: (3) Mais simplificação. Ver: drawing.h, drawing.cpp
        double tolerance = simplification.getTolerance();
        double lineTolerance = simplification.getLineTolerance();
        double areaTolerance = simplification.getAreaTolerance();
        double preTolerance = simplification.getPreTolerance();
        double overTolerance = simplification.getOverTolerance();
        int maxCountOptimize = simplification.getMaxCountOptimize();
        int maxVertices = simplification.getMaxVertices();
        double surroundTolDist = simplification.getSurroundTolDist();
        double surroundTolPoints = simplification.getSurroundTolPoints();
        double hueTolerance = simplification.getHueTolerance();
        double saturationTolerance = simplification.getSaturationTolerance();
        double intensityTolerance = simplification.getIntensityToleration();

//        int indexFirst = 0;
//        int indexLast;
//        ArrayList<Point> matchArray = new ArrayList<Point>();

        //ArrayList<Primitive> primitives = drawing.getAllPrimitives();
//        for (int i = 0; i<this.getNumPrimitives(); i++) {
//            Primitive primitive = this.getPrimitive(i);
//            indexLast = primitive.getNumPoints() - 1;
//            primitive.simplify(tolerance, primitive.getPoints(), 0, indexLast, matchArray);
//        }

        for (int i = 0; i < Simplification.SIMPLIFICATION_COUNT; i++) {
            switch (simplification.getOrder()[i]) {
//                case Simplification.REMOVE_SURROUNDING_PRIMITIVES:
//                    removeSurroundingPrimitives(surroundTolDist, surroundTolPoints);
//                    break;
                case Simplification.COLOR_CONCAT_PRIMITIVES:
                    colorConcatPrimitives(preTolerance, overTolerance, maxCountOptimize, maxVertices, hueTolerance, saturationTolerance, intensityTolerance);
                    break;
                case Simplification.REMOVE_SMALL_PRIMITIVES:
                    removeSmallPrimitives(areaTolerance);
                    break;
                case Simplification.REDUCE_VERTEX_COUNT:
                    reduceVertexCount(tolerance, lineTolerance);
                    break;
                case Simplification.NONE:
                    break;
            }
        }
//        if (simplification.isRemoveSurroundingPrimitives())
//            removeSurroundingPrimitives(surroundTolDist, surroundTolPoints);
//        if (simplification.isColorConcatPrimitives())
//            colorConcatPrimitives(preTolerance, overTolerance, maxCountOptimize, maxVertices, hueTolerance, saturationTolerance, intensityTolerance);
//        if (simplification.isRemoveSmallPrimitives())
//            removeSmallPrimitives(areaTolerance);
//        if (simplification.isReduceVertexCount())
//            reduceVertexCount(tolerance, lineTolerance);
    }

    /**
     * Removes Surrounding Primitives of a given drawing
     */
    private void removeSurroundingPrimitives(double surroundTolDist, double surroundTolPoints) {
        ArrayList<Primitive> newListPrimitives = new ArrayList<Primitive>();

        if(this.getNumPrimitives() > 0) {
            //double preTolerance = preTol * this.getDiagonalLength();
            double maxDist = surroundTolDist * this.getDiagonalLength();
            double maxDist2Allowed = Math.pow(maxDist, 2);

            ArrayList<Primitive> excludePrimitives = new ArrayList<Primitive>();

            for (int i=0; i<this.getNumPrimitives(); i++) {
                Primitive prim = this.getPrimitive(i);
                int pointsNeeded = (int) (surroundTolPoints * this.getPrimitive(i).getNumPoints());
                //System.out.println("i =" + i);
                for (int j=i+1; j< this.getNumPrimitives() && j>=0; j--) {
                    //System.out.println("j =" + j);
                    if ((j!=i) && excludePrimitives.contains(this.getPrimitive(j))) {
                        int pointsNeededJ = (int)(surroundTolPoints * this.getPrimitive(j).getNumPoints());
                        int pointsInRange = 0;

                        //loops through all points of i and j and removes add removes polygon i when
                        //all points of polygon j are less far from any point of i then max_dist, i!=j
                        for(int k=0; k<prim.getNumPoints(); k++) {
                            Point pointI = prim.getPoint(k);

                            for(int l=0; l<this.getPrimitive(j).getNumPoints(); l++){
                                Point pointJ = prim.getPoint(l);
                                double dx = pointI.getX() - pointJ.getX();
                                double dy = pointI.getY() - pointJ.getY();
                                double curDist2 = dx + dy;

                                if (curDist2 <= maxDist2Allowed) {
                                    pointsInRange++;
                                    break;
                                }
                                if (pointsInRange >= pointsNeeded) {
                                    break;
                                }
                            } //end l

                            //if there are enough points in range the primitive gets excluded
                            if (pointsInRange >= pointsNeeded) break;
                            else if (pointsInRange >= pointsNeededJ) {
                                // only possible for lines, because we want the upper polygon,
                                //not the outer, because that is probably shadow  (after gradient
                                //color polygon combining)
                                if (!(this.getPrimitive(j).isClosed())) {
                                    //combine bordercolors if needed
                                    if(!this.getPrimitive(i).getBorderColor().isSet()) {
                                        this.getPrimitive(i).setBorderColor(new Color(this.getPrimitive(j).getBorderColor(),
                                                this.getPrimitive(j).getBorderColor().isSet()));
                                    }
                                    excludePrimitives.add(this.getPrimitive(j));
                                }
                            }
                        }
                        if (pointsInRange >= pointsNeeded) {
                            //combine colors if needed
                            if (!this.getPrimitive(j).getBorderColor().isSet()) {
                                this.getPrimitive(j).setBorderColor(new Color(this.getPrimitive(i).getBorderColor(),
                                                this.getPrimitive(i).getBorderColor().isSet()));
                            }
                            if (this.getPrimitive(j).isClosed() && !this.getPrimitive(j).getFillColor().isSet()) {
                                this.getPrimitive(j).setFillColor(new Color(this.getPrimitive(i).getFillColor(),
                                                this.getPrimitive(i).getFillColor().isSet()));
                            }
                            //if the one you keep isn't a polygon(but a polyline), BUT the one you exclude is
                            else if (this.getPrimitive(i).isClosed() && !this.getPrimitive(i).getFillColor().isSet()) {
                                //then exclude the outer polyline, and keep the inner polygon
                                if (!this.getPrimitive(i).getBorderColor().isSet()) {
                                    this.getPrimitive(i).setBorderColor(new Color(this.getPrimitive(j).getBorderColor(),
                                                this.getPrimitive(j).getBorderColor().isSet()));
                                }
                                excludePrimitives.add(this.getPrimitive(j));
                                break;
                            }
                            excludePrimitives.add(this.getPrimitive(i));
                            break;
                        }
                    }
                } //end j cycle
            } //end i cycle

            for (int m = 0; m < this.getNumPrimitives(); m++) {
                Primitive prim = this.getPrimitive(m);
                if (!(excludePrimitives.contains(prim))) {
                    prim.setId(new Long(this.getNumPrimitives()+1));
                    newListPrimitives.add(prim);
                }
            }
            this.setPrimitives(newListPrimitives);
        }
    }


    /** Concatenates primitives with similar fill-color, and cuts-out primitives
     * with different colors or borders reducing primitive count by placing
     * primitives in an exclude list, when its vertices are added to another
     * primitive
     */
    private void colorConcatPrimitives(double preTol, double overTol,
        int maxCountOptimize, int maxVertices,
        double hueTol, double satTol, double intTol) {
        ArrayList<Primitive> newListPrimitives = new ArrayList<Primitive>();
        float[] hsv1 = new float[3];
        float[] hsv2 = new float[3];

        if (this.getNumPrimitives() != 0) {
            //initialization
            int numberToBeRemoved = 0;
            double preTolerance = preTol * this.getDiagonalLength();
            Primitive prim1 = new Primitive();
            Primitive prim2 = new Primitive();

            int countOptimize = 0;

            //search for a primitive which overlaps an already drawn primitive
            for (int i=0; i< this.getNumPrimitives(); i++){
                prim1 = this.getPrimitive(i);

                if(prim1.getNumPoints() == 0) {
                    prim1.setBorderColor(new Color(0, 0, 0, 0, false));
                    prim1.setFillColor(new Color(0, 0, 0, 0, false));
                }

                //this is possible when there are objects inserted after a clipping operation
                if (prim1.getBorderColor().isSet() || prim1.getFillColor().isSet()) {
                    //get already drawn primitives (backwards)
                    for (int j=i-1; j>=0; j--) {
                        prim2 = this.getPrimitive(j);

                        if (prim2.getNumPoints() == 0) {
                            prim2.setBorderColor(new Color(0,0,0,0, false));
                            prim2.setFillColor(new Color(0,0,0,0, false));
                        }

                        //skip test with primitives, which are already excluded
                        if ((prim1.getBorderColor().isSet() || prim1.getFillColor().isSet()) &&
                                (prim2.getBorderColor().isSet() || prim2.getFillColor().isSet())) {

                            //boundingbox test (if the bounding boxes don't collide, then we don't
                            //have to look at the vertices)
                            if (prim1!=prim2 && prim1.collide(prim2)) {
                                //first case: two polygons // lines do still nothing
                                if (prim1.isClosed() && prim2.isClosed()) {
                                    //color tests to eventually combine and exclude j (inner primitive)
                                    if (prim1.getFillColor().isSet() && prim2.getFillColor().isSet()) {
                                        //if bordercolor = fillcolor then remove bordercolor
                                        if ((prim1.getFillColor().equals(prim1.getBorderColor()) &&
                                                (prim1.getFillColor().isSet() && prim1.getBorderColor().isSet())) ||
                                                prim1.getBorderColor().getAlpha() == 0) {
                                            prim1.setBorderColor(new Color(prim1.getBorderColor(), false));
                                        }
                                        if ((prim2.getFillColor().equals(prim2.getBorderColor()) &&
                                                prim2.getFillColor().isSet() && prim2.getBorderColor().isSet()) ||
                                                prim2.getBorderColor().getAlpha() == 0) {
                                            prim2.setBorderColor(new Color(prim2.getBorderColor(), false));
                                        }

                                        //System.out.println("Prim1: " + i + " Prim2: " + j + " (out of " + this.getNumPrimitives() + ")");

                                        //SPEED Pre-optimalisation
                                        if (countOptimize > maxCountOptimize) {
                                            if (prim1.getNumPoints() > maxVertices) {
                                                prim1.polySimplify(preTolerance);
                                                if (prim1.getNumPoints() > maxVertices && overTol > 1) {
                                                    prim1.polySimplify(preTolerance * overTol);
                                                }
                                                countOptimize = 0;
                                            }

                                            if (prim2.getNumPoints() > maxVertices) {
                                                prim2.polySimplify(preTolerance);
                                                if (prim2.getNumPoints() > maxVertices && overTol > 1) {
                                                    prim2.polySimplify(preTolerance * overTol);
                                                }
                                                countOptimize = 0;
                                            }
                                        }
                                        countOptimize++;

                                        // convert rgb to hsv colors
                                        java.awt.Color.RGBtoHSB(prim1.getFillColor().getRed(),
                                                prim1.getFillColor().getGreen(),
                                                prim1.getFillColor().getBlue(),
                                                hsv1);
                                        java.awt.Color.RGBtoHSB(prim2.getFillColor().getRed(),
                                                prim2.getFillColor().getGreen(),
                                                prim2.getFillColor().getBlue(),
                                                hsv2);
                                        //test if color is within range AND there is no border
                                        if (!prim1.getBorderColor().isSet() && !prim2.getBorderColor().isSet() &&
                                                Math.abs(hsv1[0] - hsv2[0])*360 < hueTol &&
                                                Math.abs(hsv1[1] - hsv2[1]) < satTol &&
                                                Math.abs(hsv1[2] - hsv2[2]) < intTol) {

                                            int insertedPrimitives = mergePrimitives(prim1, prim2);
                                            if (insertedPrimitives > 0) {
                                               numberToBeRemoved += 2;

                                               //set original primitives transparent, to excluded them
                                               prim1.setBorderColor(new Color(prim1.getBorderColor(),false));
                                               prim1.setFillColor(new Color(prim1.getFillColor(), false));
                                               prim2.setBorderColor(new Color(prim2.getBorderColor(),false));
                                               prim2.setFillColor(new Color(prim2.getFillColor(),false));

                                               //update p_i to union of i and j
                                               prim1 = this.getPrimitive(i);
                                            }

                                        }
//                                        else { //color not in range or border seperates them
//                                            int insertedPrimitives = diffPrimitives(prim1, prim2);
//                                            numberToBeRemoved += 1;
//
//                                            //set original prim2 transparent, so it gets excluded
//                                            prim2.setBorderColor(new Color(
//                                                    prim2.getBorderColor().getRed(),
//                                                    prim2.getBorderColor().getGreen(),
//                                                    prim2.getBorderColor().getBlue(),
//                                                    0
//                                                    ));
//                                            prim2.setFillColor(new Color(
//                                                    prim2.getFillColor().getRed(),
//                                                    prim2.getFillColor().getGreen(),
//                                                    prim2.getFillColor().getBlue(),
//                                                    0
//                                                    ));
//
//                                            i+=insertedPrimitives;
//                                        }
//                                        if (prim1.getBorderColor() != null && prim2.getBorderColor() != null) {
//
//                                            int insertedPrimitives = mergePrimitives(prim1, prim2);
//                                            if (insertedPrimitives > 0) {
//                                               numberToBeRemoved += 2;
//
//                                               //set original primitives transparent, to excluded them
//                                               prim1.setBorderColor(Color.white);
//                                               prim1.setFillColor(Color.white);
//                                               prim2.setBorderColor(Color.white);
//                                               prim2.setFillColor(Color.white);
//                                            }
//
//                                        }
//                                        else { //border separates primitives
//                                            int insertedPrimitives = diffPrimitives(prim1, prim2);
//                                            numberToBeRemoved += 1;
//
//                                            //set original prim2 transparent, so it gets excluded
//                                            prim2.setBorderColor(Color.white);
//                                            prim2.setFillColor(Color.white);
//
//                                            i+=insertedPrimitives;
//                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //creating new drawing, without primitives which are in the exclude-list
            for (int j=0;j<this.getNumPrimitives(); j++) {
                Primitive currPrimitive = this.getPrimitive(j);
                if (currPrimitive.getBorderColor().isSet() || currPrimitive.getFillColor().isSet()) {
                    newListPrimitives.add(currPrimitive);
                }
            }
        }
        this.setPrimitives(newListPrimitives);
    }

    /**
     * Calculates the difference between two primitives
     *
     */
    public int diffPrimitives(Primitive prim1, Primitive prim2) {
                //make sure that the first primitive actually comes first
        Primitive p_i = prim1, p_j = prim2;
        if (!(prim1.getId() < prim2.getId())) {
            p_i = prim2;
            p_j = prim1;
        }

        // initialization
        int i_count = p_i.getNumPoints();
        int j_count = p_j.getNumPoints();

        int newPrimitives = 0;
        int insertedNewPrimitives = 0;

        // create on polygon for each primitive
        Poly poly1 = new PolyDefault();
        Poly poly2 = new PolyDefault();
        Point tempPoint;

        for (int i = 0; i < i_count; i++) {
            tempPoint = p_i.getPoint(i);
            poly1.add(tempPoint.getX(), tempPoint.getY());
        }
        for (int i = 0; i < j_count; i++) {
            tempPoint = p_j.getPoint(i);
            poly2.add(tempPoint.getX(), tempPoint.getY());
        }

        Color t1b = p_i.getBorderColor();
        Color t1f = p_i.getFillColor();
        Color t2b = p_j.getBorderColor();
        Color t2f = p_j.getFillColor();

        // end of initialization
        // clip polygons
        Poly result = poly2.diff(poly1);

        newPrimitives = result.getNumInnerPoly();

        if (newPrimitives != 0) {
            Primitive p_new;
            boolean foundHole=false;

            //create and add new primitives from result
            for (int j=0; j<newPrimitives; j++) {
                //set the new object if it isn't a hole
                boolean isHole=(result.getNumInnerPoly()>1)?false:result.isHole();

                if(!isHole) { //if (int_array.frompointer(result.getHole()).getitem(j) == 0) {
                    //create a new primitive which contains the result and insert it at the bottom
                    p_new = this.insertNewPrimitive(this.primitives.indexOf(p_j));//pr_j + insertedNewPrimitives);
                    insertedNewPrimitives++;
                    assert(p_new!=null);

                    //when creating (a) new primitive(s), let it contain all primitives that p_j contained (doesn't matter that this isn't always true, it is used to reset the height of the contained objects)

                    p_new.setBorderColor(new Color(t2b.getRed(),t2b.getGreen(),t2b.getBlue(),t2b.getAlpha(), t2b.isSet()));
                    p_new.setFillColor(new Color(t2f.getRed(),t2f.getGreen(),t2f.getBlue(),t2f.getAlpha(), t2f.isSet()));

                    //create the polygon
                    for(int i=0; i<result.getNumPoints(); i++) {
                        //gpc_vertex vertex=gpc_vertex_array.frompointer(contour.getVertex()).getitem(i);
                        p_new.addPoint(result.getX(i),result.getY(i));
                    }
                    if (result.getNumPoints() != 0) {
                        //gpc_vertex vertex=gpc_vertex_array.frompointer(contour.getVertex()).getitem(0);
                        p_new.addPoint(result.getX(0),result.getY(0));
                    }

                    //confirm it's a polygon
                    p_new.setClosed(p_i.isClosed());
                } else {
                    //if it is a hole
                    foundHole = true;

                    p_new = this.insertNewPrimitive(this.primitives.indexOf(p_j) + insertedNewPrimitives+1);
                    insertedNewPrimitives++;
                    assert(p_new!=null);

                    //when creating (a) new primitive(s), let it contain all primitives that p_j contained (doesn't matter that this isn't always true, it is used to reset the height of the contained objects)

                    p_new.setBorderColor(new Color(t2b.getRed(),t2b.getGreen(),t2b.getBlue(),t2b.getAlpha(), t2b.isSet()));
                    p_new.setFillColor(new Color(255,255,255,255, true));

                    //create the polygon
                    for(int i=0; i<result.getNumPoints(); i++) {
                        p_new.addPoint(result.getX(i),result.getY(i));
                    }
                    if (result.getNumPoints() != 0) {
                        p_new.addPoint(result.getX(0),result.getY(0));
                    }

                    //confirm it's a polygon
                    p_new.setClosed(p_i.isClosed());
                }

            } // end for

        }
        return insertedNewPrimitives;
    }

    /**
     * Merges two primitives
     *
     */
    private int mergePrimitives(Primitive prim1, Primitive prim2) {
        //make sure that the first primitive actually comes first
        Primitive p_i = prim1, p_j = prim2;
        if (!(prim1.getId() < prim2.getId())) {
            p_i = prim2;
            p_j = prim1;
        }

//        if (p_i.getSvgId() == "path3792" || p_j.getSvgId() == "path3792")
//            System.err.println("test");

        // initialization
        int p_isize = p_i.getNumPoints();
        int p_jsize = p_j.getNumPoints();

        int newPrimitives = 0;
        int insertedNewPrimitives = 0;
        boolean containsHole = false;

        // create on polygon for each primitive
        Poly poly1 = new PolyDefault();
        Poly poly2 = new PolyDefault();
        Point tempPoint;

        for (int i = 0; i < p_isize; i++) {
            tempPoint = p_i.getPoint(i);
            poly1.add(tempPoint.getX(), tempPoint.getY());
        }
        for (int i = 0; i < p_jsize; i++) {
            tempPoint = p_j.getPoint(i);
            poly2.add(tempPoint.getX(), tempPoint.getY());
        }

        Color t1b = p_i.getBorderColor();
        Color t1f = p_i.getFillColor();
        Color t2b = p_j.getBorderColor();
        Color t2f = p_j.getFillColor();

        // end of initialization
        // clip polygons (union = merge)
        Poly result = poly1.union(poly2);
        newPrimitives = result.getNumInnerPoly();

        Primitive p_new;

        //create and add new primitives from result
        for (int j = 0; j<newPrimitives; j++) {
            //if it isn't an hole, but a unified part
            boolean isHole=(result.getNumInnerPoly()>1)?false:result.isHole();
            if(!isHole) { //if (int_array.frompointer(result.getHole()).getitem(j) == 0) {
                //create a new primitive and insert it at location of the lowest polygon
                p_new = this.insertNewPrimitive(this.primitives.indexOf(p_j)); //pr_i
                insertedNewPrimitives++;
                assert(p_new!=null);

                //give it the avarage fill color of the original
                if (t2f.isSet()) {
                    p_new.setBorderColor(new Color((t1b.getRed() + t2b.getRed()) / 2,(t1b.getGreen() + t2b.getGreen()) / 2,(t1b.getBlue() + t1b.getBlue()) /2, (t1b.getAlpha() + t2b.getAlpha())/2, t1b.isSet()));
                    p_new.setFillColor(new Color((t1f.getRed() + t2f.getRed()) / 2,(t1f.getGreen() + t2f.getGreen()) / 2,(t1f.getBlue() + t1f.getBlue()) /2,(t1f.getAlpha()+t2f.getAlpha())/2, t1f.isSet()));
                } else {
                    p_new.setBorderColor(new Color(t1b.getRed(),t1b.getGreen(),t1b.getBlue(),t1b.getAlpha(), t1b.isSet()));
                    p_new.setFillColor(new Color(t1f.getRed(),t1f.getGreen(),t1f.getBlue(),t1f.getAlpha(), t1f.isSet()));
                }

            } else {
                //when it is a hole, add it at the top and make it white
                p_new = this.insertNewPrimitive(this.primitives.indexOf(p_i) + insertedNewPrimitives);
                assert(p_new!=null);

                p_new.setBorderColor(new Color(t1b.getRed(),t1b.getGreen(),t1b.getBlue(),t1b.getAlpha(), t1b.isSet()));
                p_new.setFillColor(new Color(255,255,255,255, true));
            }

            //create the polygon
            for(int i=0; i<result.getNumPoints(); i++) {
                //gpc_vertex vertex=gpc_vertex_array.frompointer(contour.getVertex()).getitem(i);
                p_new.addPoint(result.getX(i),result.getY(i));
            }
            if (result.getNumPoints() != 0) {
                //gpc_vertex vertex=gpc_vertex_array.frompointer(contour.getVertex()).getitem(0);
                p_new.addPoint(result.getX(0),result.getY(0));
            }

            //confirm it's a polygon
            p_new.setClosed(p_i.isClosed());

        }
        return insertedNewPrimitives;
    }

    /** Removes small primitives in the drawing
     *
     */
    private void removeSmallPrimitives(double areaTolerance) {
        ArrayList<Primitive> newListPrimitives = new ArrayList<Primitive>();

        if (this.getNumPrimitives() != 0) {
            int size = this.getNumPrimitives();

            //based on percentage of the total area
            double thresholdSize = areaTolerance * this.getArea();

            //optimize the drawing by removing small primitives
	    for (int i = 0; i < size; i++) {
                Primitive p = this.getPrimitive(i);
                // check if the primitive isn't invisible
                if ((p.getFillColor()).isSet() || (p.getBorderColor().isSet())){
                    //add to new list, when there isn't an areaSize or when the areaSize is above the threshold
                    if ((p.getAreaSize() == -1) || (p.getAreaSize() > thresholdSize)) {
                        p.setId(new Long(newListPrimitives.size()+1));
                        newListPrimitives.add(p);
                    }
                    else {
                        newListPrimitives.remove(p);
                    }
                }
            }

            this.setPrimitives(newListPrimitives);
        }
    }

    /** Remove vertices from all primitives, which distance between each other are too small
     * if 2 edges are left and the distance between each other is to small too,
     * then the whole primitive is removed
     */
    private void reduceVertexCount(double v_tolerance, double l_tolerance) {
        ArrayList<Primitive> newListPrimitives = new ArrayList<Primitive>();
        int indexFirst = 1;
        int indexLast = 0;
        ArrayList<Point> matchArray = new ArrayList<Point>();
        int drawingSize = this.getNumPrimitives();

        if (drawingSize != 0){

            double relativeTolerance = v_tolerance * this.getDiagonalLength();
            double relMinLength = l_tolerance * this.getDiagonalLength();
            Primitive p = new Primitive();

            //optimize all primitives by removing some small primitives and small lines withing primitives
            for (int i=0; i < drawingSize; i++) {
                p = this.getPrimitive(i);
                indexLast = p.getNumPoints() - 1;
                p.polySimplify(relativeTolerance);

                //add to new list, when there are still points left and in case of lines, the total length of a line still is bigger then the threshold
                if (p.getNumPoints() > 0 ) {
                    if (p.isClosed() || (p.getPerimeter() > relMinLength) ) {
                        p.setId(new Long(newListPrimitives.size() + 1));
                        newListPrimitives.add(p);
                    }
                    else {
                        newListPrimitives.remove(p);
                    }
                }
            }

            this.setPrimitives(newListPrimitives);
        }
    }

    /**
     * Returns a string representation of the drawing.
     * @return a string representation of the drawing.
     */
    @Override
    public String toString(){
        StringBuilder stringBuffer = new StringBuilder();
        Primitive p;
        stringBuffer.append("Drawing with id: ").append(id).append(System.getProperty("line.separator"));
	for (int i = 0; i < primitives.size(); i++) {
		p = primitives.get(i);
                stringBuffer.append(p.toString()).append(System.getProperty("line.separator"));
	}
	return stringBuffer.toString();
    }

    /**
     * TODO Gabe: documentar
     * @param path
     * @throws IOException
     */
    public void export(String path) throws IOException {
        double preferredWidth = 640;
        double preferredHeight = 640;

        double dw = getWidth();
        double dh = getHeight();
        if (dw <= 0) dw = 1;
        if (dh <= 0) dh = 1;
        double pw = preferredWidth;
        double ph = preferredHeight;
        boolean isLandscape = (dw >= dh ? true : false);
        double xlim;
        double ylim;
        if (isLandscape) {
            xlim = pw; //(dw > pw ? pw : dw);
            ylim = xlim * (dh/dw);
        } else {
            ylim = ph; //(dh > ph ? ph : dh);
            xlim = ylim * (dw/dh);
        }

        double xratio = xlim/dw;
        double yratio = ylim/dh;

        BufferedImage bi = new BufferedImage((int)xlim, (int)ylim, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        draw(ig2, xratio, yratio);
        ImageIO.write(bi, "PNG", new File(path + ".png"));
    }

    /**
     * TODO Gabe: documentar
     * @param g2d
     * @param xratio
     * @param yratio
     */
    //TO DO - uncomment the setColor parts to complete the code!
    public void draw(Graphics2D g2d, double xratio, double yratio) {
        g2d.setBackground(java.awt.Color.WHITE);
        Polygon polygon;
        Color fill, stroke;
        float alpha;
        double xmin = getXmin();
        double ymin = getYmin();

        for (Primitive primitive : getAllPrimitives()) {
            g2d.setStroke(new BasicStroke(new Double(primitive.getBorderWidth()).floatValue()));
            fill = primitive.getFillColor();
            stroke = primitive.getBorderColor();
            if (primitive.isClosed()) { // it's a polygon
                polygon = new Polygon();
                for (Point point : primitive.getPoints()) {
                    polygon.addPoint((int) ((point.getX() - xmin) * xratio) , (int) ((point.getY() - ymin) * yratio));
                }
                g2d.setColor(new java.awt.Color(fill.getRed(), fill.getGreen(), fill.getBlue()));
                alpha = (float) fill.getAlpha() / 255.0f;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.fillPolygon(polygon);
                alpha = (float) stroke.getAlpha() / 255.0f;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.setColor(new java.awt.Color(stroke.getRed(), stroke.getGreen(), stroke.getBlue()));
                g2d.drawPolygon(polygon);
            }
            else { // it's a polyline
                ArrayList<Point> points = primitive.getPoints();
                int len = points.size();
                Point point;
                int[] xpoints = new int[len];
                int[] ypoints = new int[len];
                for (int i = 0; i < len; i++) {
                    point = points.get(i);
                    xpoints[i] = (int)((point.getX() - xmin) * xratio);
                    ypoints[i] = (int)((point.getY() - ymin) * yratio);
                }
                Polygon p = new Polygon(xpoints, ypoints, len);
                g2d.setColor(new java.awt.Color(fill.getRed(), fill.getGreen(), fill.getBlue()));
                alpha = fill.getAlpha(); //already in the [0,1] interval
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.fillPolygon(p);
                alpha = stroke.getAlpha();  //already in the [0,1] interval
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2d.setColor(new java.awt.Color(stroke.getRed(), stroke.getGreen(), stroke.getBlue()));
                g2d.drawPolyline(xpoints, ypoints, len);
            }
        }
    }

    @Override
    public Drawing clone() {
        Drawing drawing = new Drawing(this.getId());
        ArrayList<Primitive> prims = this.getAllPrimitives();
        ArrayList<Primitive> newPrims = new ArrayList<Primitive>();
        for (Primitive prim : prims)
            newPrims.add(prim.clone());
        drawing.setPrimitives(newPrims);
        return drawing;
    }
}
