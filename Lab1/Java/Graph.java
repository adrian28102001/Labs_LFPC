package LABFA;

import java.util.ArrayList;

public class Graph {
    private ArrayList<ArrayList<Edge>> adjList;
    private ArrayList<Character> vertices;

    public Graph(ArrayList<ArrayList<Edge>> adjList, ArrayList<Character> vertices) {
        this.adjList = adjList;
        this.vertices = vertices;
    }

    public void addEdge(String userInput) {
        char[] chars = userInput.toCharArray();

        //When input is length 4 "S aB"
        if (chars.length == 4) {
            //Check if Node exists
            if (!vertices.contains(chars[0])) {
                vertices.add(chars[0]);
                adjList.add(new ArrayList<>());
                adjList.get(vertices.size() - 1).add(new Edge(chars[0], chars[3], chars[2]));   //Create new Arraylist and add new node to start
            } else {//existing character
                adjList.get(getIndex(adjList, chars[0])).add(new Edge(chars[0], chars[3], chars[2]));
            }
            //When input is length 3 "A a"
        } else if (chars.length == 3) {
            //Check if Node exists
            if (!vertices.contains(chars[0])) {
                vertices.add(chars[0]);
                adjList.add(new ArrayList<>());
                adjList.get(vertices.size() - 1).add(new Edge(chars[0], ' ', chars[2])); //Create new Arraylist and add new node to start
            } else {//existing character
                adjList.get(getIndex(adjList, chars[0])).add(new Edge(chars[0], ' ', chars[2]));
            }
        }
    }

    public void printGraph() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.print("\nAdjacency list of vertex: " + adjList.get(i).get(0).getSrc());
            for (int j = 0; j < adjList.get(i).size(); j++) {
                if (adjList.get(i).get(j).getDest() == ' ') {
                    System.out.print(" -->  End Node (" + adjList.get(i).get(j).getWeight() + ") ");
                } else {
                    System.out.print(" --> " + adjList.get(i).get(j).getDest() + "(" + adjList.get(i).get(j).getWeight() + ") ");
                }
            }
            System.out.println();
        }
    }

    public static int getIndex(ArrayList<ArrayList<Edge>> adj, char start) { //S
        for (int i = 0; i < adj.size(); i++) {
            Edge e = adj.get(i).get(0);
            if (e.getSrc() == start)
                return i;
        }
        return -1;
    }

    public boolean isValid(String sequence) {
        char key = 'S';
        if (sequence.indexOf('b') != sequence.length() - 1) {
            return false;
        }

        for (Character c : sequence.toCharArray()) {

            for (Edge e : adjList.get(getIndex(adjList, key))) {

                if (e.getWeight() == c) {
                    key = e.getDest();
                    break;
                } else if (adjList.get(getIndex(adjList, key)).indexOf(e) == adjList.get(getIndex(adjList, key)).size() - 1) {
                    return false;
                }
            }

            if (key == ' ' && sequence.indexOf(c) == sequence.length() - 1) {
                return true;
            } else if (key == ' ' && sequence.indexOf(c) != sequence.length() - 1) {
                return false;
            }
        }
        return true;
    }
}
