package pt.inevo.encontra.drawing.geometry.descriptors;


import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.Primitive;
import pt.inevo.encontra.drawing.geometry.CIGeometric;
import pt.inevo.encontra.index.Vector;
import pt.inevo.encontra.storage.IEntry;

import java.util.ArrayList;
import java.util.List;

public class DrawingGeometry implements IEntry<Long,List<Vector>> {

    public static String PATH="geometry";

    Long id;
    List<Vector> geometry;

    public DrawingGeometry(Drawing drawing){
        super();
        setId(drawing.getId());
        setValue(getGeometry(drawing));
    }

    public List<Vector> getGeometry(Drawing drawing) {
        /*
           * Global approach:
           * - compute the geometrical descriptors of each of the Primitives
           *   for each Primitive
           *   - add points to a Geometry object
           *   - use Geometry to compute descriptor
           *   - convert [] to HDPoint
           *   - store HDPoint in vector
           *
           * - return the vector
           */

        // Vector that will be returned
        List<Vector> retVal = new ArrayList<Vector>();

        // Get the Primives and the number of Primitives for the Drawing
        List<Primitive> lst_primitives = drawing.getAllPrimitives();
        long n_prims = lst_primitives.size();

        // Create a new Geometry object
        CIGeometric geom =  new CIGeometric ();

        int i;
        for(i = 0; i < n_prims; i++) {
            int num_points = lst_primitives.get(i).getNumPoints();

            if ( num_points > 0 ) {

                geom.newScribble();
                geom.newStroke();

                Primitive p = lst_primitives.get(i);

                // Add all the Primitive's points to the CIScribble.
                int j;
                for(j = 0; j < num_points; j++) {
                    double x = p.getPoint(j).x;
                    double y = p.getPoint(j).y;

                    geom.addPoint(x,y);
                }

                /* Calculate the geometric features for this Primitive */
                /* fvector with the geometrical features
                     * the dimension of the vector is on the first position */
                ArrayList <Double> fvector = geom.geometricFeatures();
                Vector vector = new Vector(Double.class,fvector.size());
                for(int v=0;v<fvector.size();v++){
                    vector.set(v,fvector.get(v));
                }


                retVal.add(vector);

                /* fvector does not need to be deleted. it is a pointer to a member
                     * of CIGeometry. */
            }
        }

        // Cleanup
        //delete g;

        // Return values
        return retVal;
    }

    @Override
    public List<Vector> getValue() {
        return geometry;
    }

    @Override
    public void setValue(List<Vector> o) {
        this.geometry=o;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
       this.id=id;
    }
}
