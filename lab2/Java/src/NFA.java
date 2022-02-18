import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

//Grath to NFA
public class NFA {
    Graph graph;
    LinkedHashMap<String, ArrayList<Edge>> nfa;

    public NFA(Graph graph, LinkedHashMap<String, ArrayList<Edge>> nfa) {
        this.graph = graph;
        this.nfa = nfa;
    }

    public HashMap<String, ArrayList<Edge>> getNfa() {
        return nfa;
    }

    public void graphToNFA() {
        //for each state we loop through its array
        for (String src : graph.getAdjList().keySet()) {
            if (graph.getAdjList().get(src).isEmpty()) {
                nfa.put(src, new ArrayList<Edge>());
            }
            //we store the unique weights that the state has in an array
            ArrayList<String> weights = uniqueWeights(graph.getAdjList().get(src));
            //for each weight in the list we find the edges that have the same weight
            for (String weight : weights) {
                //We create an array of edges that have the same weight q0q1, q1q2
                //for each weight of the list we call weightArray function that returns the array of edges that have the same weight
                ArrayList<Edge> edgesSameWeight = weightArray(graph.getAdjList().get(src), weight);

                //Concatenate nodes with same weight
                String newState = "";
                for (Edge e : edgesSameWeight) {
                    newState += e.getDest();
                }
                Edge newStateEdge = new Edge(src, newState, weight);
                if (nfa.containsKey(src)) {
                    //appending to existing src
                    nfa.get(src).add(newStateEdge);
                } else {
                    //creating new src and appending to it the new edge
                    nfa.put(src, new ArrayList<Edge>());
                    nfa.get(src).add(newStateEdge);
                }
            }
        }
    }


    //returns an array of edges that have the same weight that is specified
    public ArrayList<Edge> weightArray(ArrayList<Edge> list, String weight) {
        ArrayList<Edge> outputEdge = new ArrayList<>();
        for (Edge edge : list) {
            if (edge.getWeight().equals(weight)) {
                outputEdge.add(edge);
            }
        }
        return outputEdge;
    }

    public ArrayList<String> uniqueWeights(ArrayList<Edge> list) {
        ArrayList<String> outputWeights = new ArrayList<>();
        for (Edge edge : list) {
            if (!outputWeights.contains(edge.getWeight())) {
                outputWeights.add(edge.getWeight());
            }
        }
        return outputWeights;
    }

    public ArrayList<String> uniqueWeightsVoid() {
        ArrayList<String> weights = new ArrayList<>();

        for (String s : nfa.keySet()) {
            for (Edge e : nfa.get(s)) {
                if (!weights.contains(e.getWeight())) {
                    weights.add(e.getWeight());
                }
            }
        }
        return weights;
    }

    public void printNFA() {
        String endState = "";
        int nOfElements=nfa.keySet().size()-1;
        int count = 0;
        for (String key: nfa.keySet()){
            if (count==nOfElements){
                endState = key;
            }
            count++;
        }

        for (String s : nfa.keySet()) {
            if (s.contains(endState) && !endState.equals("")){
                System.out.print("*" + s + " : ");
            }
            else if (s.equals("q0")){
                System.out.print("->" + s + " : ");
            }
            else{
                System.out.print(s + " : ");
            }
            for (Edge e : nfa.get(s))
                e.printEdge();
            System.out.println();
        }
    }


}
