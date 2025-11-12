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

    public void parse() {
        while (peek().type != TokenType.EOF) {
            statement();
        }
    }

    private void statement() {
        Token token = peek();
        switch (token.type) {
            case LET:
                parseVarDecl();
                break;
            case IF:
                parseIf();
                break;
            case FOR:
                parseFor();
                break;
            case PRINT:
                parsePrint();
                break;
            case INPUT:
                parseInput();
                break;
            default:
                next(); // ignora por enquanto
                break;
        }
    }

    private void parseVarDecl() {
        next(); // let
        Token name = next();
        next(); // :
        Token type = next();

        String cppType;
        switch (type.text) {
            case "int": cppType = "int"; break;
            case "real": cppType = "double"; break;
            case "text": cppType = "string"; break;
            case "bool": cppType = "bool"; break;
            default: cppType = "auto"; break;
        }

        if (peek().type == TokenType.EQ) {
            next(); // =
            Token value = next();
            gen.emit(cppType + " " + name.text + " = " + formatValue(value) + ";");
        } else {
            gen.emit(cppType + " " + name.text + ";");
        }
    }

    private void parseIf() {
        next(); // if
        StringBuilder cond = new StringBuilder();
        while (!peek().type.equals(TokenType.LBRACE)) cond.append(next().text).append(" ");
        next(); // abre {
        gen.emit("if (" + cond.toString().trim() + ") {");

        while (!peek().type.equals(TokenType.RBRACE) && peek().type != TokenType.EOF) statement();
        next(); // fecha }

        if (peek().type == TokenType.ELSE) {
            next(); // else
            next(); // {
            gen.emit("} else {");
            while (!peek().type.equals(TokenType.RBRACE) && peek().type != TokenType.EOF) statement();
            next(); // fecha }
        }

        gen.emit("}");
    }

    private void parseFor() {
        next(); // for
        Token var = next(); // i
        next(); // in
        Token start = next(); // 0
        next(); // ..
        Token end = next(); // 5
        next(); // {

        gen.emit("for (int " + var.text + " = " + start.text + "; " + var.text + " < " + end.text + "; ++" + var.text + ") {");
        while (!peek().type.equals(TokenType.RBRACE) && peek().type != TokenType.EOF) statement();
        next(); // fecha }
        gen.emit("}");
    }

    private void parsePrint() {
        next(); // print
        next(); // (
        Token content = next();
        gen.emit("cout << " + formatValue(content) + " << endl;");
        next(); // )
    }

    private void parseInput() {
        next(); // input
        next(); // (
        Token prompt = next(); // STRING
        next(); // ,
        Token var = next(); // IDENT
        next(); // )

        gen.emit("cout << " + "\"" + prompt.text + "\" << endl;");
        gen.emit("cin >> " + var.text + ";");
    }

    private String formatValue(Token token) {
        if (token.type == TokenType.STRING) {
            return "\"" + token.text + "\"";
        } else {
            return token.text;
        }
    }
}
