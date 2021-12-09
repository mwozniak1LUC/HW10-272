import java.util.*;
import java.io.*;

public class DirectedGraph  {

    ArrayList<DirectedNodeList> dag;
    int numVertex;
    boolean [] marked;
    int[] finishingN;
    int tracker;

    public DirectedGraph() {
        dag = new ArrayList<>();
        numVertex=0;
        finishingN = new int[numVertex];
    }

    public DirectedGraph(int n) {
        numVertex = n;
        finishingN = new int[numVertex];
        dag = new ArrayList<>(n);
        marked= new boolean[n];
        for (int i = 0; i < numVertex; i++) {
            dag.add(new DirectedNodeList());
        }
    }

    public static ArrayList<Edge> generateEdgeList() throws FileNotFoundException {
        Scanner edgeScanner = new Scanner (new File ("Slashdot0902.txt"));
        edgeScanner.nextLine(); //skipping lines without data
        edgeScanner.nextLine(); //skipping lines without data
        edgeScanner.nextLine(); //skipping lines without data
        edgeScanner.nextLine(); //skipping lines without data
        ArrayList<Edge> edgeList = new ArrayList<Edge>();
        while (edgeScanner.hasNext()) {
            String line = edgeScanner.nextLine();
            String[] parts = line.split("\t");
            int v1 = Integer.parseInt(parts[0]);
            int v2 = Integer.parseInt(parts[1]);
            edgeList.add(new Edge(v1, v2));
        }
        return edgeList;
    }

    //    public void printAdjacency(int u) {
//        DirectedNodeList dnl = getNeighborList(u);
//        System.out.println ("vertices going into "+u+"  "+dnl.getInList());
//        System.out.println ("vertices going out of "+u+"  "+dnl.getOutList());
//        System.out.println();
//    }

    public void addEdge(int u, int v) {
        if (u>=0 && u<numVertex && v>=0 && v<numVertex) {
            if (u!=v) {
                getNeighborList(u).addToOutList(v);
                getNeighborList(v).addToInList(u);
            }
        }
        else throw new IndexOutOfBoundsException();
    }

    public DirectedNodeList getNeighborList(int u) {
        return dag.get(u);
    }

    public void postOrderDepthFirstTraversal() {
        tracker = 0;
        for (int i=0;i<numVertex;i++)
            if (!marked[i])
                postOrderDFT(i);

    }

    public void postOrderDFT(int v){
        marked[v] = true;
        for (Integer u: dag.get(v).getInList()){
            if (!marked[u]) postOrderDFT(u);
        }

        // reduced try
        try {
            finishingN[tracker] = v;
            tracker++;
        } catch(Exception e) {
            System.out.println("Failed");
        }
    }

    public ArrayList<Integer> depthFirst(int v){
        ArrayList<Integer> fillMarked = new ArrayList<>();
        if (marked[v]) {
            return fillMarked;
        }
        marked[v] = true;
        fillMarked.add(v);

        for ( Integer u: dag.get(v).getOutList() )
            if (!marked[u])
                fillMarked.addAll(depthFirst(u)); //recursive call
        return fillMarked;
    }

    public int[] ccStats(int[] vertices) {
        int[] ccStats = new int[2];
        for(int i = numVertex - 1; i >= 0; i--) {
            ArrayList<Integer> dF = depthFirst(finishingN[i]);

            if (dF.size() > 0)          ccStats[1]++; // count++
            if (dF.size() > ccStats[0]) ccStats[0] = dF.size(); // new max

            for(Integer vertex: dF) {
                vertices[vertex] = finishingN[i];
            }

        }
        return ccStats; // [0] = max, [1] = count
    }

    public DirectedGraph reducedDag(int[] vertices, int countCC) {
        DirectedGraph reducedDag = new DirectedGraph(countCC); //10559
        HashSet<Integer> leaderHSet = new HashSet<>(vertices.length);
        for(int vertex: vertices) {
            leaderHSet.add(vertex);
        }

        ArrayList<Integer> leadersArrList = new ArrayList<>(leaderHSet);
        int[] arrMap = new int[numVertex];

        for( int i = 0; i < numVertex; i++ )
            arrMap[i] = -1;
        for( int i = 0; i < leadersArrList.size(); i++ )
            arrMap[leadersArrList.get(i)] = i;
        for( int i = 0; i < numVertex; i++ ){
            for(Integer v: getNeighborList(i).inList){
                reducedDag.addEdge(arrMap[vertices[v]], arrMap[vertices[i]]);
            }

        }

        return reducedDag;
    }

    int getEdgeCount(DirectedGraph dg) {
        int count = 0;
        for(DirectedNodeList nodes: dg.dag) {
            count += nodes.inList.size();
        }
        return count;
    }

    public void stronglyConnected(){
        // 1,2,3
        postOrderDepthFirstTraversal();
        int[] vertices = new int[numVertex];
        for( int i = 0; i < numVertex; i++ ) {
            vertices[finishingN[i]] = i;
        }
        marked = new boolean[numVertex];
        int[] ccStats = ccStats(vertices);
        System.out.println("(i) number of strongly connected components: "           + ccStats[1]);
        System.out.println("(ii) max size among all strongly connected components: " + ccStats[0]);

        // 4
        DirectedGraph reduced = reducedDag(vertices, ccStats[1]);
        System.out.println("-------------------------------------------------");
        System.out.println("Problem 4: reduced DAG edges: " + getEdgeCount(reduced));
        System.out.println("-------------------------------------------------");
    }


    /** Needs to be run with -Xss100m like stated in problem 5 to dodge stack overflow **/
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("HOMEWORK #10 (mwozniak1: RUN WITH -Xss100m)");
        System.out.println("-------------------------------------------------");
        int vertexCount = 82168; // total vertices
        DirectedGraph dag = new DirectedGraph(vertexCount);
        ArrayList<Edge> edges = dag.generateEdgeList();
        for(Edge current: edges) {
            dag.addEdge(current.v1, current.v2);
        }
        dag.marked = new boolean[vertexCount];
        dag.stronglyConnected();
    }

}
