package pt.inevo.encontra.drawing.descriptors.Topogeo;

import pt.inevo.encontra.drawing.Primitive;
import pt.inevo.encontra.graph.GraphNode;

import java.util.ArrayList;

/**
 * This class represents a node in the topology graph. Each node represnts a
 * drawing primitive.
 * @author Gabriel
 */
public class TopogeoNode extends GraphNode {
    private Primitive primitive;
    private TopogeoNode parent;
    private ArrayList<TopogeoNode> children;
    private ArrayList<TopogeoNode> siblings;

    /**
     * Feature node ID
     */
    public static Long FEATURE_NODE = -1l;
    public static Long ROOT_NODE = 0l;

    /**
     * Creates a new empty node.
     */
    public TopogeoNode() {
        this(new Long(0), "");
    }

    /**
     * Creates a new empty node.
     */
    public TopogeoNode(Long id, String svgid) {
        super(id);
        primitive = new Primitive(id);
        primitive.setSvgId(svgid);
        parent = null;
        children = new ArrayList<TopogeoNode>();
        siblings = new ArrayList<TopogeoNode>();
    }

    /**
     * Returns the primitive represented by this node.
     * @return the primitive represented by this node.
     */
    public Primitive getPrimitive() {
        return primitive;
    }

    /**
     * Sets the primitive represented by this node.
     * @param primitive the primitive to be represented by this node.
     */
    public void setPrimitive(Primitive primitive) {
        this.primitive = primitive;
    }

    /**
     * Returns the parent of this node.
     * @return the parent of this node.
     */
    public TopogeoNode getParent() {
        return parent;
    }

    /**
     * Sets the parent of this node.
     * @param parent the parent to set.
     */
    public void setParent(TopogeoNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the children of this node.
     * @return the children of this node.
     */
    public ArrayList<TopogeoNode> getChildren() {
        return children;
    }

    /**
     * Sets the children of this node.
     * @param children the children to set.
     */
    public void setChildren(ArrayList<TopogeoNode> children) {
        this.children = children;
    }

    /**
     * Returns the siblings of this node.
     * @return the siblings of this node.
     */
    public ArrayList<TopogeoNode> getSiblings() {
        return siblings;
    }

    /**
     * Sets the siblings of this node.
     * @param siblings the siblings to set.
     */
    public void setSiblings(ArrayList<TopogeoNode> siblings) {
        this.siblings = siblings;
    }

    /**
     * Returns the level of this node.
     * @return the level of this node.
     */
    public int getLevel() {
        if (parent != null)
            return parent.getLevel()+1;
        else
            return 0;
    }

    /**
     * Creates a new node, assigned to the same primitive as this one.
     * @return returns a duplicate of this node.
     */
    public TopogeoNode duplicate() {
        TopogeoNode newNode = new TopogeoNode();
        newNode.setPrimitive(this.getPrimitive());
        return newNode;
    }

    /**
     * Returns a string representation of the node.
     * @return a string representation of the node.
     */
    @Override
    public String toString() {
        return "node " + primitive.getId();
    }
}
