package pt.inevo.encontra.drawing.descriptors.test;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.DrawingFactory;
import pt.inevo.encontra.drawing.descriptors.TopogeoDescriptor;
import pt.inevo.encontra.drawing.descriptors.TopogeoDescriptorExtractor;
import pt.inevo.encontra.index.IndexedObject;

import java.io.FileReader;
import java.io.IOException;


public class DescriptorExtractorsTest extends TestCase {

    Drawing drawing;
    String testFilePath;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testFilePath = getClass().getResource("/classic_house.svg").getPath();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

    }

    @Test
    public void testTopogeoExtractor() throws IOException {
        //Descriptor for the classic_house.svg
        Double [] expectedResult = {0.1556270718415356,0.10299619928922851,0.03316326946744761,
                0.031596915438982645,0.02302202008250275,0.01921865617272827,
                0.014542870559078854,0.0045798832450853,0.0011391450453826892};

        IndexedObject<Long, Drawing> idx = new IndexedObject<Long, Drawing>();
        idx.setName("image");
        idx.setId(new Long(1));

        //creating the drawing from the svg file
        drawing = DrawingFactory.getInstance().drawingFromSVG(new FileReader(testFilePath));
        drawing.show();
        drawing.setId(idx.getId());
        idx.setValue(drawing);

        TopogeoDescriptorExtractor extractor = new TopogeoDescriptorExtractor();
        TopogeoDescriptor descriptor = extractor.extract(idx);

        assertNotNull(descriptor);
        Double [] values = descriptor.getValues();

        Assert.assertArrayEquals(expectedResult, values);
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
