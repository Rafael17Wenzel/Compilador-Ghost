package application;

import lexico.AnalisadorLexico;
import lexico.TipoToken;
import lexico.Token;

public class Main {
    public static void main(String[] args) {
        String fonte = "If+ -123*Then/!=";
        fonte = fonte.toUpperCase();

        AnalisadorLexico anallexico = new AnalisadorLexico(fonte);
        Token token = anallexico.obterToken();

        while (token.getKind() != TipoToken.EOF) {
            System.out.println(token.getText() + "  ->  " + token.getKind());
            token = anallexico.obterToken();
        }
    }
}