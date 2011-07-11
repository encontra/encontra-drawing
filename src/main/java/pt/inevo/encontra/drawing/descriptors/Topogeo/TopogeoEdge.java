package pt.inevo.encontra.drawing.descriptors.Topogeo;

import edu.uci.ics.jung.graph.util.EdgeType;
import pt.inevo.encontra.graph.GraphEdge;

/**
 * This class represents a topology graph edge. Such an edge represents a link
 * between two drawing primitives.
 * @author Gabriel
 */
public class TopogeoEdge extends GraphEdge {
//public class TopogeoEdge extends GraphInclusionEdge {

    private float value;
    private Type type;

    @Override
    public EdgeType getType() {
        if (type.equals(Type.Parental) || type.equals(Type.Adjacency)) {
            return EdgeType.DIRECTED;
        } else {
            return EdgeType.UNDIRECTED;
        }
    }

    /*
     * The type of TopogeoEdge.
     */
    public enum Type {
        /**
         * a directed link from a parent node to a child
         */
        Parental("Parental"),
        /**
         * a undirected link between two sibling nodes
         */
        Adjacency("Adjacency"),
        /**
         * a undirected link between a node and a feature node
         */
        Feature("Feature");

        private final String name;

        private Type(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    /**
     * Creates a new topology edge.
     * @param val the value describing the spatial relationship betwee the
     * two drawing primitives.
     * @param type the type of edge. It can be a Parental, Adjacency or Feature edge.
     */
    public TopogeoEdge(float val, Type type) {
        super(null, null);
        this.value = val;
        this.type = type;
    }

    /**
     * Returns the edge value.
     * @return the edge value.
     */
    public float getValue() {
        return value;
    }

    /**
     * Returns the type of the edge.
     * @return the type of the edge.
     */
    public Type getTopoGeoEdgeType() {
        return type;
    }

    /**
     * Returns a string representation of the edge.
     * @return a string representation of the edge.
     */
    @Override
    public String toString() {
        return type.toString() + " " + value;
    }
}
