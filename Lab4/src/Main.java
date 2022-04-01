import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        Chomsky chomsky = new Chomsky();
        System.out.println("Original");
        chomsky.readInput();

        System.out.println("Remove empty");
        while (chomsky.hasEpsilon() != ' ') {
            char eps = chomsky.hasEpsilon();
            chomsky.epsilonProduction(eps); //AB or BA or B
        }
        System.out.println("Remove dead");
        chomsky.removeDeadStates();
        chomsky.printHashmap();
        chomsky.findUnitStates();
        System.out.println("Unit productions");
        chomsky.printHashmap();
        chomsky.checkReachableStates();
        chomsky.printHashmap();
        chomsky.createChomsky(new ArrayList<>(chomsky.getProductions().keySet()));
        System.out.println("Renamed States");
        chomsky.printHashmap();
    }
}
