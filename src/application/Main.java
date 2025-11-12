package application;

import lexer.*;
import parser.*;
import codegen.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code =
            "let x: int = 10\n" +
            "if x > 5 {\n" +
            "    print(\"maior que 5\")\n" +
            "} else {\n" +
            "    print(\"menor ou igual a 5\")\n" +
            "}\n" +
            "for i in 0..3 {\n" +
            "    print(i)\n" +
            "}\n" +
            "let nome: text\n" +
            "input(\"Digite seu nome: \", nome)\n" +
            "print(\"Olá, \" + nome)\n";

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        System.out.println("=== TOKENS ===");
        for (Token t : tokens) {
            System.out.println(t);
        }

        CodeGenerator gen = new CodeGenerator();
        gen.start();

        Parser parser = new Parser(tokens, gen);
        parser.parse();

        gen.end();

        System.out.println("\n=== C++ GERADO ===\n");
        System.out.println(gen.getCode());
    }
}
