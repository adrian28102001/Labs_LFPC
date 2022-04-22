import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Precedence {
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
            if (isLower && (!firstHashmap.get(value).contains(firstChar))) { //if first letter is small we add it to firstHashmap
                firstHashmap.get(value).add(firstChar);
            } else {
                if (!firstHashmap.get(value).contains(firstChar)) {
                    firstHashmap.get(value).add(firstChar);
                    if (firstHashmap.get(firstChar).isEmpty()) {  //if first letter has not already been called
                        first(firstChar, value);
                    } else {
                        for (String val : firstHashmap.get(firstChar)) { //if first letter has been called, we copy her values
                            if (!firstHashmap.get(value).contains(val))
                                firstHashmap.get(value).add(val);
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
                    if (everyChar.indexOf(String.valueOf(key)) == -1) { //does not
                        everyChar.append(key);
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
                equalPrecedence(value);
                smallerPrecedence(value);
                biggerPrecedence(value);
                dollarRelation();
            }
        }
    }

    public void equalPrecedence(String production) { //bA        ABCab
        if (production.length() > 1) {
            for (int i = 0; i < production.length() - 1; i++) {
                int index = everyChar.indexOf(production.charAt(i)); //4
                int index2 = everyChar.indexOf(production.charAt(i + 1)); //4
                precedence[index][index2] = "=";
            }
        }
    }

    public void smallerPrecedence(String production) {
        for (int i = 1; i < production.length(); i++) {
            if (Character.isUpperCase(production.charAt(i))) {
                int index = everyChar.indexOf(production.charAt(i - 1)); //4
                for (String value : firstHashmap.get(String.valueOf(production.charAt(i)))) {
                    int index2 = everyChar.indexOf(value.charAt(0));
                    precedence[index][index2] = "<";
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
                        precedence[index2][index] = ">";
                    }
                } else {
                    for (String value : lastHashmap.get(String.valueOf(production.charAt(i)))) {
                        int index = everyChar.indexOf(value.charAt(0)); //4
                        for (String first : firstHashmap.get(String.valueOf(production.charAt(i + 1)))) {
                            if (first.equals(first.toLowerCase())) {
                                int index2 = everyChar.indexOf(first.charAt(0)); //4
                                precedence[index][index2] = ">";
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    public void dollarRelation() {
        ArrayList<String> firstKeys = new ArrayList<>();
        for (String key : firstHashmap.keySet()) {
            for (String value : firstHashmap.get(key)) {
                if (!firstKeys.contains(value)) {
                    firstKeys.add(value);
                }
            }
        }
        for (String key : firstKeys) {
            int index = everyChar.indexOf(key.charAt(0));
            precedence[everyChar.indexOf("$")][index] = "<";
        }

        ArrayList<String> lastKeys = new ArrayList<>();
        for (String key : lastHashmap.keySet()) {
            for (String value : lastHashmap.get(key)) {
                if (!lastKeys.contains(value)) {
                    lastKeys.add(value);
                }
            }
        }
        for (String key : lastKeys) {
            int index = everyChar.indexOf(key.charAt(0));
            precedence[index][everyChar.indexOf("$")] = ">";
        }
    }


    public void initializeRelationsWord(String string) { //$dabacbaa$
        StringBuilder resultString = new StringBuilder();
        int firstPosition, secPosition;
        for (int i = 0; i < string.length() - 1; i++) {
            firstPosition = everyChar.indexOf(string.charAt(i));
            secPosition = everyChar.indexOf(string.charAt(i + 1));
            resultString.append(string.charAt(i));
            resultString.append(precedence[firstPosition][secPosition]);
        }
        resultString.append("$");
        System.out.println(resultString);
    }

    public void checkTheString(String string) {
        System.out.println(string);
        if (string.equals("$<S>$")) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(string);// $<d<a<b<a>c<b<a>a>$
        for (int i = 1; i < string.length() - 1; i += 2) {
            if (string.charAt(i) == '<' && string.charAt(i + 2) == '>') {
                String letter = String.valueOf(string.charAt(i + 1));  //<a> => a is etter
                String replaceKey = replaceLetter(letter);  // B => a so we replace a with B
                if (!replaceKey.equals(" ")) {
                    stringBuilder.replace(i + 1, i + 2, replaceKey);  //replace a with B
                    stringBuilder = replaceSigns(i + 1, stringBuilder, replaceKey); //$<d<a<b=B>c<b<a>a$

                    checkTheString(stringBuilder.toString()); //$<d<a<b=B>c<b<a>a$
                    break;
                }
            } else if (string.charAt(i) == '=') {
                String result = findNewString(new StringBuilder(string), i);
                if (!result.equals(string)) {
                    checkTheString(result);
                    break;
                }
            }

        }
//        return "";
    }

    //Replaces the signs
    public StringBuilder replaceSigns(int i, StringBuilder stringLetter, String replaceKey) {
        char charBefore = stringLetter.charAt(i - 2);
        char charAfter = stringLetter.charAt(i + 2);
        String sign = precedence[everyChar.indexOf(charBefore)][everyChar.indexOf(replaceKey)];
        stringLetter.replace(i - 1, i, sign);
        sign = precedence[everyChar.indexOf(replaceKey)][everyChar.indexOf(charAfter)];
        stringLetter.replace(i + 1, i + 2, sign);

        return stringLetter;
    }


    //this function finds the quality and replaces it ex: b=c  is replaced with D
    public String findNewString(StringBuilder stringBuilder, int startFrom) {
        StringBuilder equalLetters = new StringBuilder(); //All the letters equal between them ex : a=B=c => aBc
        int i = startFrom;
        for (; i < stringBuilder.length() - 1; i += 2) {
            if (stringBuilder.charAt(i) == '=') {
                equalLetters.append(stringBuilder.charAt(i - 1));
            } else {
                break;
            }
        }
        equalLetters.append(stringBuilder.charAt(i - 1));
        StringBuilder toReplace = stringBuilder;
        String keyToReplaceWith = replaceLetter(equalLetters.toString());
        if (!keyToReplaceWith.equals(" ")) {
            toReplace.replace(startFrom - 1, i, keyToReplaceWith);
            stringBuilder = new StringBuilder(toReplace); //$<d<a<D>c<b<a>a>$
            if (stringBuilder.toString().equals("$<S>$")) {
                return String.valueOf(stringBuilder);
            }
            //After we changed the letters that are equal, we need to change their signs too
            replaceSigns(startFrom - 1, stringBuilder, keyToReplaceWith);
        }
        return String.valueOf(stringBuilder);
    }

    //Searches for a key that derives in our production: ex  S ->ab
    public String replaceLetter(String string) {
        for (String key : productions.keySet()) {
            for (String value : productions.get(key)) {
                if (value.equals(string)) {
                    return key;
                }
            }
        }
        return " ";
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
                    System.out.print(" " + precedence[i][j]);
            }
            System.out.println();
        }
    }

    public void main() throws IOException {
        System.out.println("Initial production");
        readInput();
        for (String key : productions.keySet()) {
            first(key, key);
            last(key, key);
        }
        createTableIndexes();
        establishingRelations();
        System.out.println("First");
        printHashmap(firstHashmap);
        System.out.println("Last");
        printHashmap(lastHashmap);
        System.out.println();
        printArray();
        System.out.println();
        initializeRelationsWord("$dabacbaa$");
        checkTheString("$<d<a<b<a>c<b<a>a>$");

    }

}