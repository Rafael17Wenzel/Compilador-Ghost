package parser;

import lexer.*;
import codegen.*;
import semantic.SemanticAnalyzer;
import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;
    private final CodeGenerator gen;
    private final SemanticAnalyzer sem;

    public Parser(List<Token> tokens, CodeGenerator gen) {
        this.tokens = tokens;
        this.gen = gen;
        this.sem = new SemanticAnalyzer();
    }

    private Token peek() { return pos < tokens.size() ? tokens.get(pos) : new Token(TokenType.EOF, ""); }
    private Token next() { return pos < tokens.size() ? tokens.get(pos++) : new Token(TokenType.EOF, ""); }

    public void parse() {
        while (peek().type != TokenType.EOF) statement();
        sem.printSymbolTable();
    }

    private void statement() {
        Token token = peek();
        switch (token.type) {
            case LET: parseVarDecl(); break;
            case IDENT: parseIdent(); break;
            case IF: parseIf(); break;
            case FOR: parseFor(); break;
            case PRINT: parsePrint(); break;
            case INPUT: parseInput(); break;
            default: next(); break;
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

        sem.declareVariable(name.text, cppType);

        if (peek().type == TokenType.EQ) {
            next();
            Token value = next();
            sem.checkAssignment(name.text, value);
            gen.emit(cppType + " " + name.text + " = " + formatValue(value) + ";");
        } else {
            gen.emit(cppType + " " + name.text + ";");
        }
    }

    private void parseIdent() {
        Token name = next();
        next(); // =
        Token value = next();
        sem.checkAssignment(name.text, value);
        gen.emit(name.text + " = " + formatValue(value) + ";");
    }

    private void parseIf() {
        next(); // if
        StringBuilder cond = new StringBuilder();
        while (!peek().type.equals(TokenType.LBRACE)) cond.append(next().text).append(" ");
        next(); // {
        gen.emit("if (" + cond.toString().trim() + ") {");

        while (!peek().type.equals(TokenType.RBRACE) && peek().type != TokenType.EOF) statement();
        next(); // }

        if (peek().type == TokenType.ELSE) {
            next(); next(); // {
            gen.emit("} else {");
            while (!peek().type.equals(TokenType.RBRACE) && peek().type != TokenType.EOF) statement();
            next(); // }
        }

        gen.emit("}");
    }

    private void parseFor() {
        next(); // for
        Token var = next();
        next(); // in
        Token start = next(); // NUMBER
        next(); // DOTS
        Token end = next();   // NUMBER

        sem.declareVariable(var.text, "int");
        gen.emit("for (int " + var.text + " = " + start.text + "; " + var.text + " < " + end.text + "; " + var.text + "++) {");

        next(); // {
        while (!peek().type.equals(TokenType.RBRACE) && peek().type != TokenType.EOF) statement();
        next(); // }
        gen.emit("}");
    }

    private void parsePrint() {
        next(); // print
        next(); // (
        Token content = next();
        gen.emit("cout << " + formatValue(content));
        while(peek().type == TokenType.PLUS) { next(); gen.emit(" << " + formatValue(next())); }
        gen.emit(" << endl;");
        next(); // )
    }

    private void parseInput() {
        next(); // input
        next(); // (
        Token prompt = next(); // STRING
        next(); // COMMA
        Token var = next();    // IDENT
        sem.checkVariableExists(var.text);
        next(); // )

        gen.emit("cout << \"" + prompt.text + "\";");
        gen.emit("cin >> " + var.text + ";");
    }

    private String formatValue(Token token) {
        if (token.type == TokenType.STRING) return "\"" + token.text + "\"";
        if (token.type == TokenType.IDENT) { sem.checkVariableExists(token.text); return token.text; }
        return token.text;
    }
}
