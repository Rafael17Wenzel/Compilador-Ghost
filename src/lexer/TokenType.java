package lexer;

public enum TokenType {
    EOF, IDENT, NUMBER, STRING, BOOL,

    // Tipos
    INT, REAL, TEXT, BOOL_TYPE,

    // Palavras-chave
    LET, IF, ELSE, FOR, IN, INPUT, PRINT,

    // Operadores e símbolos
    PLUS, MINUS, STAR, SLASH,
    EQ, EQEQ, NOTEQ, GT, GTEQ, LT, LTEQ,
    COLON, DOTS, LPAREN, RPAREN, LBRACE, RBRACE,
    COMMA, DOT
}
