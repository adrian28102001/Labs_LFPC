package MyLexer;

public class Token {
    String tokenName;
    MyLexer.TokenType type;



    public Token(String tokenName, MyLexer.TokenType type) {
        this.tokenName = tokenName;
        this.type = type;
    }
    public void printToken(){
        System.out.println(tokenName + type.toString());
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public MyLexer.TokenType getType() {
        return type;
    }

    public void setType(MyLexer.TokenType type) {
        this.type = type;
    }
}
