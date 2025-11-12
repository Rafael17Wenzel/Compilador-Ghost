package lexico;

public enum TipoToken {
    EOF(-1),                 // Fim do código
    NEWLINE(0),       // Linha nova
    NUMBER(1),        // Número literal
    IDENT(2),         // Nome de variável/função
    STRING(3),        // Texto entre aspas

    // Palavras-chave
    LABEL(101),
    GOTO(102),
    PRINT(103),
    INPUT(104),
    LET(105),
    IF(106),
    THEN(107),
    ENDIF(108),
    WHILE(109),
    REPEAT(110),
    ENDWHILE(111),

    // Operadores
    EQ(201),
    PLUS(202),
    MINUS(203),
    ASTERISK(204),
    SLASH(205),
    EQEQ(206),
    NOTEQ(207),
    LT(208),
    LTEQ(209),
    GT(210),
    GTEQ(211);

    private final int value;

    TipoToken(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
