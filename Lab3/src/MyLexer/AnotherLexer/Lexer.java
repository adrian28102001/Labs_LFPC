package AnotherLexer;

import java.util.ArrayList;
import java.util.HashMap;

import static AnotherLexer.TokenType.*;

public class Lexer {
    private String input;
    private ArrayList<Token> tokens = new ArrayList<>();
    //    private static String OPERATION_CHARS = "><!=";
    private int current = 0;
    private int line = -1;
    private int length;
    private boolean error = false;

    private static final HashMap<String, TokenType> punctuationTokens = new HashMap<>();

    static {
        punctuationTokens.put("{", LBRACE);
        punctuationTokens.put("}", RBRACE);
        punctuationTokens.put("(", LPAREN);
        punctuationTokens.put(")", RPAREN);
        punctuationTokens.put("[", LBRACKET);
        punctuationTokens.put("]", RBRACKET);
        punctuationTokens.put("/", SLASH);
        punctuationTokens.put("*", MULT);
        punctuationTokens.put("-", MINUS);
        punctuationTokens.put("+", PLUS);
        punctuationTokens.put("%", MOD);
        punctuationTokens.put(",", COMMA);
        punctuationTokens.put(".", DOT);
        punctuationTokens.put(";", SEMICOLON);
        punctuationTokens.put(":", COLON);
    }

    private static final HashMap<String, TokenType> operationTokens = new HashMap<>();

    static {
        operationTokens.put("<", SMALLER);
        operationTokens.put(">", BIGGER);
        operationTokens.put("!", NEGATION);
        operationTokens.put("=", EQUAL);
    }

    private static final HashMap<String, TokenType> keywordTokens = new HashMap<>();

    static {
        keywordTokens.put("main", MAIN);
        keywordTokens.put("int", INT);
        keywordTokens.put("double", DOUBLE);
        keywordTokens.put("boolean", BOOLEAN);
        keywordTokens.put("array", ARRAY);
        keywordTokens.put("char", CHAR);
        keywordTokens.put("string", STRING);
        keywordTokens.put("for", FOR);
        keywordTokens.put("while", WHILE);
        keywordTokens.put("do", DO);
        keywordTokens.put("if", IF);
        keywordTokens.put("elif", ELIF);
        keywordTokens.put("else", ELSE);
        keywordTokens.put("function", FUNCTION);
        keywordTokens.put("return", RETURN);
        keywordTokens.put("&&", AND);
        keywordTokens.put("||", OR);
        keywordTokens.put("true", TRUE);
        keywordTokens.put("false", FALSE);
        keywordTokens.put("print", PRINT);
    }


    public Lexer() {
    }

    public void initializer(String inp) {
        line++;
        input = inp;
        current = 0;
        length = inp.length();
        error = false;
    }

    public void tokenizer(String inp) {
        initializer(inp);

        while (current < length) {
            //Ignore white space
            if (input.charAt(current) == ' ') {
                increaseCurrent();
            }
            //Create a punctuation :
            else if (punctuationTokens.containsKey(Character.toString(input.charAt(current)))) {
                setPunctuationTokens(Character.toString(input.charAt(current)));
            }
            //Create a number
            else if (Character.isDigit(input.charAt(current))) {
                setDigitTokens(String.valueOf(input.charAt(current)));
            }
            //Create a word
            else if (Character.isAlphabetic(input.charAt(current))) {
                setKeywordTokens();
            }
            //Create a operation
            else if (operationTokens.containsKey(Character.toString(input.charAt(current)))) {
                setOperationTokens();
            }
        }

    }

    public void setPunctuationTokens(String punctuationToken) {
        Token token = new Token(punctuationToken, punctuationTokens.get(punctuationToken));
        tokens.add(token);
        increaseCurrent();
    }

    public void setOperationTokens() {
        int position = current;
        String operation = "";

        while (operationTokens.containsKey(String.valueOf(input.charAt(position)))) {
            if (position - current > 2) {
                error = true;
                System.out.println("Too many operators error!");
            }
            position++;
        }
        operation = input.substring(current, position);
        Token token;
        if (!error)
            switch (operation) {
                case "<":
                    token = new Token(operation, SMALLER);
                    tokens.add(token);
                    break;
                case "<=":
                    token = new Token(operation, SMALLER_EQUAL);
                    tokens.add(token);
                    break;
                case ">=":
                    token = new Token(operation, BIGGER_EQUAL);
                    tokens.add(token);
                    break;
                case ">":
                    token = new Token(operation, BIGGER);
                    tokens.add(token);
                    break;
                case "!=":
                    token = new Token(operation, NEGATION_EQUAL);
                    tokens.add(token);
                    break;
                case "!":
                    token = new Token(operation, NEGATION);
                    tokens.add(token);
                    break;
                case "=":
                    token = new Token(operation, EQUAL);
                    tokens.add(token);
                    break;
                case "==":
                    token = new Token(operation, EQUAL_EQUAL);
                    tokens.add(token);
                    break;
                default:
                    System.out.println("Operation Error!");
            }
        current = position;
    }

    public void setKeywordTokens() {
        int position = current;
        String str = "";

        while ( position != length) {
            if (Character.isAlphabetic(input.charAt(position)) || Character.isDigit(input.charAt(position)))
                position++;
            else break;
        }
        str = input.substring(current, position);
        if (keywordTokens.containsKey(str)) {
            Token token = new Token(str.toString(), keywordTokens.get(str));
            tokens.add(token);
        } else {
            Token token = new Token(str.toString(), IDENTIFIER);
            tokens.add(token);
        }
        current = position;
    }

    public void setDigitTokens(String digitToken) {
        //increments while current character is a number
        int position = current, dotCounter = 0;
        // stores the resulting number in a string
        String number = "";
        while (true) {
            //if we exceed length of input string or we encounter a char which isn't a digit or point we BREAK
            if (position >= input.length()) break;
            if (!Character.isDigit(input.charAt(position)) && input.charAt(position) != '.') {
                //we throw an error if a number is in the wrong format like 123a45, grammar mistake made
                if (Character.isAlphabetic(input.charAt(position))) {
                    error = true;
                }
                break;
            }

            //We check to see if we only have one dot between numbers to exclude 1.1.2 scenario
            if (input.charAt(position) == '.') dotCounter++;

            position++;
        }

        //Store resulting string
        number = input.substring(current, position);

        //Configure token based on whether it found a dot or not
        if (!error) {
            if (dotCounter == 0) {
                Token token = new Token(number, INT);
                tokens.add(token);
            } else if (dotCounter == 1) {
                Token token = new Token(number, DOUBLE);
                tokens.add(token);
            }
        } else System.out.println("Syntax Error! Wrong number format! Check line: " + line);
        current = position;
    }

    public char setCurrent(int current) {
        if (current >= input.length())
            return '\0';
        return input.charAt(current);
    }

    public void increaseCurrent() {
        current++;
    }

    public void printTokens() {
        for (Token t : tokens) {
            System.out.println(t.tokenName + " " + t.type.toString());
        }
    }
}
