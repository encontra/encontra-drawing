package pt.inevo.encontra.drawing.descriptors;

import pt.inevo.encontra.descriptors.DescriptorExtractor;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.index.IndexedObject;

/**
 * Extractor for the TopogeoDescriptor.
 */
public class TopogeoDescriptorExtractor extends DescriptorExtractor<IndexedObject<Long, Drawing>, TopogeoDescriptor> {

    @Override
    protected IndexedObject<Long, Drawing> setupIndexedObject(TopogeoDescriptor descriptor, IndexedObject<Long, Drawing> object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TopogeoDescriptor extract(IndexedObject<Long, Drawing> object) {

//        MatrixFactory.getInstance().setUseGeometry(false);
//        Drawing drawing = DrawingFactory.getInstance().drawingFromSVG(path);
//        TopogeoGraph graph = GraphFactory.getInstance().generateTopogeoGraph(drawing);

//        TopogeoDescriptor descriptor = DescriptorFactory.getInstance().generateDescriptors(graph).get(0);

        Drawing drawing = object.getValue();




        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
