package MyLexer;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        // File path is passed as parameter
        File file = new File("U:\\SecondYear_Sem2\\LFPC\\Labs\\Lab3\\src\\MyLexer\\program.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        MyLexer.Lexer lexer = new MyLexer.Lexer();
        while ((st = br.readLine()) != null){
            // Print the string
            lexer.tokenizer(st);
            System.out.println(st);
        }
        System.out.println();
        lexer.printTokens();

    }
    //
}
