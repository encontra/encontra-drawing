package pt.inevo.encontra.drawing.descriptors;


import pt.inevo.encontra.common.distance.DistanceMeasure;
import pt.inevo.encontra.common.distance.EuclideanDistanceMeasure;
import pt.inevo.encontra.descriptors.Descriptor;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.index.Vector;

import java.io.Serializable;

public class TopologyDescriptor<O extends Drawing> extends Vector<Double> implements Descriptor {

    protected Serializable id;

//    public static class EuclideanDistance implements DistanceMeasure<TopologyDescriptor>{
//
//        @Override
//        public double distance(TopologyDescriptor t1, TopologyDescriptor t2) {
//            double tmp, dist;
//            int i;
//            Vector<Double> v1, v2;
//
//            dist=0;
//            if (t1.size() >= t2.size()) {
//                v1 = t1;
//                v2 = t2;
//            }
//            else {
//                v1 = t2;
//                v2 = t1;
//            }
//
//            int d1 = v1.size();
//            int d2 = v2.size();
//
//
//            for (i=0; i < d2; i++) {
//                tmp = v1.get(i) - v2.get(i);
//                dist += tmp * tmp;
//            }
//            for (i= d2; i < d1; i++)
//                dist += Math.pow(v1.get(i),2);
//
//
//            return Math.sqrt(dist);
//        }
//
//        @Override
//        public double distance(double centroidLengthSquare, TopologyDescriptor centroid, TopologyDescriptor v) {
//            return 0;  //To change body of implemented methods use File | Settings | File Templates.
//        }
//    }
    private DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

    public TopologyDescriptor(){
        super(Double.class,0);
    }

    @Override
    public String getName() {
        return "TopologyDescriptor";
    }

    @Override
    public double getDistance(Descriptor d) {
        TopologyDescriptor other = (TopologyDescriptor) d;
        return distanceMeasure.distance(this,other);
    }

    @Override
    public double getNorm() {
        return super.norm(2);
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
