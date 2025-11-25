package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    private char peek() {
        return pos < input.length() ? input.charAt(pos) : '\0';
    }

    private char next() {
        return pos < input.length() ? input.charAt(pos++) : '\0';
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = peek();

            if (Character.isWhitespace(c)) {
                next();
                continue;
            }

            if (Character.isLetter(c)) {
                String word = readWhile(Character::isLetterOrDigit);
                tokens.add(makeKeywordOrIdent(word));
                continue;
            }

            if (Character.isDigit(c)) {
                String num = readWhile(Character::isDigit); // removido ponto
                tokens.add(new Token(TokenType.NUMBER, num));
                continue;
            }

            switch (c) {
                case '+': tokens.add(new Token(TokenType.PLUS, "+")); next(); break;
                case '-': tokens.add(new Token(TokenType.MINUS, "-")); next(); break;
                case '*': tokens.add(new Token(TokenType.STAR, "*")); next(); break;
                case '/': tokens.add(new Token(TokenType.SLASH, "/")); next(); break;
                case '(': tokens.add(new Token(TokenType.LPAREN, "(")); next(); break;
                case ')': tokens.add(new Token(TokenType.RPAREN, ")")); next(); break;
                case '{': tokens.add(new Token(TokenType.LBRACE, "{")); next(); break;
                case '}': tokens.add(new Token(TokenType.RBRACE, "}")); next(); break;
                case ':': tokens.add(new Token(TokenType.COLON, ":")); next(); break;
                case ',': tokens.add(new Token(TokenType.COMMA, ",")); next(); break;
                case '=':
                    next();
                    if (peek() == '=') { next(); tokens.add(new Token(TokenType.EQEQ, "==")); }
                    else tokens.add(new Token(TokenType.EQ, "="));
                    break;
                case '!':
                    next();
                    if (peek() == '=') { next(); tokens.add(new Token(TokenType.NOTEQ, "!=")); }
                    break;
                case '>':
                    next();
                    if (peek() == '=') { next(); tokens.add(new Token(TokenType.GTEQ, ">=")); }
                    else tokens.add(new Token(TokenType.GT, ">"));
                    break;
                case '<':
                    next();
                    if (peek() == '=') { next(); tokens.add(new Token(TokenType.LTEQ, "<=")); }
                    else tokens.add(new Token(TokenType.LT, "<"));
                    break;
                case '"':
                    next();
                    StringBuilder str = new StringBuilder();
                    while (peek() != '"' && peek() != '\0') str.append(next());
                    next(); // fecha aspas
                    tokens.add(new Token(TokenType.STRING, str.toString()));
                    break;
                case '.':
                    next();
                    if (peek() == '.') { next(); tokens.add(new Token(TokenType.DOTS, "..")); }
                    else tokens.add(new Token(TokenType.DOT, "."));
                    break;
                default:
                    next(); // ignora caracteres inválidos
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private String readWhile(java.util.function.Predicate<Character> cond) {
        StringBuilder sb = new StringBuilder();
        while (cond.test(peek())) sb.append(next());
        return sb.toString();
    }

    private Token makeKeywordOrIdent(String word) {
        switch (word) {
            case "let":   return new Token(TokenType.LET, word);
            case "int":   return new Token(TokenType.INT, word);
            case "real":  return new Token(TokenType.REAL, word);
            case "text":  return new Token(TokenType.TEXT, word);
            case "bool":  return new Token(TokenType.BOOL_TYPE, word);
            case "if":    return new Token(TokenType.IF, word);
            case "else":  return new Token(TokenType.ELSE, word);
            case "for":   return new Token(TokenType.FOR, word);
            case "in":    return new Token(TokenType.IN, word);
            case "input": return new Token(TokenType.INPUT, word);
            case "print": return new Token(TokenType.PRINT, word);
            case "true":
            case "false": return new Token(TokenType.BOOL, word);
            default:      return new Token(TokenType.IDENT, word);
        }
    }
}
