package pt.inevo.encontra.drawing.descriptors.test;

import junit.framework.TestCase;
import org.junit.Test;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.descriptors.Topogeo.DrawingFactory;
import pt.inevo.encontra.drawing.descriptors.TopogeoDescriptor;
import pt.inevo.encontra.drawing.descriptors.TopogeoDescriptorExtractor;
import pt.inevo.encontra.index.IndexedObject;

import java.io.IOException;


public class DescriptorExtractorsTest extends TestCase {

    Drawing drawing;
    String testFilePath;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testFilePath = getClass().getResource("/radioactive.svg").getPath();
        drawing = new Drawing(testFilePath);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    @Test
    public void testTopogeoExtractor() throws IOException {
        IndexedObject<Long, pt.inevo.encontra.drawing.descriptors.Topogeo.Drawing> idx = new IndexedObject<Long, pt.inevo.encontra.drawing.descriptors.Topogeo.Drawing>();
        idx.setName("image");
        idx.setId(new Long(1));

        TopogeoDescriptorExtractor extractor = new TopogeoDescriptorExtractor();
        pt.inevo.encontra.drawing.descriptors.Topogeo.Drawing dr = DrawingFactory.getInstance().drawingFromSVG(testFilePath);
        dr.setId(idx.getId().intValue());
        idx.setValue(dr);

        TopogeoDescriptor descriptor = extractor.extract(idx);

        assertNotNull(descriptor);
        Double [] values = descriptor.getValues();
        System.out.print("Topogeo descriptor: ");
        for (int i = 0; i < descriptor.size(); i++){
            System.out.print(values[i] + " ");
        }
        System.out.println();
    }

    @Test
    public void testTopologyExtractor() {
        /*
        TopologyDescriptorExtractor extractor=new TopologyDescriptorExtractor();
        List<TopologyDescriptor> descriptors=extractor.extract(new DrawingTopology(drawing));
        assertFalse(descriptors.isEmpty());*/
    }

    @Test
    public void testGeometryExtractor() {
        /*
        GeometryDescriptorExtractor<DrawingGeometry> extractor=new GeometryDescriptorExtractor<DrawingGeometry>();
        List<GeometryDescriptor> descriptors=extractor.extract(new DrawingGeometry(drawing));
        assertFalse(descriptors.isEmpty()); */
    }
}
