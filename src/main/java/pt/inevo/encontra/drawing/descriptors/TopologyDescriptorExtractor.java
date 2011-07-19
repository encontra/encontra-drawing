package pt.inevo.encontra.drawing.descriptors;

import pt.inevo.encontra.descriptors.MultiDescriptorExtractor;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.index.Vector;

import java.util.ArrayList;
import java.util.List;

public class TopologyDescriptorExtractor extends MultiDescriptorExtractor<IndexedObject<Long, DrawingTopology>, TopologyDescriptor> {

    @Override
    protected List<TopologyDescriptor> extractDescriptors(IndexedObject<Long, DrawingTopology> obj) {
        TopologyDescriptor descriptor;
        List<TopologyDescriptor> descriptors=new ArrayList<TopologyDescriptor>();
        DrawingTopology topo = obj.getValue();
        List<Vector> mDescritors = topo.getValue();
        for(Vector vector:mDescritors){
            descriptor=new TopologyDescriptor();
            descriptor.setId(obj.getId());
            descriptor.setValue(vector);
            descriptors.add(descriptor);
        }
        return descriptors;
    }

}
