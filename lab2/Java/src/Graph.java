import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

//Grammar to FA
public class Graph {
    private LinkedHashMap<String, ArrayList<Edge>> adjList;
    private ArrayList<String> vertices;
    public HashMap<String, ArrayList<Edge>> getAdjList() {
        return adjList;
    }

    public Graph(LinkedHashMap<String, ArrayList<Edge>> adjList, ArrayList<String> vertices) {
        this.adjList = adjList;
        this.vertices = vertices;
    }

    public void addEdge(String userInput) {
        String[] arrOfStr = userInput.split(" ");
        String src = arrOfStr[0];
        String weight = arrOfStr[1];
        String dest = arrOfStr[2];

        if (!vertices.contains(src)) {//q0 a q2
            vertices.add(src);
            adjList.put(src, new ArrayList<Edge>());
            adjList.get(src).add(new Edge(src, dest, weight));
        }
        else {
            Edge e = new Edge(src, dest, weight);
            adjList.get(src).add(e);
        }

        if(!vertices.contains(dest)){
            vertices.add(dest);
            adjList.put(dest, new ArrayList<Edge>());
        }
    }

    public void printGraph(){
        for (String s : adjList.keySet()) {
            System.out.print(s+" : ");
            for(Edge e: adjList.get(s))
                e.printEdge();
            System.out.println();
        }
    }
}