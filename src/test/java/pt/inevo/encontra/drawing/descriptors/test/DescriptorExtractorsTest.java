package pt.inevo.encontra.drawing.descriptors.test;

import junit.framework.TestCase;
import org.junit.Test;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.DrawingFactory;
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
//        testFilePath = getClass().getResource("/radioactive.svg").getPath();
        testFilePath = getClass().getResource("/classic_house.svg").getPath();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    @Test
    public void testTopogeoExtractor() throws IOException {
        //From the Gabriel Code - radioactive.svg
        Double [] expectedResult = {0.181,0.101,0.033,0.020,0.020,0.020,0.013};

        IndexedObject<Long, Drawing> idx = new IndexedObject<Long, Drawing>();
        idx.setName("image");
        idx.setId(new Long(1));

        //creating the drawing from the svg file
        Drawing drawing = DrawingFactory.getInstance().drawingFromSVG(testFilePath);
        drawing.setId(idx.getId());
        idx.setValue(drawing);

        TopogeoDescriptorExtractor extractor = new TopogeoDescriptorExtractor();
        TopogeoDescriptor descriptor = extractor.extract(idx);

        assertNotNull(descriptor);
        Double [] values = descriptor.getValues();
        System.out.print("Topogeo descriptor: ");
        for (int i = 0; i < descriptor.size(); i++){
            System.out.print(values[i] + " ");
        }
        System.out.println();

        for (;;) {

        }

//        Assert.assertArrayEquals(expectedResult, values);
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
