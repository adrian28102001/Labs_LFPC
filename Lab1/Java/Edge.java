package LABFA;

public class Edge {
    private char src, dest, weight;

    public Edge(char src, char dest, char weight) {
        this.src = src;
        this.weight = weight;
        this.dest = dest;
    }

    public char getSrc(){
        return this.src;
    }

    public char getDest() {
        return this.dest;
    }

    public char getWeight() {
        return weight;
    }
}
