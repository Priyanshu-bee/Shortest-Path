package col106.assignment6;

public class DijkstraNode {
    int Last;
    double value;
    
    public DijkstraNode() {
        this.Last = -1;
        this.value = Double.POSITIVE_INFINITY;
    }

    public void update(int l, double v) {
        Last = l; value = v;
    }

}