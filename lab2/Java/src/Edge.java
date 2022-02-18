public class Edge {
    private String src, dest, weight;


    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public String getWeight() {
        return weight;
    }

    public Edge(String src, String dest, String weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public void printEdge(){
        System.out.print(src + " (" + weight + ") " + dest + "  |  ");
    }

}