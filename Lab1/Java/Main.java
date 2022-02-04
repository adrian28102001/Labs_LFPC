package LABFA;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Provide your input below. When finished type !!!\"exit\"!!!");
        ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();
        ArrayList<Character> vertices = new ArrayList<>();
        Graph FA = new Graph(adjList, vertices);

        while (true) {
            //Input
            String userInput = sc.nextLine();
            if (userInput.equals("exit") || userInput.equals("EXIT") || userInput.equals("Exit")) {
                break;
            } else {
                FA.addEdge(userInput);
            }
        }

        FA.printGraph();
    }
}
/*
SAMPLE INPUT
S aA
S aB
A aA
B bB
A a
B b
exit
 */
