package MyLexer;

public enum TokenType {
    //////////////////////SINGLE CHARACTER/////////////////////////

    /////Punctuation
    DOT, COMMA, SEMICOLON, COLON, LBRACE, RBRACE, LPAREN, RPAREN, LBRACKET, RBRACKET,

    /////Operation
    PLUS, MINUS, SLASH, MOD, MULT,
    //////////////////////SINGLE CHARACTER/////////////////////////

    /////Comparisons
    SMALLER, BIGGER, EQUAL, NEGATION, NEGATION_EQUAL, EQUAL_EQUAL, BIGGER_EQUAL, SMALLER_EQUAL,

    /////Keywords
    INT, DOUBLE, BOOLEAN, ARRAY, CHAR, STRING, FOR, WHILE, DO, IF, ELSE,
    ELIF, FUNCTION, MAIN, RETURN, AND, OR, TRUE, FALSE, PRINT,
    IDENTIFIER,

    //End of file
    EOF
}
