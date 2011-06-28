package pt.inevo.encontra.drawing.descriptors.test;

import junit.framework.TestCase;
import org.junit.Test;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.descriptors.DrawingTopology;
import pt.inevo.encontra.drawing.descriptors.TopologyDescriptor;
import pt.inevo.encontra.drawing.descriptors.TopologyDescriptorExtractor;
import pt.inevo.encontra.drawing.geometry.descriptors.DrawingGeometry;
import pt.inevo.encontra.drawing.geometry.descriptors.GeometryDescriptor;
import pt.inevo.encontra.drawing.geometry.descriptors.GeometryDescriptorExtractor;

import java.util.List;


public class DescriptorExtractorsTest  extends TestCase {

    Drawing drawing;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String testFilePath = getClass().getResource("/radioactive.svg").getPath();
        drawing=new Drawing(testFilePath);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

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
