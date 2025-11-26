# Compilador-Ghost
Compilador da Linguagem Ghost para C++

A linguagem Ghost é uma linguagem fictícia. Os detalhes de sua estrutura estão logo abaixo

## 🚀 Como executar

### ✔ Requisito
- **Git (para clonar o repositório)**
- **Java atual (recomendado Java 25)**

### ✔ Clonando e executando com Git
Em alguma pasta, abrir o terminal e executar:
```bash
git clone https://github.com/Rafael17Wenzel/Compilador-Ghost.git
```
```bash
cd Compilador-Ghost/src
```

```bash
java application.Main programa.txt
```

## Estrutura básica da linguagem Ghost 
#### Tipos primitivos
- int = int
- real = double
- text = std::string
- bool = bool

#### Declaração de variáveis
- let x: int = 10
- let nome: text = "Ana"

#### Condicionais
if x > 10 {
    print("Maior que 10")
} else {
    print("Menor ou igual a 10")
}

#### Loops
for i in 0..5 {
    print(i)
}

#### Entrada/Saída
let nome: text
input("Digite seu nome: ", nome)
print("Olá, " + nome)
