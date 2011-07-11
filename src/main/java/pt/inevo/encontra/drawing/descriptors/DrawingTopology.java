package pt.inevo.encontra.drawing.descriptors;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import pt.inevo.encontra.drawing.Drawing;
import pt.inevo.encontra.drawing.Primitive;
import pt.inevo.encontra.graph.Graph;
import pt.inevo.encontra.graph.GraphEdge;
import pt.inevo.encontra.graph.GraphNode;
import pt.inevo.encontra.graph.swing.GraphViewer;
import pt.inevo.encontra.index.Vector;
import pt.inevo.encontra.storage.IEntry;

import java.util.ArrayList;
import java.util.List;

public class DrawingTopology implements IEntry<Long,List<Vector>> {

    List<Vector> topology;
    Long id;

     //!< # of IDs stored in front of the feature vector. (in this case: graph, subgraph and level)
    public static int NIDS = 3;

     public DrawingTopology(Drawing drawing){
         this(drawing,true);
     }

    public DrawingTopology(Drawing drawing,boolean levels){
       super();
       setId(drawing.getId());
       this.topology=getTopology(drawing, levels);
    }

    public Graph buildTopologyGraph(Drawing drawing) {
        // Create the new Graph with the same id as the drawing
        Graph<GraphNode<Primitive>, GraphEdge> graph = new Graph(drawing.getId());

        /* Retrieve all primitives in an ordered list (the direction really doesn't
           * matter as long as it's ordered somehow.
           * This makes the calculation of inclusion a whole lot easier.
           */
        List<Primitive> lst_primitives = drawing.getAllPrimitivesSorted();

        // Counters
        int i = 0;
        int j = 0;

        /* This is the root node of the graph. It contains a Primitive with the
           * maximum size of the drawing. All other nodes start as children of this
           * node and are changed later if necessary.
           */
        Primitive rootPrimitive = new Primitive();

        rootPrimitive.addPoint(0, 0);
        rootPrimitive.addPoint(drawing.getWidth(), 0);
        rootPrimitive.addPoint(drawing.getWidth(), drawing.getHeight());
        rootPrimitive.addPoint(0, drawing.getHeight());
        rootPrimitive.addPoint(0, 0);

        GraphNode rootNode = new GraphNode();
        rootNode.setData(rootPrimitive);

        graph.createNode(0l, rootNode); // Root node

        // Add all Primitives in the list to the rootNode.
        for(i = 0; i < lst_primitives.size(); i++) {
            Primitive tempPrimitive = lst_primitives.get(i);

            assert(tempPrimitive!=null);

            GraphNode tempNode = new GraphNode(new Long(tempPrimitive.getId()));
            tempNode.setData(tempPrimitive);

            graph.addChild(0l, new Long(tempPrimitive.getId()));
        }

        // Do the inclusion thing first (proximity depends on inclusion)

        //System.out.println("[++] SbrCore::buildTopologyGraph - Computing inclusions ..");

        for(i = 0; i < lst_primitives.size(); i++) {
            Primitive thisPrimitive = lst_primitives.get(i);

            for (j = i+1; j < lst_primitives.size(); j++) {
                Primitive thatPrimitive = lst_primitives.get(j);

                if (thisPrimitive.isInPrimitive(thatPrimitive)) {
                    graph.setParent(new Long(thisPrimitive.getId()), new Long(thatPrimitive.getId()));
                }

                // Check if the primitive at j is in the primitive at i
                if (thatPrimitive.isInPrimitive(thisPrimitive)) {
                    graph.setParent(new Long(thatPrimitive.getId()), new Long(thisPrimitive.getId()));
                }
            }
        }

        /* Do the proximity thing, this means that any Nodes that have
           * the same parent node will be considered adjacent.
           */

        List<GraphNode<Primitive>> nodeList = new ArrayList(graph.getVertices());

        for(i = 0; i < nodeList.size(); i++) {
            for(j = i+1; j < nodeList.size(); j++) {
                if((nodeList.get(i).getParent()==nodeList.get(j).getParent()) && nodeList.get(i).getId() != 0) {
                    nodeList.get(i).addAdjLink(nodeList.get(j));
                    nodeList.get(j).addAdjLink(nodeList.get(i));

                    //System.out.println( "[++] SbrCore::buildTopologyGraph  - Adjacency found between " + nodeList.get(i).getId() + " and " + nodeList.get(j).getId() );
                }
            }
        }

        // Delete the vector of pointers
        //delete lst_primitives;

        //graph.printList( nodeList );

        int size = nodeList.get(0).getInclusionLinkCount();
        //System.out.println( "[!!] SbrCore::buildTopologyGraph - Node(0) now contains [" + size + "] items");

        return graph;
    }


    /**
     * Computes a descriptor for a level.
     *
     * @param currSubGraph	The current subgraph
     * @param level			The level to compute?
     * @param subGraphID	The id of the subgraph for which to compute the descriptor?
     */
    void levelDescriptor(Graph graph,List<GraphNode> currSubGraph, int level, int subGraphID, List<DoubleMatrix1D> descriptorSet) {
        //System.out.println("[++] Graph::levelDescriptor - Computing leveldescriptor for level [" + level + "] with subGraphID [" + subGraphID + "]");
        //System.out.println("[++] Graph::levelDescriptor - getNumItems =" + currSubGraph.getNumItems());


        if (currSubGraph.size() > 1) {

            DoubleMatrix2D adjMatrix;
            //(currSubGraph->getNumItems());


            //System.out.println("[++] levelDescriptor: computing adjacency matrix" );
            adjMatrix=Graph.adjacencyMatrix(currSubGraph);

            DoubleMatrix1D eig=	Graph.subgraphEigenvalues(adjMatrix);

            DoubleMatrix1D vals= DoubleFactory1D.sparse.make(eig.size() + NIDS);

            vals.set(1, graph.getId());
            vals.set(2, subGraphID);
            vals.set(3, level);

            //System.out.println("[++] Graph::levelDescriptor - creating RowVector _id " +  _id + " subGraphID " + subGraphID + " level " + level);

            int j;
            for(j = 0; j < eig.size(); j++) {
                vals.set(j+NIDS,eig.get(j));
                //System.out.println("[++] levelDescriptor: pushing value " + eig.get(j) + " to RowVector" );
            }

            descriptorSet.add(vals);

            //delete adjMatrix;
            //delete eig;
        }

    }
    /**
     * Computes a descriptor for the graph, using all eigenvalues.
     *
     * @param graphList	List containing the subgraphs for wich the descriptors
     *					have to be computed.
     */
    void subgraphDescSet(Graph graph,List<GraphNode> graphList, List<DoubleMatrix1D> descriptorSet) {
        levelDescriptor(graph,graphList, 0, 0, descriptorSet);
    }


    /**
     * Computes the set of descriptors (one for each level) for a subgraph.
     *
     * The subgraph is specified by the list of nodes (graphList).
     *
     * @param graphList		List containing the subgraphs for wich the descriptors
     *						have to be computed.
     * @param subGraphID	The id of the subGraph for wich the descriptors
     *						have to be computed. (CHECK!)
     */
    void subgraphDescSet(Graph graph,List<GraphNode> graphList, int subGraphID, List<DoubleMatrix1D> descriptorSet) {

        int level=0;

        List<GraphNode> currSubGraph = new ArrayList<GraphNode>();

        currSubGraph.add(graphList.get(0));  // adds the root node of the subgraph
        List<GraphNode> level1List = graphList.get(0).getIncList();
        currSubGraph.addAll(level1List);

        levelDescriptor(graph,currSubGraph, level, subGraphID,descriptorSet);

        while (currSubGraph.size() != graphList.size()) {
            level++;
            List<GraphNode> newLevel = new ArrayList<GraphNode>();

            int i;
            for(i = 0; i < level1List.size(); i++) {
                newLevel.addAll(level1List.get(i).getIncList());
            }

            currSubGraph.addAll(newLevel);

            levelDescriptor(graph,currSubGraph, level, subGraphID, descriptorSet);

            if (level > 1) {
                //delete level1List;
            }

            level1List = newLevel;
        }
    }

    /**
     * Calculates the Graph's single- or multilevel-descriptor.
     * A descriptor is a list of Eigenvalues.
     *
     * @param levels	If true descriptors for all levels will be calculated.
     *
     * @return	CIList of RowVectors. Each RowVector contains the descriptor for a level.
     */
    public List<DoubleMatrix1D> getDescriptors(Graph graph,boolean levels) {

        //System.out.println( "[++] descriptors: for graph " + _id);

        //if (levels)
        //System.out.println(" and its subgraphs");


        List<DoubleMatrix1D> descriptorSet = new ArrayList<DoubleMatrix1D>();

        if (levels) {
            // Isolate al subgraphs to a certain depth/level and store them in _subgraphs

            //System.out.println( "Graph:::descriptors - Isolating subgraphs to a level of " + MAXLEVEL );
            //isolateAllSubGraphs(MAXLEVEL);
            //System.out.println( "Graph:::descriptors - Isolating subgraphs" );
            List<List<GraphNode>> subgraphs=graph.isolateAllSubGraphs();

            // Compute descriptors for all subgraphs
            int j;
            for(j=0; j < subgraphs.size(); j++) {
                //System.out.println(  "Graph:::descriptors - Computing descriptors for subgraph " + j );
                subgraphDescSet(graph,subgraphs.get(j), j, descriptorSet);

            }

        } else {
            // Compute the descriptor for the entire Graph
            subgraphDescSet(graph,new ArrayList(graph.getVertices()), descriptorSet);
        }

        return descriptorSet;
    }

    public void printDescriptors(List<DoubleMatrix1D> descriptors) {
        int i;
        int j;

        for(i = 0; i < descriptors.size(); i++) {
            System.out.print( "Graph::printDescriptors - descriptor " + i + ": ");

            for(j = 0; j < descriptors.get(i).size(); j++)
                System.out.print( descriptors.get(i).get(j) + " ");

            //System.out.println();
        }
    }


    public List<Vector> getTopology(Drawing drawing, boolean levels) {
        /*
           * Global approach:
           * - compute the topology Graph
           * - compute the topology descriptors of the Graph
           * - return the list
           */

        Graph graph = buildTopologyGraph(drawing);

        GraphViewer viewer=new GraphViewer(graph);
        //viewer.writeJPEGImage("graph_"+graph.getId()+".jpg");

        // Compute and return the topology descriptors
        System.out.println("[++] SbrCore::getTopology - Computing topology descriptors");
        List<DoubleMatrix1D> mDescritors = getDescriptors(graph, levels);

        graph.printAdjacencyMatrix();
        printDescriptors(mDescritors);

        Vector<Double> vector;
        List<Vector> res = new ArrayList<Vector>();

        for(DoubleMatrix1D matrix:mDescritors){
            int size=matrix.size();
            vector=new Vector<Double>(Double.class,size);
            for(int i=0;i<size;i++){
                vector.set(i,matrix.get(i));
            }
            res.add(vector);
        }

        return res;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public List<Vector> getValue() {
        return topology;
    }

    @Override
    public void setValue(List<Vector> o) {
        this.topology=o;
    }
}
