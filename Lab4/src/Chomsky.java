import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Chomsky {
    private final LinkedHashMap<String, ArrayList<String>> productions = new LinkedHashMap<>();

    public void readInput() throws IOException {
        File file = new File("U:\\SecondYear_Sem2\\LFPC\\Labs\\Lab4\\src\\Valentina.txt");
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
        System.out.println(productions);
    }

    //////////////////////////Finds the key with has an epsilon production: C-> # //////////////////////////
    public char hasEpsilon() {
        for (String nonTerminal : productions.keySet()) {
            if (productions.get(nonTerminal).contains("#")) {
                return nonTerminal.charAt(0);
            }
        }
        return ' ';
    }

    // loops through the productions and creates null states with the already found epsilon state
    public void epsilonProduction(char epsilon) {
        for (String key : productions.keySet()) {
            for (int values = 0; values < productions.get(key).size(); values++) {
                if (productions.get(key).get(values).contains(String.valueOf(epsilon))) {
                    createEpsilonCombinations(productions.get(key).get(values), epsilon, key);
                }
            }
        }
        productions.get(String.valueOf(epsilon)).remove("#");
        System.out.println(productions);
    }

    //creates the combinations based on indexes of each epsilon char in the string
    public void createEpsilonCombinations(String production, char epsilon, String key) {
        char[] set = production.toCharArray();
        ArrayList<Integer> index = new ArrayList<>(); //Store indexes pn which empty states can be found

        for (int i = 0; i < production.length(); i++) {  //AbAbA  -> A = eps
            if (set[i] == epsilon) {
                index.add(i); //0, 2, 4
            }
        }
        String indexString = index.toString();
        String digits = indexString.replaceAll("[^0-9]", "");
        char[] combinations = digits.toCharArray();
        printPowerSet(combinations, index.size(), production, key); //Gets all the combinations out of given indexes we found empty state in
    }

    //Function to print all the combinations of empty symbol: ABA -> AB, BA, B
    public void printPowerSet(char[] set, int set_size, String production, String key) {
        long pow_set_size = (long) Math.pow(2, set_size);
        int counter, j;
        for (counter = 0; counter < pow_set_size; counter++) {
            List<Character> indexList = new ArrayList<>();
            for (j = 0; j < set_size; j++) {
                if ((counter & (1 << j)) > 0) {
                    indexList.add(set[j]);
                }
            }
            if (!indexList.isEmpty())
                deleteCharAtIndex(production, indexList, key);
        }
    }

    //having all the possible combinations of eliminating chars in a string we eliminate the chars at those indexes and store the result
    public void deleteCharAtIndex(String production, List<Character> indexList, String key) {
        StringBuilder copy = new StringBuilder(production);
        for (int i = 0; i < indexList.size(); i++) {
            copy.deleteCharAt(Character.getNumericValue(indexList.get(i)) - i); //ACA
            if (!productions.get(key).contains(copy.toString()) && !copy.isEmpty()) // BaC -> Ba
                productions.get(key).add(copy.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////
///////////////////// Removes states that have no productions like C->___  , this happens if C->epsilon , at the start    /////////////////
    public void removeDeadStates() {
        for (String key : productions.keySet()) {
            if (productions.get(key).size() == 0) {
                findStatesContainingDeadStates(key);
                productions.remove(key);
                removeDeadStates();
                break;
            }
        }
    }

    //Finds all the states that have dead states: C -> # then AC and CA will be removed
    public void findStatesContainingDeadStates(String deadState) {
        for (String key : productions.keySet()) {
            for (int value = 0; value < productions.get(key).size(); value++) {
                if (productions.get(key).get(value).contains(deadState)) {
                    productions.get(key).remove(value);
                    findStatesContainingDeadStates(deadState);
                    break;
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////// Unit transitions ///////////////////////////////////////////////////////

    public void findUnitStates() {
        for (String key : productions.keySet()) {
            for (int value = 0; value < productions.get(key).size(); value++) {
                if (productions.get(key).get(value).length() == 1 && productions.get(key).get(value).equals(productions.get(key).get(value).toUpperCase())) {
                    //function call
                    unitProduction(key, productions.get(key).get(value));
                    productions.get(key).remove(productions.get(key).get(value));
                    findUnitStates();
                    break;
                }
            }
        }
    }

    public void unitProduction(String key, String unitProd) {
        for (String value : productions.get(unitProd)) {
            if (!productions.get(key).contains(value))
                productions.get(key).add(value);
        }
    }
//////////////////////////////////////////////////////////////////////////////

    public void checkReachableStates() {
        ArrayList<String> reacheableKeys = new ArrayList<>();
        reacheableKeys.add("S");
        for (int key = 0; key < reacheableKeys.size(); key++) {
            for (int value = 0; value < productions.get(reacheableKeys.get(key)).size(); value++) {
                //loop through each letter in the current string
                for (char letter : productions.get(reacheableKeys.get(key)).get(value).toCharArray()) {
                    if (Character.isUpperCase(letter) && !reacheableKeys.contains(String.valueOf(letter))) {
                        reacheableKeys.add(String.valueOf(letter));
                    }
                }
            }
        }
        System.out.println("Reacheable keys" + reacheableKeys);
        isNonReacheable(reacheableKeys);
    }

    public void isNonReacheable(ArrayList<String> reacheableKeys) {
        for (String key : productions.keySet()) {
            if (!reacheableKeys.contains(key)) {
                productions.remove(key);
                isNonReacheable(reacheableKeys);
                break;
            }
        }
    }


    //////////////////////////////////////Renaming step//////////////////////////////////////////
    //function that loop through each state and calls other functions based on the type
    public void createChomsky(ArrayList<String> keys) {
        //We loop through our keys array while it's not empty
        if (!keys.isEmpty()) {
            for (String key : keys) {
                //we loop through each production
                for (int value = 0; value < productions.get(key).size(); value++) {
                    String currentValue = productions.get(key).get(value);
                    //if it's not a valid production A->a or A->BC
                    if (!validProduction(currentValue)) {
                        String newProduction;
                        //Simple case of length 2 like Aa ->  AX where X->a
                        if (currentValue.length() == 2) {
                            newProduction = createProduction(currentValue);
                        }
                        //bCaCb returns FG for example so we replace bCaCb with FG
                        else {
                            newProduction = splitProduction(currentValue);
                        }
                        //add new transition and key to hashmap
                        ArrayList<String> newList = productions.get(key);  //copy of values at keys
                        newList.remove(value);
                        newList.add(newProduction);
                        productions.replace(key, newList);
                        //call function again since the array is now resized
                        createChomsky(keys);
                        break;
                    }
                }
                //printHashmap();
                //remove the key from passed array, so we don't loop through it again
                keys.remove(key);
                createChomsky(keys);
                break;
            }
        }
    }

    public boolean  validProduction(String production) {
        //if the production is of the form A -> AB  or A -> a then it's valid
        return (production.length() == 2 && production.equals(production.toUpperCase())) || production.length() == 1 && production.equals(production.toLowerCase());
    }

    //recursive function that takes a production as input for example bCab and
    public String splitProduction(String production) { // Aa | aAa
        //create a single production
        if (production.length() == 1) {
            return createFinalProduction(production);
        }
        //create a double production of the form: AA aa Aa
        else if (production.length() == 2) {
            String newProd = createProduction(production);
            return createFinalProduction(newProd);
        } else {
            int half = production.length() / 2;
            String firstHalf = splitProduction(production.substring(0, half));
            String secondHalf = splitProduction(production.substring(half));

            if (firstHalf.length() == 2) {
                firstHalf = createFinalProduction(firstHalf);
            }
            if (secondHalf.length() == 2) {
                secondHalf = createFinalProduction(secondHalf);
            }
            return firstHalf + secondHalf;
        }
    }

    //returns an existing production or creates a new one if it doesn't exist
    public String createFinalProduction(String production) {
        for (String key : productions.keySet()) {
            if (productions.get(key).size() == 1) {
                if (productions.get(key).contains(production)) {
                    return key;
                }
            }
        }
        return addTransition(production);
    }

    public String createProduction(String production) { //aA or Aa -> AX
        if (production.equals(production.toLowerCase())) {
            return createLowerCaseProduction(production);
        } else if (production.equals(production.toUpperCase())) {
            return production;
        } else {
                //get the small letter from a double production like Aa
            String smallLetter = "";
            for (char ch:production.toCharArray()) {
                if (Character.isLowerCase(ch))
                    smallLetter = String.valueOf(ch);
            }

            //check if there exists a transition that goes into a
            String newKey = "";
            for (String key : productions.keySet())
                if (productions.get(key).size() == 1 && productions.get(key).contains(smallLetter))
                    newKey = key;

            //if it doesn't exist we create a new tranistion
            if (newKey.equals(""))
                newKey = addTransition(smallLetter);

            // we replace the obtained production with the small letter
            return production.replace(smallLetter, newKey);
        }
    }

    // deal with productions of the form aa |  aba
    public String createLowerCaseProduction(String production) {
        StringBuilder newProduction = new StringBuilder();
        //We check for existing characters and if there doesnt exist we create a new transition and continue to the next letter
        for (char letter : production.toCharArray()) {
            boolean exists = false;
            for (String key : productions.keySet()) {
                if (productions.get(key).size() == 1 && productions.get(key).contains(String.valueOf(letter))) { // only keys with small letters, derive only in terminal
                    newProduction.append(key);
                    exists = true;
                    break;
                }
            }
            //create a new transition for the lower case letter
            if (!exists) {
                String newKey = addTransition(String.valueOf(letter));
                newProduction.append(newKey);
            }
        }
        return newProduction.toString();
    }

    // add a new transition to the hashmap
    public String addTransition(String production) {
        ArrayList<String> singleList = new ArrayList<>();
        singleList.add(String.valueOf(production));
        String newKey = getValidKey();
        productions.put(newKey, singleList);
        return newKey;
    }

    // get a valid new key
    public String getValidKey() {
        String keys = "ZYXWVUTSRQPONMLKJIHGFEDCBA";
        for (char key : keys.toCharArray()) {
            //returns a key which is not present in map
            if (!productions.containsKey(String.valueOf(key))) {
                return String.valueOf(key);
            }
        }
        return "";
    }


    //I do not know what this function does
    public void printHashmap() {
        System.out.println(productions);

    }

    public LinkedHashMap<String, ArrayList<String>> getProductions() {
        return productions;
    }
}
