package col106.assignment6;
/**
 * Directed, weighted Edge class
 */
public class Edge {
    private final int u;
    private final int v;
    private final double weight;

    /**
     * Initializes a directed edge from vertex u to vertex v
     * @param u the source vertex
     * @param v the destination vertex
     * @param weight the weight of the directed edg
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public Edge(int u, int v, double weight) {
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return u;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return v;
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }
}