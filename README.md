# Compilador-Ghost
Compilador da Linguagem Ghost para C++

Estrutura básica da linguagem Ghost
1. Tipos primitivos
int = int
real = double
text = std::string
bool = bool

2. Declaração de variáveis
let x: int = 10
let nome: text = "Ana"

3. Condicionais
if x > 10 {
    print("Maior que 10")
} else {
    print("Menor ou igual a 10")
}

4. Loops
for i in 0..5 {
    print(i)
}

5. Entrada/Saída
let nome: text
input("Digite seu nome: ", nome)
print("Olá, " + nome)



##########################################

Gramática simplificada (pseudo-EBNF)

Program        ::= { Statement } EOF

Statement      ::= VarDecl
                 | Assignment
                 | IfStmt
                 | ForStmt
                 | InputStmt
                 | PrintStmt
                 | ε                (* linha vazia *)

VarDecl        ::= "let" IDENT ":" Type [ "=" Expression ]

Assignment     ::= IDENT "=" Expression

Type           ::= "int" | "real" | "text" | "bool"

IfStmt         ::= "if" Expression Block [ "else" Block ]

ForStmt        ::= "for" IDENT "in" Expression ".." Expression Block

InputStmt      ::= "input" "(" STRING "," IDENT ")"

PrintStmt      ::= "print" "(" Expression ")"

Block          ::= "{" { Statement } "}"

Expression     ::= Equality

Equality       ::= Comparison { ( "==" | "!=" ) Comparison }

Comparison     ::= Term { ( ">" | ">=" | "<" | "<=" ) Term }

Term           ::= Factor { ( "+" | "-" ) Factor }

Factor         ::= Unary { ( "*" | "/" ) Unary }

Unary          ::= [ "-" | "+" ] Primary

Primary        ::= NUMBER
                 | STRING
                 | "true"
                 | "false"
                 | IDENT
                 | "(" Expression ")"

IDENT          ::= Letter { Letter | Digit | "_" }
NUMBER         ::= Digit { Digit } [ "." Digit { Digit } ]
STRING         ::= '"' { Character } '"'
