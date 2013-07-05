package pt.inevo.encontra.drawing.descriptors;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import org.apache.commons.lang.ArrayUtils;
import pt.inevo.encontra.descriptors.DescriptorExtractor;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.descriptors.Topogeo.TopogeoGraph;
import pt.inevo.encontra.drawing.descriptors.Topogeo.TopogeoNode;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.index.Vector;

import java.util.*;

/**
 * Extractor for the TopogeoDescriptor.
 */
public class TopogeoDescriptorExtractor extends DescriptorExtractor<IndexedObject<Long, Drawing>, TopogeoDescriptor> {

    private int MAXVAL = 50; // TODO: fix normalization

    @Override
    protected IndexedObject<Long, Drawing> setupIndexedObject(TopogeoDescriptor descriptor, IndexedObject<Long, Drawing> object) {
        object.setId((Long) descriptor.getId());
        return object;
    }

    @Override
    public TopogeoDescriptor extract(IndexedObject<Long, Drawing> object) {
        Drawing drawing = object.getValue();

        //generate the graph and calculate the descriptor
        TopogeoGraph graph = generateTopogeoGraph(drawing);
        graph.show();
        DoubleMatrix2D matrix = generateMatrix(graph);
        double[] descriptorD = generateDescriptor(matrix);

        //set the descriptor (as a vector)
        TopogeoDescriptor descriptor = new TopogeoDescriptor();
        descriptor.setId(drawing.getId());
        Vector<Double> d = new Vector<Double>(Double.class, descriptorD.length);
        d.setValues(ArrayUtils.toObject(descriptorD));

        descriptor.setValue(d);

        return descriptor;
    }

    /**
     * Generates a Topogeo graph based on a drawing
     *
     * @param drawing the drawing to be used to create the graph
     * @return a Topogeo Graph
     */
    private TopogeoGraph generateTopogeoGraph(Drawing drawing) {
        //drawing.show();

        // SVG -> Topogeo Graph.
        TopogeoGraph graph = new TopogeoGraph(drawing.getId());

        // We get an ordered collection of primitive nodes!
        Object[] nodes = graph.initialize(drawing).toArray();

        TopogeoNode a, b;

        for (int i = 0; i < nodes.length - 1; i++) {
            a = (TopogeoNode) nodes[i];
            if (a.getPrimitive().getId().equals(TopogeoNode.FEATURE_NODE))
                continue;
            for (int j = i + 1; j < nodes.length; j++) {
                b = (TopogeoNode) nodes[j];
                if (b.getPrimitive().getId().equals(TopogeoNode.FEATURE_NODE))
                    continue;
                if (a.getPrimitive().isInPrimitive(b.getPrimitive()))
                    graph.setParent(a, b);
                else if (b.getPrimitive().isInPrimitive(a.getPrimitive()))
                    graph.setParent(b, a);
            }
        }

        return graph;
    }

    /**
     * Generates a Matrix from the Topogeo Graph.
     *
     * @param graph the graph to be used to create the matrix
     * @return a matrix representing the graph
     */
    private DoubleMatrix2D generateMatrix(TopogeoGraph graph) {
        // Topogeo Graph -> Matrix
        List<TopogeoNode> nodes = new ArrayList(graph.getVertices());
        int matSize = nodes.size();
        double[][] mat = new double[matSize][matSize];
        HashMap<TopogeoNode, Integer> indexMap = new HashMap<TopogeoNode, Integer>();

        for (int i = 0; i < matSize; i++)
            indexMap.put(nodes.get(i), i);

        TopogeoNode currentNode, theOtherNode;
        int currentIndex, theOtherIndex;
        double theValue;
        List<TopogeoNode> includedNodes, adjacentNodes;

        for (int i = 0; i < matSize; i++) {
            currentNode = nodes.get(i);
            currentIndex = indexMap.get(currentNode);
            includedNodes = currentNode.getChildren();
            adjacentNodes = currentNode.getSiblings();


            for (int j = 0; j < includedNodes.size(); j++) {
                theOtherNode = includedNodes.get(j);
                theOtherIndex = indexMap.get(theOtherNode);
                //using geometry, because the useGeometry property doesn't exists here
                theValue = graph.findParentalEdge(theOtherNode, currentNode).getValue();
                mat[currentIndex][theOtherIndex] = theValue;
                mat[theOtherIndex][currentIndex] = theValue;
            }

            for (int j = 0; j < adjacentNodes.size(); j++) {
                theOtherNode = adjacentNodes.get(j);
                theOtherIndex = indexMap.get(theOtherNode);
                //using distance, because the useDistance property doesn't exists here
                theValue = graph.findAdjacencyEdge(currentNode, theOtherNode).getValue();
                mat[currentIndex][theOtherIndex] = theValue;
                mat[theOtherIndex][currentIndex] = theValue;
            }

            if (currentNode.getPrimitive().getId() != TopogeoNode.ROOT_NODE &&
                    currentNode.getPrimitive().getId() != TopogeoNode.FEATURE_NODE) {
                for (int j = 0; j < TopogeoGraph.featureNodes.length; j++) {
                    theOtherNode = TopogeoGraph.featureNodes[j];
                    theOtherIndex = indexMap.get(theOtherNode);
                    theValue = graph.findFeatureEdge(currentNode, theOtherNode).getValue();
                    mat[currentIndex][theOtherIndex] = theValue;
                    mat[theOtherIndex][currentIndex] = theValue;
                }
            }
        }

        DoubleMatrix2D result = DoubleFactory2D.sparse.make(mat);
        return result;
    }

    /**
     * Generates a topogeo descriptor with its internal description.
     *
     * @param matrix the matrix to be used to generate the descriptor
     * @return a double array that represents the topogeo descriptor
     */
    private double[] generateDescriptor(DoubleMatrix2D matrix) {
        EigenvalueDecomposition eig_decomp = new EigenvalueDecomposition(matrix);
        // computes eigen values
        double[] eig_vals = eig_decomp.getRealEigenvalues().toArray();
        // make all the values absolute
        for (int i = 0; i < eig_vals.length; i++) {
            if (eig_vals[i] < 0)
                eig_vals[i] = Math.abs(eig_vals[i]);
            eig_vals[i] /= MAXVAL;
        }
        // sorts eigen values in descending order so that values carrying
        // more important information come first
        Arrays.sort(eig_vals);

        int trim_index = 0;
        for (int i = 0; i < eig_vals.length; i++) {
            if (eig_vals[i] > 0.001) {
                trim_index = i;
                break;
            }
        }
        double[] trim_array = new double[eig_vals.length - trim_index];
        System.arraycopy(eig_vals, trim_index, trim_array, 0, trim_array.length);
        eig_vals = trim_array;

        for (int left = 0, right = eig_vals.length - 1; left < right; left++, right--) {
            // exchange the first and last
            double temp = eig_vals[left];
            eig_vals[left] = eig_vals[right];
            eig_vals[right] = temp;
        }
        return eig_vals;
    }
}
