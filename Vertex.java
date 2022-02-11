package col106.assignment6;

public class Vertex {
    int i; int fi;
    int j; int fj;
    int key;

    public Vertex(int i, int j, int key){
        this.i = i;
        this.j = j;
        this.key = key;
    }

    public Vertex(int i, int j, int fi, int fj, int key){
        this.i = i; this.fi = fi;
        this.j = j; this.fj = fj;
        this.key = key;
    }
}