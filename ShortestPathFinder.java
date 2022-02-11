package col106.assignment6;

import java.util.ArrayList;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

public class ShortestPathFinder implements ShortestPathInterface {
    /**
     * Computes shortest-path from the source vertex s to destination vertex t 
     * in graph G.
     * DO NOT MODIFY THE ARGUMENTS TO THIS CONSTRUCTOR
     *
     * @param  G the graph
     * @param  s the source vertex
     * @param  t the destination vertex 
     * @param left the cost of taking a left turn
     * @param right the cost of taking a right turn
     * @param forward the cost of going forward
     * @throws IllegalArgumentException unless 0 <= s < V
     * @throws IllegalArgumentException unless 0 <= t < V
     * where V is the number of vertices in the graph G.
     */
    private HashMap<Integer,Vertex> pseudo;
    int numVertex; int adjSize; int min_key;
    private ArrayList<Edge>[] adjDual;
    private DijkstraNode[] dijkstra;
    private ArrayList<Integer> des_keys;

    public ShortestPathFinder (final Digraph G, final int[] s, final int[] t, 
    final int left, final int right, final int forward) {
        pseudo = new HashMap<Integer,Vertex>();
        numVertex = G.E()+1; adjSize = 4*G.V()+1;
        adjDual = new ArrayList[adjSize]; des_keys = new ArrayList<>();
        dijkstra = new DijkstraNode[adjSize]; int width = G.W();
        for(int v=0; v<adjSize; v++) adjDual[v] = new ArrayList<Edge>();
        for(int v=0; v<adjSize; v++) dijkstra[v] = new DijkstraNode();
        Vertex Source = new Vertex(-1,-1,s[0],s[1],0);
        createDual(G, Source, width, left, right, forward, t);
        dijkstra[0].update(-1,0); calculateShortest(0,t);
    }

    public void createDual(Digraph G, Vertex Source, int width, int left, int right, int forward, int[] des) {
        if(!pseudo.containsKey(Source.key)) pseudo.put(Source.key, Source);
        else return;
        if(Source.fi==des[0] && Source.fj==des[1]) des_keys.add(Source.key);
        Iterable<Edge> adjlist = G.adj(Source.fi*width+Source.fj);
        Iterator<Edge> adj = adjlist.iterator();
        ArrayList<Edge> current = new ArrayList<>();
        while(adj.hasNext())
        {
            Edge adjacent = adj.next(); double w = adjacent.weight(); int turn = 0; 
            Vertex nn = G.nodemap(adjacent.to()); int key = key(Source.fi, Source.fj, nn.i, nn.j, width);
            Vertex Secondary = new Vertex(Source.fi, Source.fj, nn.i, nn.j, key);
            if(Source.key!=0) turn = direction(Source, Secondary);
            if(turn==0) w = w+forward;
            else if(turn>0) w = w+left;
            else w = w+right;
            current.add(new Edge(Source.key,Secondary.key,w));
            createDual(G, Secondary, width, left, right, forward, des);
        }
        adjDual[Source.key] = current;
    }

    // Return number of nodes in dual graph
    public int numDualNodes() {
        return numVertex;
    }

    // Return number of edges in dual graph
    public int numDualEdges() {
        int out = 0;
        for(int i=0;i<adjSize;i++) out = out+adjDual[i].size();
        return out;
    }

    // Return hooks in dual graph
    // A hook (0,0) - (1,0) - (1,2) with weight 8 should be represented as
    // the integer array {0, 0, 1, 0, 1, 2, 8}
    public ArrayList<int[]> dualGraph() {
        ArrayList<int []> out = new ArrayList<>();
        for (int y=0;y<adjSize;y++)
        {
            ArrayList<Edge> ad = adjDual[y];
            int sz = ad.size();
            for(int x=0;x<sz;x++)
            {
                int[] trial = new int[7];
                int i = ad.get(x).from();
                int j = ad.get(x).to();
                int k = (int)ad.get(x).weight();
                trial[0] = pseudo.get(i).i;
                trial[1] = pseudo.get(i).j;
                trial[2] = pseudo.get(j).i;
                trial[3] = pseudo.get(j).j;
                trial[4] = pseudo.get(j).fi;
                trial[5] = pseudo.get(j).fj;
                trial[6] = k;
                out.add(trial);
            }
        }
        return out;
    }

    // Return true if there is a path from s to t.
    public boolean hasValidPath() {
        if(des_keys.size()>0) return true;
        return false;
    }

    // Return the length of the shortest path from s to t.
    public int ShortestPathValue() {
        int size = des_keys.size(); double out = Double.POSITIVE_INFINITY; 
        for(int i=0;i<size;i++) 
        {
            if(dijkstra[des_keys.get(i)].value<out) 
            {
                min_key = des_keys.get(i);
                out = dijkstra[min_key].value;
            }
        }    
        return (int)out;
    }

    public void calculateShortest(int s, int[] des) {
        if(pseudo.get(s).fi==des[0] && pseudo.get(s).fj==des[1]) return;
        ArrayList<Edge> neighbour = adjDual[s];
        int size = neighbour.size();
        for(int i=0;i<size;i++)
        {
            int key = neighbour.get(i).to();
            double val = dijkstra[s].value+neighbour.get(i).weight();
            if(val<dijkstra[key].value) dijkstra[key].update(s,val);
            calculateShortest(key,des);
        }
    }
    // Return the shortest path computed from s to t as an ArrayList of nodes, 
    // where each node is represented by its location on the grid.
    public ArrayList<int[]> getShortestPath() {
        ArrayList<Vertex> path = new ArrayList<>();
        DijkstraNode ls = dijkstra[min_key];
        path.add(pseudo.get(min_key));
        while(ls.Last!=-1)
        { 
            path.add(pseudo.get(ls.Last));
            ls = dijkstra[ls.Last];
        }
        ArrayList<int[]> out = new ArrayList<>();
        for(int i=path.size()-1;i>=0;i--) out.add(new int[]{path.get(i).fi, path.get(i).fj});
        return out;
    }

    public int direction(Vertex i, Vertex f) {
        int a = i.i - i.fi; 
        int b = i.j - i.fj;
        int c = f.i - f.fi;
        int d = f.j - f.fj;
        return a*d-b*c;
    }

    public int key(int x, int y, int x_, int y_, int w) {
        int decider = x-x_;
        int out = 4*(x*w+y);
        if(decider==0)
        { 
            if((y-y_)>0) return out+1;
            else return out+4;
        }
        else if(decider<0) return out+2;
        else return out+3;
    }
}
