import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Greibach greibach = new Greibach();
        greibach.ChomskyMain();

        while(greibach.findGreibachTransitions()){

        }
        System.out.println("Greibach form");
        greibach.printHashmap();

    }
}
