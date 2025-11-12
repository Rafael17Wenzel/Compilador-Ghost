package lexico;

public class AnalisadorLexico {
    private String source;
    private char curChar;
    private int curPos;

    public AnalisadorLexico(String source) {
        this.source = source + "\n";
        this.curChar = '\0';
        this.curPos = -1;
        proximoCaractere();
    }

    private void proximoCaractere() {
        curPos++;
        if (curPos >= source.length()) {
            curChar = '\0';
        } else {
            curChar = source.charAt(curPos);
        }
    }

    private char pesquisar() {
        if (curPos + 1 >= source.length()) return '\0';
        return source.charAt(curPos + 1);
    }

    private void aborta(String message) {
        throw new RuntimeException("Erro do Analisador Léxico: " + message);
    }

    private void pularEspacoBranco() {
        while (curChar == ' ' || curChar == '\t' || curChar == '\r') {
            proximoCaractere();
        }
    }

    private void pularComentario() {
        if (curChar == '#') {
            while (curChar != '\n' && curChar != '\0') {
                proximoCaractere();
            }
        }
    }

    public Token obterToken() {
        pularEspacoBranco();
        pularComentario();
        Token token = null;

        if (curChar == '+')
            token = new Token(String.valueOf(curChar), TipoToken.PLUS);
        else if (curChar == '-')
            token = new Token(String.valueOf(curChar), TipoToken.MINUS);
        else if (curChar == '*')
            token = new Token(String.valueOf(curChar), TipoToken.ASTERISK);
        else if (curChar == '/')
            token = new Token(String.valueOf(curChar), TipoToken.SLASH);
        else if (curChar == '=') {
            if (pesquisar() == '=') {
                char last = curChar;
                proximoCaractere();
                token = new Token("" + last + curChar, TipoToken.EQEQ);
            } else token = new Token(String.valueOf(curChar), TipoToken.EQ);
        }
        else if (curChar == '>') {
            if (pesquisar() == '=') {
                char last = curChar;
                proximoCaractere();
                token = new Token("" + last + curChar, TipoToken.GTEQ);
            } else token = new Token(String.valueOf(curChar), TipoToken.GT);
        }
        else if (curChar == '<') {
            if (pesquisar() == '=') {
                char last = curChar;
                proximoCaractere();
                token = new Token("" + last + curChar, TipoToken.LTEQ);
            } else token = new Token(String.valueOf(curChar), TipoToken.LT);
        }
        else if (curChar == '!') {
            if (pesquisar() == '=') {
                char last = curChar;
                proximoCaractere();
                token = new Token("" + last + curChar, TipoToken.NOTEQ);
            } else aborta("Caractere esperado !=, obtido !" + pesquisar());
        }
        else if (curChar == '"') {
            proximoCaractere();
            int startPos = curPos;

            while (curChar != '"' && curChar != '\0') {
                if (curChar == '\r' || curChar == '\n' || curChar == '\t' || curChar == '\\' || curChar == '%')
                    aborta("Caractere ilegal na String/Texto.");
                proximoCaractere();
            }

            String tokText = source.substring(startPos, curPos);
            token = new Token(tokText, TipoToken.STRING);
        }
        else if (Character.isDigit(curChar)) {
            int startPos = curPos;
            while (Character.isDigit(pesquisar())) proximoCaractere();

            if (pesquisar() == '.') {
                proximoCaractere();
                if (!Character.isDigit(pesquisar()))
                    aborta("Caractere ilegal no número.");

                while (Character.isDigit(pesquisar()))
                    proximoCaractere();
            }

            String tokText = source.substring(startPos, curPos + 1);
            token = new Token(tokText, TipoToken.NUMBER);
        }
        else if (Character.isAlphabetic(curChar)) {
            int startPos = curPos;
            while (Character.isLetterOrDigit(pesquisar())) proximoCaractere();

            String tokText = source.substring(startPos, curPos + 1);
            TipoToken keyword = Token.verificarSePalavraChave(tokText);

            if (keyword == null)
                token = new Token(tokText, TipoToken.IDENT);
            else
                token = new Token(tokText, keyword);
        }
        else if (curChar == '\n') {
            token = new Token("\\n", TipoToken.NEWLINE);
        }
        else if (curChar == '\0') {
            token = new Token("\\0", TipoToken.EOF);
        }
        else {
            aborta("Token desconhecido: " + curChar);
        }

        proximoCaractere();
        return token;
    }
}
