package pt.inevo.encontra.drawing.descriptors.Topogeo;

import edu.uci.ics.jung.graph.util.EdgeType;
import org.apache.commons.lang.ArrayUtils;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.Primitive;
import pt.inevo.encontra.drawing.geometry.CIGeometric;
import pt.inevo.encontra.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * This class represents a topology graph for a SVG file. An instance of this
 * class is used in order to compute the topology features for a specific SVG
 * drawing.
 *
 * @author Gabriel
 */
public class TopogeoGraph extends Graph<TopogeoNode, TopogeoEdge> {

    /**
     * The root node of the Graph.
     */
    private TopogeoNode root;

    /**
     * Feature Nodes - Nodes representing each considered geometric feature.
     * Every other primitive node should connect to every single one of these
     * in order to establish a relationship between the primitive and the each
     * feature.
     */
    public static TopogeoNode[] featureNodes = new TopogeoNode[]{
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Tl_Pch"), // Tl_Pch
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Pch2_Ach"), // Pch2_Ach
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Her_Wer"), // Her_Wer
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Alq_Aer"), // Alq_Aer
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Ach_Aer"), // Ach_Aer
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Alq_Ach"), // Alq_Ach
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Alt_Alq"), // Alt_Alq
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Alt_Ach"), // Alt_Ach
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Plq_Pch"), // Plq_Pch
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Plt_Pch"), // Plt_Pch
            new TopogeoNode(TopogeoNode.FEATURE_NODE, "Pch_Per") // Pch_Per
    };

    /**
     * Creates an empty topology graph.
     */
    public TopogeoGraph() {
        this(new Long(0));
    }

    /**
     * Creates an empty topology graph.
     *
     * @param id the id of the graph.
     */
    public TopogeoGraph(Long id) {
        super(id);
    }

    /**
     * Returns the root node.
     *
     * @return the root noode.
     */
    public TopogeoNode getRoot() {
        return root;
    }

    /**
     * Sets the root node of the graph.
     *
     * @param root the root node of the graph.
     */
    public void setRoot(TopogeoNode root) {
        this.root = root;
    }

    /**
     * Initializes the graph according to the supplied drawing.
     * In this method, the root node is set, as well as the minimum and maximum
     * X and Y values. All the primitives are added as children to the root node.
     *
     * @param drawing the drawing to be represented.
     * @return An collection of nodes ordered by insertion
     */
    public Collection<TopogeoNode> initialize(Drawing drawing) {
        TopogeoNode node;
        ArrayList<Primitive> primitives = drawing.getPrimitivesSortedX();
        // Keep an ordered collection of nodes
        setRoot(new TopogeoNode());
        double xmin, ymin, xmax, ymax;
        xmin = drawing.getXmin();
        xmax = drawing.getXmax();
        ymin = drawing.getYmin();
        ymax = drawing.getYmax();

        getRoot().getPrimitive().addPoint(xmin, ymin);
        getRoot().getPrimitive().addPoint(xmax, ymin);
        getRoot().getPrimitive().addPoint(xmax, ymax);
        getRoot().getPrimitive().addPoint(xmin, ymax);
        getRoot().getPrimitive().addPoint(xmin, ymin);
        getRoot().getPrimitive().setSvgId("root");

        addVertex(getRoot());

        // add feature nodes
        for (TopogeoNode fn : featureNodes) {
            addVertex(fn);
        }

        // add all nodes and set them as children from the root node
        for (Iterator<Primitive> i = primitives.iterator(); i.hasNext(); ) {
            node = new TopogeoNode();
            node.setPrimitive(i.next());
            addVertex(node);

            double[] descriptor = generateGeometryDescriptor(node.getPrimitive());
            if (descriptor.length == featureNodes.length) {
                for (int j = 0; j < descriptor.length; j++) {
                    addEdge(new TopogeoEdge(descriptor[j], TopogeoEdge.Type.Feature), featureNodes[j], node, EdgeType.UNDIRECTED);
                }
            } // else, something really wrong happened...
            setParent(node, getRoot());
        }

        log.log(Level.INFO, "Topogeo Graph with id " + getId() + " initialized.");

        return getVertices();
    }

    /**
     * Generates a Geometry Descriptor for the Primitive, based on Cali
     *
     * @param primitive the primitive to be used to determine the descriptor
     * @return a double array that represents the geometry descriptor
     */
    private double[] generateGeometryDescriptor(Primitive primitive) {
        int numPoints = primitive.getNumPoints();
        if (numPoints > 0) {
            CIGeometric g = new CIGeometric();
            g.newScribble();
            g.newStroke();
            for (int i = 0; i < numPoints; i++) {
                g.addPoint(primitive.getPoint(i).getX(), primitive.getPoint(i).getY());
            }
            return ArrayUtils.toPrimitive(g.geometricFeatures().toArray(new Double[1]));
        } else {
            return null;
        }
    }

    /**
     * Sets the parent of a 'child' node. Removes the necessary edges and establishes the new ones
     * accordingly to the algorithm.
     *
     * @param childId     the child node id
     * @param newParentId the parent node id
     * @return true if the parent is successfully set, or false otherwise
     */
    @Override
    public boolean setParent(Long childId, Long newParentId) {
        TopogeoNode childNode = findNode(childId);
        TopogeoNode parentNode = findNode(newParentId);

        if (childNode != null && parentNode != null) {
            return setParent(childNode, parentNode);
        } else {
            return false;
        }
    }

    /**
     * Sets the parent of a 'child' node. Removes the necessary edges and establishes the new ones
     * accordingly to the algorithm.
     *
     * @param child     the child node
     * @param newParent the parent node
     * @return true if the parent is successfully set, or false otherwise
     */
    public boolean setParent(TopogeoNode child, TopogeoNode newParent) {
        if (!containsVertex(child) || !containsVertex(newParent))
            return false;

        TopogeoNode currentParent = child.getParent();
        // only if current parent is different from the new parent
        if (currentParent == null || !currentParent.equals(newParent)) {
            // remove the adjacency links to the old siblings
            if (currentParent != null) {
                TopogeoEdge edgeToRemove = null;
                Collection<TopogeoNode> oldSiblings = new ArrayList<TopogeoNode>(child.getSiblings());
                TopogeoNode oldSibling = null;
                for (Iterator<TopogeoNode> i = oldSiblings.iterator(); i.hasNext(); ) {
                    oldSibling = i.next();
                    edgeToRemove = findAdjacencyEdge(child, oldSibling);
                    if (edgeToRemove != null) {
                        removeEdge(edgeToRemove);
                        child.getSiblings().remove(oldSibling);
                        oldSibling.getSiblings().remove(child);
                    }
                }
                // remove the edge linking the old parent to the child;
                edgeToRemove = findParentalEdge(child, currentParent);
                if (edgeToRemove != null) {
                    removeEdge(edgeToRemove);
                    child.setParent(null);
                    currentParent.getChildren().remove(child);
                }
            }

            // add a new parental link
            addEdge(new TopogeoEdge(1.0f, TopogeoEdge.Type.Parental), newParent, child, EdgeType.DIRECTED);
            newParent.getChildren().add(child);
            child.setParent(newParent);
            // add new adjacency links to the new siblings
            Collection<TopogeoNode> siblings = new ArrayList(newParent.getChildren());
            siblings.remove(child);
            double diag = newParent.getPrimitive().getDiagonalLength();
            double adjacencyHint;
            TopogeoNode sibling = null;
            // make a new adjacency link between the new child and all other siblings.
            for (Iterator<TopogeoNode> i = siblings.iterator(); i.hasNext(); ) {
                sibling = i.next();
                adjacencyHint = generateAdjacencyValue(diag, child.getPrimitive(), sibling.getPrimitive());
                addEdge(new TopogeoEdge(adjacencyHint, TopogeoEdge.Type.Adjacency), child, sibling, EdgeType.UNDIRECTED);
                child.getSiblings().add(sibling);
                sibling.getSiblings().add(child);
            }
        }
        return true;
    }

    /**
     * Generates the adjacency value between two sibling primitives on a graph.
     *
     * @param parentDiagonal the diagonal distance of the parent primitive.
     * @param child1         one of the siblings.
     * @param child2         the other sigling.
     * @return the adjacency value.
     */
    private double generateAdjacencyValue(double parentDiagonal, Primitive child1, Primitive child2) {
        double dist = child1.getShortestDistance(child2);
        return (parentDiagonal - dist) / parentDiagonal;
    }

    /**
     * Returns the parental edge between two nodes.
     *
     * @param child  the child.
     * @param parent the parent.
     * @return the edge linking the two nodes or null if no parental link exists
     *         between them.
     */
    public TopogeoEdge findParentalEdge(TopogeoNode child, TopogeoNode parent) {
        TopogeoEdge parentalEdge = null;
        TopogeoEdge possibleEdge = findEdge(parent, child);
        if (possibleEdge != null && possibleEdge.getTopoGeoEdgeType() == TopogeoEdge.Type.Parental) {
            parentalEdge = possibleEdge;
        }
        return parentalEdge;
    }

    /**
     * Returns the adjacency edge between two sibling nodes.
     *
     * @param child1 on of the children.
     * @param child2 the other children.
     * @return the edge linking the two siblings or null if no adjacency link
     *         exists between them.
     */
    public TopogeoEdge findAdjacencyEdge(TopogeoNode child1, TopogeoNode child2) {
        TopogeoEdge adjacencyEdge = null;
        TopogeoEdge possibleEdge = findEdge(child1, child2);
        if (possibleEdge != null && possibleEdge.getTopoGeoEdgeType() == TopogeoEdge.Type.Adjacency)
            adjacencyEdge = possibleEdge;
        return adjacencyEdge;
    }

    public TopogeoEdge findFeatureEdge(TopogeoNode node, TopogeoNode feature) {
        TopogeoEdge featureEdge = null;
        TopogeoEdge possibleEdge = findEdge(node, feature);
        if (possibleEdge != null && possibleEdge.getTopoGeoEdgeType() == TopogeoEdge.Type.Feature)
            featureEdge = possibleEdge;
        return featureEdge;
    }

    /**
     * Returns a string representation of the topology graph.
     *
     * @return a string representation of the topology graph.
     */
    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Graph ").append(getId())
                .append(" has the following composition:")
                .append(System.getProperty("line.separator"));
//        stringBuffer.append(this.toString());
        return stringBuffer.toString();
    }
}
