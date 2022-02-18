import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DFA {
    NFA nfa;
    LinkedHashMap<String, ArrayList<Edge>> dfa;


    public DFA(NFA nfa, LinkedHashMap<String, ArrayList<Edge>> dfa) {
        this.nfa = nfa;
        this.dfa = dfa;
    }

    public void nfaToDfa() {
        dfa.put("q0", nfa.getNfa().get("q0")); //q0 : (a) q1 , (b) q2

        //until we don't find a new empty state
        while (!findNewState().equals("empty")) {
            String newState = findNewState();
            //if the state is single
            if (newState.length() == 2) {
                //we put that new state in dfa!
                dfa.put(newState, nfa.getNfa().get(newState));
            } else { //if the state is not single
                dfa.put(newState, new ArrayList<Edge>());
                concatenateNodes(newState);
            }
        }

    }


    public void concatenateNodes(String nodes) {
        String[] nodesList = usingSplitMethod(nodes);//q0 q1
        ArrayList<String> weights = nfa.uniqueWeightsVoid();
        for (String weight : weights) {
            String resultNode = ""; //q0q1q0
            for (String node : nodesList) {
                if (!findEdgeWithWeight(node, weight).equals("")) {
                    resultNode += findEdgeWithWeight(node, weight);
                }
            }
            if (!resultNode.equals("")) {
                resultNode = removeDuplicates(resultNode);
                Edge newNode = new Edge(nodes, resultNode, weight);
                dfa.get(nodes).add(newNode);
            }
        }
    }

    //Removing duplicates from string
    public String[] usingSplitMethod(String text) {
        return text.split("(?<=\\G.{" + 2 + "})");
    }

    public String removeDuplicates(String s) {
        String[] variables = usingSplitMethod(s); //q0 q0 q1 q2
        String result = "";
        for (String node : variables) {
            if (!result.contains(node)) {
                result += node;
            }
        }
        return result;
    }

    //Searches through a specific array list of a node and find an edge that has a specific weight
    public String findEdgeWithWeight(String node, String weight) {
        for (Edge e : nfa.getNfa().get(node)) {
            if (e.getWeight().equals(weight)) {
                return e.getDest();
            }
        }
        return "";
    }

    //loops through whole dfa and finds states that haven't been added to dfa yet
    public String findNewState() {
        for (String s : dfa.keySet()) {
            for (Edge edge : dfa.get(s)) {
                if (!dfa.containsKey(edge.getDest())) {
                    return edge.getDest();
                }
            }
        }
        return "empty";
    }


    public void printDFA() {
        String endState = "";
        int nOfElements = nfa.getNfa().keySet().size() - 1;
        int count = 0;
        for (String key : nfa.getNfa().keySet()) {
            if (count == nOfElements) {
                endState = key;
            }
            count++;
        }

        for (String s : dfa.keySet()) {
            if (s.contains(endState) && !endState.equals("")) {
                System.out.print("*" + s + " : ");
            } else if (s.equals("q0")) {
                System.out.print("->" + s + " : ");
            } else {
                System.out.print(s + " : ");
            }

            for (Edge e : dfa.get(s))
                e.printEdge();
            System.out.println();
        }

        System.out.println("\n"+"*************** PythonInput ***************");
        for (String s : dfa.keySet()) {
            for (Edge e : dfa.get(s)) {
                System.out.println(e.getSrc() + " " + e.getWeight() + " " + e.getDest());
            }
        }
    }
}
