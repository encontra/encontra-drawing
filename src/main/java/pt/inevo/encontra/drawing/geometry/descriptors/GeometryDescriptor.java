package pt.inevo.encontra.drawing.geometry.descriptors;


import pt.inevo.encontra.common.distance.DistanceMeasure;
import pt.inevo.encontra.common.distance.EuclideanDistanceMeasure;
import pt.inevo.encontra.common.distance.SquaredEuclideanDistanceMeasure;
import pt.inevo.encontra.descriptors.Descriptor;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.index.Vector;

import java.io.Serializable;

public class GeometryDescriptor<O extends Drawing>  extends Vector<Double> implements Descriptor {

    protected Serializable id;

    private DistanceMeasure distanceMeasure=new EuclideanDistanceMeasure();

    public GeometryDescriptor(){
        super(Double.class,0);
    }

    @Override
    public String getName() {
        return "GeometryDescriptor";
    }

    @Override
    public double getDistance(Descriptor other) {
        return distanceMeasure.distance(this,other);
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
        this.id=id;
    }
}
