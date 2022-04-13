import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class ReadInput {
    protected final LinkedHashMap<String, ArrayList<String>> productions = new LinkedHashMap<>();
    protected final LinkedHashMap<String, ArrayList<String>> firstHashmap = new LinkedHashMap<>();
    protected final LinkedHashMap<String, ArrayList<String>> lastHashmap = new LinkedHashMap<>();
    protected String[][] precedence;
    protected String everyChar;


    public void initialing() {
        for (String key : productions.keySet()) {
            firstHashmap.put(key, new ArrayList<>());
            lastHashmap.put(key, new ArrayList<>());
        }

    }

    public void readInput() throws IOException {
        File file = new File("U:\\SecondYear_Sem2\\LFPC\\Labs\\Lab5\\src\\Input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String string;
        while ((string = br.readLine()) != null) { //A aB
            if (!productions.containsKey(string.substring(0, 1))) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(string.substring(2));
                productions.put(String.valueOf(string.charAt(0)), arrayList);
            } else {
                productions.get(String.valueOf(string.charAt(0))).add(string.substring(2));
            }
        }
        initialing();
        System.out.println(productions);
    }


    public void first(String key, String value) { // S -> Ab     A
        for (String valueAtKey : productions.get(key)) {
            String firstChar = String.valueOf(valueAtKey.charAt(0));
            boolean isLower = firstChar.equals(firstChar.toLowerCase());
            if (isLower) { //if first letter is small we add it to firstHashmap
                if (!firstHashmap.get(value).contains(firstChar)) {
                    firstHashmap.get(value).add(firstChar);
                }
            } else {
                if (!firstHashmap.get(value).contains(firstChar)) {
                    firstHashmap.get(value).add(firstChar);
                    if (firstHashmap.get(firstChar).isEmpty()) {  //if first letter has not already been called
                        first(firstChar, value);
                    } else {
                        for (String val : firstHashmap.get(firstChar)) { //if first letter has been called, we copy her values
                            if (!firstHashmap.get(value).contains(val)) firstHashmap.get(value).add(val);
                        }
                    }
                }
            }
        }
    }

    public void last(String key, String value) { // S -> Ab     A
        for (String valueAtKey : productions.get(key)) {
            String lastChar = String.valueOf(valueAtKey.charAt(valueAtKey.length() - 1));
            boolean isLower = lastChar.equals(lastChar.toLowerCase());
            if (isLower) { //if first letter is small we add it to firstHashmap
                if (!lastHashmap.get(value).contains(lastChar)) {
                    lastHashmap.get(value).add(lastChar);
                }
            } else {
                if (!lastHashmap.get(value).contains(lastChar)) {
                    lastHashmap.get(value).add(lastChar);
                    if (lastHashmap.get(lastChar).isEmpty()) {  //if first letter has not already been called
                        last(lastChar, value);
                    } else {
                        for (String val : lastHashmap.get(lastChar)) { //if first letter has been called, we copy her values
                            if (!lastHashmap.get(value).contains(val)) lastHashmap.get(value).add(val);
                        }
                    }
                }
            }
        }
    }

    public void createTableIndexes() {
        StringBuilder everyChar = new StringBuilder();
        for (String key : productions.keySet()) {
            for (String value : productions.get(key)) {
                for (Character characters : value.toCharArray()) {
                    if (everyChar.indexOf(String.valueOf(characters)) == -1) { //does not
                        everyChar.append(characters);
                    }
                }
            }
        }
        everyChar.append("$");
        int size = everyChar.length();
        precedence = new String[size][size];
        this.everyChar = String.valueOf(everyChar);
    }

    public void establishingRelations() {
        for (String key : productions.keySet()) {
            for (String value : productions.get(key)) {
                //FunctionCAll
                equalPrecedence(value);
                smallerPrecedence(value);
                biggerPrecedence(value);
            }
        }
    }

    public void equalPrecedence(String production) { //bA        ABCab
        if (production.length() > 1) {
            for (int i = 0; i < production.length() - 1; i++) {
                int index = everyChar.indexOf(production.charAt(i)); //4
                int index2 = everyChar.indexOf(production.charAt(i + 1)); //4
                precedence[index][index2] = " =";
            }
        }
    }

    public void smallerPrecedence(String production) {
        for (int i = 1; i < production.length(); i++) {
            if (Character.isUpperCase(production.charAt(i))) {
                int index = everyChar.indexOf(production.charAt(i - 1)); //4
                for (String value : firstHashmap.get(String.valueOf(production.charAt(i)))) {
                    int index2 = everyChar.indexOf(value.charAt(0));
                    precedence[index][index2] = " <";
                }
                break;
            }
        }
    }

    public void biggerPrecedence(String production) {
        for (int i = 0; i < production.length() - 1; i++) {
            if (Character.isUpperCase(production.charAt(i))) { //BV
                if (Character.isLowerCase(production.charAt(i + 1))) {
                    int index = everyChar.indexOf(production.charAt(i + 1)); //4
                    for (String value : lastHashmap.get(String.valueOf(production.charAt(i)))) {
                        int index2 = everyChar.indexOf(value.charAt(0));
                        precedence[index2][index] = " >";
                    }
                    break;
                } else {
                    for (String value : lastHashmap.get(String.valueOf(production.charAt(i)))) {
                        int index = everyChar.indexOf(value.charAt(0)); //4
                        for (String first : firstHashmap.get(String.valueOf(production.charAt(i + 1)))) {
                            if (first.equals(first.toLowerCase())) {
                                int index2 = everyChar.indexOf(first.charAt(0)); //4
                                precedence[index][index2] = " >";
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void printHashmap(LinkedHashMap<String, ArrayList<String>> hashMap) {
        System.out.println(hashMap);
    }

    public void printArray() {
        System.out.print(" ");
        for (int i = 0; i < everyChar.length(); i++) {
            System.out.print(" " + everyChar.charAt(i));
        }
        System.out.println();
        for (int i = 0; i < everyChar.length(); i++) {
            System.out.print(everyChar.charAt(i));
            for (int j = 0; j < everyChar.length(); j++) {
                if (precedence[i][j] == null) {
                    System.out.print(" -");
                } else
                    System.out.print(precedence[i][j]);
            }
            System.out.println();
        }
    }

    public void main() throws IOException {
        readInput();
        for (String key : productions.keySet()) {
            first(key, key);
            last(key, key);
        }
        createTableIndexes();
        establishingRelations();
        printHashmap(firstHashmap);
        printHashmap(lastHashmap);
        System.out.println();
        printArray();
    }

}