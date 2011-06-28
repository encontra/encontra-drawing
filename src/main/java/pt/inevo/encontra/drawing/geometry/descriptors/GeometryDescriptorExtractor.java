package pt.inevo.encontra.drawing.geometry.descriptors;


import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import pt.inevo.encontra.descriptors.MultiDescriptorExtractor;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.Primitive;
import pt.inevo.encontra.drawing.descriptors.DrawingTopology;
import pt.inevo.encontra.drawing.descriptors.TopologyDescriptor;
import pt.inevo.encontra.drawing.geometry.CIGeometric;
import pt.inevo.encontra.graph.Graph;
import pt.inevo.encontra.graph.GraphNode;
import pt.inevo.encontra.graph.swing.GraphViewer;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.index.Vector;
import pt.inevo.jcali.CIPoint;

import java.util.ArrayList;
import java.util.List;

public class GeometryDescriptorExtractor extends MultiDescriptorExtractor<IndexedObject<Long, DrawingGeometry>, GeometryDescriptor> {

    @Override
    protected List<GeometryDescriptor> extractDescriptors(IndexedObject<Long, DrawingGeometry> obj) {
        GeometryDescriptor descriptor;
        List<GeometryDescriptor> descriptors=new ArrayList<GeometryDescriptor>();
        DrawingGeometry geom=obj.getValue();
        List<Vector> mDescritors = geom.getValue();
        for(Vector vector:mDescritors){
            descriptor=new GeometryDescriptor();
            descriptor.setId(obj.getId());
            descriptor.setValue(vector);
            descriptors.add(descriptor);
        }
        return descriptors;
    }



}
