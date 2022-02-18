import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Provide your input below. When finished type !!!\"exit\"!!!");

        LinkedHashMap<String, ArrayList<Edge>> adjList = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<Edge>> adjListNFA = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<Edge>> adjListDFA = new LinkedHashMap<>();
        ArrayList<String> vertices = new ArrayList<>();
        Graph FA = new Graph(adjList, vertices);

        while (true) {
            //Input S aB
            String userInput = sc.nextLine();
            if (userInput.equals("exit") || userInput.equals("EXIT") || userInput.equals("Exit")) {
                break;
            } else {
                FA.addEdge(userInput);
            }
        }

        System.out.println("\n" + "*************** I am Finite automation state ***************" + "\n");
        FA.printGraph();

        System.out.println("\n" + "*************** I am NFA state ***************" + "\n");
        NFA nfa = new NFA(FA, adjListNFA);
        nfa.graphToNFA();
        nfa.printNFA();

        System.out.println("\n" + "*************** I am DFA state ***************" + "\n");
        DFA dfa = new DFA(nfa, adjListDFA);
        dfa.nfaToDfa();
        dfa.printDFA();

    }
}
/*
q0 a q1
q1 b q2
q2 c q0
q1 a q3
q0 b q2
q2 c q3
*/
