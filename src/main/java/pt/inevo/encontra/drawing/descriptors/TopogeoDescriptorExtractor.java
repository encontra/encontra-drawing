package pt.inevo.encontra.drawing.descriptors;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import org.apache.commons.lang.ArrayUtils;
import pt.inevo.encontra.descriptors.DescriptorExtractor;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.descriptors.Topogeo.TopogeoGraph;
import pt.inevo.encontra.drawing.descriptors.Topogeo.TopogeoNode;
import pt.inevo.encontra.graph.swing.GraphViewer;
import pt.inevo.encontra.index.IndexedObject;
import pt.inevo.encontra.index.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Extractor for the TopogeoDescriptor.
 */
public class TopogeoDescriptorExtractor extends DescriptorExtractor<IndexedObject<Long, Drawing>, TopogeoDescriptor> {

  private int MAXVAL = 50; //250; // TODO Gabe: Rever normalização

  @Override
  protected IndexedObject<Long, Drawing> setupIndexedObject(TopogeoDescriptor descriptor, IndexedObject<Long, Drawing> object) {
    object.setId((Long)descriptor.getId());
    return object;
  }

  @Override
  public TopogeoDescriptor extract(IndexedObject<Long, Drawing> object) {
    Drawing drawing = object.getValue();

    //generate the graph and calculate the descriptor
    TopogeoGraph graph = generateTopogeoGraph(drawing);

    //GraphViewer graphViewer = new GraphViewer(graph);
    //graphViewer.show();

    DoubleMatrix2D matrix = generateMatrix(graph);
    double [] descriptorD = generateDescriptor(matrix);
//
//        //set the descriptor (as a vector)
    TopogeoDescriptor descriptor = new TopogeoDescriptor();
    descriptor.setId(drawing.getId());
    Vector<Double> d = new Vector<Double>(Double.class, descriptorD.length);
    d.setValues(ArrayUtils.toObject(descriptorD));

    descriptor.setValue(d);

    return descriptor;
  }

  private boolean isFeatureNode(TopogeoNode n){
    return n.getPrimitive().getId() == TopogeoNode.FEATURE_NODE;
  }

  public TopogeoGraph generateTopogeoGraph(Drawing drawing){
    // SVG -> Graph. Ver:
    // dbbuilder.cpp::main(), sbrclassify.cpp -> SbrClassify::classify(Drawing &drawing, NBtree &nbt)
    TopogeoGraph graph = new TopogeoGraph(drawing.getId());

    // We get an ordered collection of primitive nodes!
    ArrayList<TopogeoNode> nodes = graph.initialize(drawing);

    TopogeoNode a, b;

    for (int i = 0; i < nodes.size()-1; i++) {
      a = nodes.get(i);
      for(int j = i+1; j < nodes.size(); j++) {
        b = nodes.get(j);
        if (a.getPrimitive().isInPrimitive(b.getPrimitive()))
          graph.setParent(a, b);
        else if(b.getPrimitive().isInPrimitive(a.getPrimitive()))
          graph.setParent(b, a);
      }
    }

    return graph;
  }

  public DoubleMatrix2D generateMatrix(TopogeoGraph graph) {
    // Graph -> Matrix
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
//                theValue = 1.0;
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

  /*
public double []  generateDescriptor(DoubleMatrix2D matrix){
          DoubleMatrix1D vals = DoubleFactory1D.sparse.make(matrix.columns());
      EigenvalueDecomposition eigenValues = new EigenvalueDecomposition(matrix); //EigenValues(*matrix, D);

      DoubleMatrix2D D = eigenValues.getD();
      //System.out.println("subgraphEigenvalues: computed eigenvalues!");

      int c = 0;

      int i;
      for (i = 0; i < D.rows(); i++) {
          vals.set(i, Math.abs(D.get(i, i)) / MAXVAL);

          // vals(i) cannot be bigger then 1 since NBtree expects its values to be between (0,1).
          if (vals.get(i) < 1) {
              vals.set(i, Math.abs(vals.get(i)));
              //System.out.println( "[!!] Graph::subgraphEigenvalues - Warning! Value bigger than 1 !! - " + vals.get(i));
              //exit(1);
          } else {
             vals.set(i, vals.get(i)/MAXVAL);
          }


      }

      // descending order
      return vals.viewSorted().viewFlip().toArray();
}  */

  public double[] generateDescriptor(DoubleMatrix2D matrix) {
    EigenvalueDecomposition eig_decomp = new EigenvalueDecomposition(matrix);
    // computes eigen values
    double [] eig_vals = eig_decomp.getRealEigenvalues().toArray();
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
      if (eig_vals[i] > 0.001){
        trim_index = i;
        break;
      }
    }
    double[] trim_array = new double[eig_vals.length-trim_index];
    System.arraycopy(eig_vals, trim_index, trim_array, 0, trim_array.length);
    eig_vals = trim_array;

    for (int left=0, right=eig_vals.length-1; left<right; left++, right--) {
// exchange the first and last
      double temp = eig_vals[left]; eig_vals[left] = eig_vals[right]; eig_vals[right] = temp;
    }
    return eig_vals;
  }
}
