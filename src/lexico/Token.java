package lexico;

public class Token {
    private String text;
    private TipoToken kind;

    public Token(String text, TipoToken kind) {
        this.text = text;
        this.kind = kind;
    }

    public String getText() {
        return text;
    }

    public TipoToken getKind() {
        return kind;
    }

    // Verifica se o texto é uma palavra-chave
    public static TipoToken verificarSePalavraChave(String tokenText) {
        for (TipoToken tipo : TipoToken.values()) {
            if (tipo.name().equals(tokenText) && tipo.getValue() >= 100 && tipo.getValue() < 200) {
                return tipo;
            }
        }
        return null;
    }
}
