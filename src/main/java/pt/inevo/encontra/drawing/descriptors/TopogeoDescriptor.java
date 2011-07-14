package pt.inevo.encontra.drawing.descriptors;

import pt.inevo.encontra.common.distance.DistanceMeasure;
import pt.inevo.encontra.common.distance.EuclideanDistanceMeasure;
import pt.inevo.encontra.descriptors.Descriptor;
import pt.inevo.encontra.index.Vector;

import java.io.Serializable;

/**
 * TopogeoDescriptor. Adapted from the Crush Project.
 */
public class TopogeoDescriptor extends Vector<Double> implements Descriptor {

    protected Serializable id;
    protected DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

    public TopogeoDescriptor() {
        super(Double.class,0);
    }

    @Override
    public String getName() {
        return "TopoGeoDescriptor";
    }

    @Override
    public double getNorm() {
        return super.norm(2);
    }

    @Override
    public double getDistance(Descriptor other) {
        return distanceMeasure.distance(this, other);
    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public void setValue(Object o) {
        Vector<Double> val =  (Vector<Double>)o;
        this.setValues(val.getValues());
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public void setId(Serializable id) {
        this.id = id;
    }
}

