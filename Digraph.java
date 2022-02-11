package col106.assignment6;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Digraph {
    private int H;                      // height of the grid
    private int W;                      // width of the grid
    private int V;                      // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private ArrayList<Edge>[] adj;      // adj[v] = adjacency list for vertex v
    private HashMap<Integer,Vertex> map;
    
    /**  
     * Initializes an edge-weighted digraph from a csv file.
     * The format is the height of the grid, followed by the width W,
     * followed by the number of edges E,
     * followed by E pairs of vertices (i,j) and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  file the input file
     * @throws FileNotFoundException if file cannot be found
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public Digraph(String file){
        try {
            Scanner sc = new Scanner(new File(file));
            this.H = sc.nextInt();
            if (H < 0) throw new IllegalArgumentException("Height of grid must be nonnegative");
            this.W = sc.nextInt();
            if (W < 0) throw new IllegalArgumentException("Width of grid must be nonnegative");
            
            // Initialize an empty adjacency list of length V
            V = H * W;
            adj = new ArrayList[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new ArrayList<Edge>();
            }

            // Initialize a hashmap to store unique keys for vertices
            this.map = new HashMap<Integer,Vertex>();

            // Add edges to the adjacency list
            this.E = sc.nextInt();
            if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
            for (int e = 0; e < E; e++) {
                // Read first vertex
                int i1 = sc.nextInt();
                int j1 = sc.nextInt();
                int key1 = i1 * W + j1;
                Vertex v = new Vertex(i1, j1, key1);
                if (!this.map.containsKey(key1)) this.map.put(key1, v);

                // Read second vertex
                int i2 = sc.nextInt();
                int j2 = sc.nextInt();
                int key2 = i2 * W + j2;
                Vertex w = new Vertex(i2, j2, key2);
                if (!this.map.containsKey(key2)) this.map.put(key2, w);
                
                // Read edge weight
                double weight = sc.nextDouble();
                // Add edge to adjacency list
                addEdge(new Edge(key1, key2, weight));
            }
        }   
        catch (FileNotFoundException e) {
            System.out.println("invalid input file");
        }
    }

    // throw an IllegalArgumentException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the directed edge e to graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between 0 and V-1
     */
    public void addEdge(Edge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // Returns the width of the graph.
    public int W() {
        return W;
    }

     /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<Edge> edges() {
        ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Return Vertex for a given node ID in graph
     * @return
     */
    public Vertex nodemap(int v) {
        Vertex ver = this.map.get(v);
        return ver;
    }

    // Returns the hashmap
    public HashMap<Integer,Vertex> getHashMap() {
        return map;
    }

    // Returns the adjacency list of the graph.
    public ArrayList<Edge>[] adjacency() {
        return adj;
    }

}