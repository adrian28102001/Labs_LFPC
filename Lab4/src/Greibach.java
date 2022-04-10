import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Greibach extends Chomsky {
    private final LinkedHashMap<String, ArrayList<String>> productions = getProductions();
    StringBuilder keys = new StringBuilder("ZYXWVUTSRQPONMLKJIHGFEDCBA");

    public boolean findGreibachTransitions() {
        for (String key : productions.keySet()) {
            for (String value : productions.get(key)) {
                if (!isGreibachForm(value)) { // S-> AB   A->SA  value - AB
                    String firstChar = String.valueOf(value.charAt(0));

                    //Eliminate recursion
                    if (firstChar.equals(key)) { // S->SA
                        removeRecursion(value, firstChar);
                        return false;
                    }
                    // check indirect recursion
                    else if (firstChar.equals(firstChar.toUpperCase())) {
                        //eliminate indirect recursion
                        if (isIndirectRecursion(value, key)) {
                            //we break and start again
                            createRecursion(value, key, firstChar);
                            return false;
                        }
                        else {
                            //copy contents of first letter to the value
                            //A -> BcA
                            //B -> bD
                            //A -> bDcA
                            unitProd(value, key);
                            return false;
                        }
                    } else if (firstChar.equals(firstChar.toLowerCase())) {
                        smallLetter(value, key);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isGreibachForm(String value) {
        if (value.length() == 1 && Character.isLowerCase(value.charAt(0))) {
            return true;
        } else if (Character.isLowerCase(value.charAt(0))) {
            String sub = value.substring(1);
            if (sub.equals(sub.toUpperCase())) return true;
        }
        return false;
    }

    // S-> SAB  key = S  recursion = SAB
    public void removeRecursion(String recursion, String key) {
        ArrayList<String> newProductions = new ArrayList<>();
        String newKey = getValidChar("");

        for (String value : productions.get(key)) {
            StringBuilder newValue = new StringBuilder(value);
            //if the start letter is not recursive we put the new key to the end
            // S-> AB then X -> ABX
            if (!String.valueOf(value.charAt(0)).equals(key)) {
                newValue.append(newKey);
                newProductions.add(String.valueOf(newValue));
            }
            //if its recursive we remove the recursive key at the start and add the newKey to the end
            else {
                newValue.deleteCharAt(0);
                newValue.append(newKey);
                ArrayList<String> singleList = new ArrayList<>();
                singleList.add(String.valueOf(newValue));
                if (productions.containsKey(newKey)) {
                    productions.get(newKey).add(String.valueOf(newValue));
                } else {
                    productions.put(newKey, singleList);
                }

            }

        }
        productions.get(key).remove(recursion);
        productions.get(key).addAll(newProductions);
    }

    public boolean isIndirectRecursion(String value, String recursiveKey) {
        String firstKey = String.valueOf(value.charAt(0));
        for (String key : productions.get(firstKey)) {
            if (String.valueOf(key.charAt(0)).equals(recursiveKey)) {
                return true;
            }
        }
        return false;
    }

    //S Ba
    //B Sa this will create recursion
    //copy all B to S
    public void createRecursion(String value, String recursiveKey, String firstChar) {
        ArrayList<String> newProductions = new ArrayList<>();

        for (String key : productions.get(firstChar)) {
            StringBuilder newProduction = new StringBuilder(value);
            newProduction.replace(0, 1, key);
            newProductions.add(String.valueOf(newProduction));
        }
        //we added new transitions to arraylist and now we have to remove the recursive value
        productions.get(recursiveKey).remove(value);
        productions.get(recursiveKey).addAll(newProductions);
    }

    public void smallLetter(String value, String key) {
        //value = bAcB  c->X  so we get  bAXB
        StringBuilder newValue = new StringBuilder(value);
        for (int i = 1; i < value.length(); i++) {
            if (Character.isLowerCase(value.charAt(i))) {
                //create a new key for that letter
                String newKey = getValidChar(String.valueOf(value.charAt(i)));
                ArrayList<String> singleList = new ArrayList<>();
                singleList.add(String.valueOf(value.charAt(i)));
                productions.put(newKey, singleList);

                // replace letter with new key
                newValue.replace(i, i + 1, newKey);
            }
        }
        productions.get(key).add(String.valueOf(newValue));
        productions.get(key).remove(value);
    }

    public void unitProd(String value, String key) {
        String firstKey = String.valueOf(value.charAt(0));
        ArrayList<String> newProductions = new ArrayList<>();
        for (String element : productions.get(firstKey)) {
            StringBuilder newValue = new StringBuilder(value);
            newValue.replace(0, 1, element);

            newProductions.add(String.valueOf(newValue));
        }
        productions.get(key).addAll(newProductions);
        productions.get(key).remove(value);
    }

    public String getValidChar(String smallLetter) {
        for (String key : productions.keySet()) {
            if (productions.get(key).size() == 1 && productions.get(key).contains(smallLetter)) {
                return key;
            }
        }
        for (int key = 0; key < keys.length(); key++) {
            if (!productions.containsKey(String.valueOf(key))) {
                keys.deleteCharAt(key);
                return String.valueOf(keys.charAt(key));
            }
        }
        return " ";
    }

    @Override
    public void printHashmap() {
        System.out.println(productions);
    }
}
