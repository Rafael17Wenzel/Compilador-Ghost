package application;

import lexer.*;
import parser.*;
import codegen.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Se o usuário não passar um arquivo, mostra ajuda
        if (args.length == 0) {
            System.out.println("Uso: java -jar compilador.jar <arquivo>");
            return;
        }

        String filename = args[0];
        String code = "";

        try {
            code = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + filename);
            e.printStackTrace();
            return;
        }

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
