package parser;

import lexer.*;
import codegen.*;
import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;
    private final CodeGenerator gen;

    public Parser(List<Token> tokens, CodeGenerator gen) {
        this.tokens = tokens;
        this.gen = gen;
    }

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : new Token(TokenType.EOF, "");
    }

    private Token next() {
        return pos < tokens.size() ? tokens.get(pos++) : new Token(TokenType.EOF, "");
    }

    private void expect(TokenType type, String message) {
        if (peek().type != type) {
            throw new RuntimeException(message + ", encontrado: " + peek().text);
        }
        next();
    }

    public void parse() {
        while (peek().type != TokenType.EOF) {
            statement();
        }
    }

    private void statement() {
        switch (peek().type) {
            case LET: parseVarDecl(); break;
            case IDENT: parseAssignment(); break;
            case IF: parseIf(); break;
            case FOR: parseFor(); break;
            case PRINT: parsePrint(); break;
            case INPUT: parseInput(); break;
            default: next(); break;
        }
    }

    private void parseVarDecl() {
        next(); // let
        Token name = next(); // nome
        expect(TokenType.COLON, "Esperado ':'");
        Token type = next(); // tipo

        String cppType = switch (type.type) {
            case INT -> "int";
            case REAL -> "double";
            case TEXT -> "string";
            case BOOL_TYPE -> "bool";
            default -> "auto";
        };

        if (peek().type == TokenType.EQ) {
            next();
            String value = parseExpression();
            gen.emit(cppType + " " + name.text + " = " + value + ";");
        } else {
            gen.emit(cppType + " " + name.text + ";");
        }
    }

    private void parseAssignment() {
        Token name = next();
        expect(TokenType.EQ, "Esperado '='");
        String value = parseExpression();
        gen.emit(name.text + " = " + value + ";");
    }

    private void parseIf() {
        next(); // if
        String condition = parseExpression();
        expect(TokenType.LBRACE, "Esperado '{'");
        gen.emit("if (" + condition + ") {");

        while (peek().type != TokenType.RBRACE && peek().type != TokenType.EOF) {
            statement();
        }
        expect(TokenType.RBRACE, "Esperado '}'");

        if (peek().type == TokenType.ELSE) {
            next();
            expect(TokenType.LBRACE, "Esperado '{' após else");
            gen.emit("} else {");
            while (peek().type != TokenType.RBRACE && peek().type != TokenType.EOF) {
                statement();
            }
            expect(TokenType.RBRACE, "Esperado '}' após else");
        }

        gen.emit("}");
    }

    private void parseFor() {
        next(); // for
        Token var = next();
        expect(TokenType.IN, "Esperado 'in'");
        Token start = next();
        expect(TokenType.DOTS, "Esperado '..'");
        Token end = next();

        gen.emit("for (int " + var.text + " = " + start.text + "; " + var.text + " <= " + end.text + "; " + var.text + "++) {");
        expect(TokenType.LBRACE, "Esperado '{'");
        while (peek().type != TokenType.RBRACE && peek().type != TokenType.EOF) {
            statement();
        }
        expect(TokenType.RBRACE, "Esperado '}'");
        gen.emit("}");
    }

    private void parsePrint() {
        next(); // print
        expect(TokenType.LPAREN, "Esperado '('");
        StringBuilder expr = new StringBuilder();
        expr.append(parseExpression());
        while (peek().type == TokenType.PLUS) {
            next();
            expr.append(" << ").append(parseExpression());
        }
        expect(TokenType.RPAREN, "Esperado ')'");
        gen.emit("cout << " + expr + " << endl;");
    }

    private void parseInput() {
        next(); // input
        expect(TokenType.LPAREN, "Esperado '('");
        Token prompt = next();
        expect(TokenType.COMMA, "Esperado ','");
        Token var = next();
        expect(TokenType.RPAREN, "Esperado ')'");
        gen.emit("cout << " + formatValue(prompt) + ";");
        gen.emit("cin >> " + var.text + ";");
    }

    private String parseExpression() {
        StringBuilder sb = new StringBuilder();
        Token token = peek();

        if (token.type == TokenType.STRING || token.type == TokenType.BOOL || token.type == TokenType.NUMBER || token.type == TokenType.IDENT) {
            sb.append(formatValue(next()));
        } else if (token.type == TokenType.LPAREN) {
            next();
            sb.append("(").append(parseExpression()).append(")");
            expect(TokenType.RPAREN, "Esperado ')'");
        } else {
            throw new RuntimeException("Expressão inesperada: " + token.text);
        }

        while (isOperator(peek().type)) {
            sb.append(" ").append(next().text).append(" ").append(parseExpression());
        }

        return sb.toString();
    }

    private boolean isOperator(TokenType type) {
        return switch (type) {
            case PLUS, MINUS, STAR, SLASH, EQEQ, NOTEQ, GT, GTEQ, LT, LTEQ -> true;
            default -> false;
        };
    }

    private String formatValue(Token token) {
        return switch (token.type) {
            case STRING -> "\"" + token.text + "\"";
            case BOOL -> token.text;
            default -> token.text;
        };
    }
}
