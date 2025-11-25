package semantic;

import lexer.Token;
import java.util.HashMap;
import java.util.Map;

public class SemanticAnalyzer {
    private final Map<String, String> table = new HashMap<>();

    public void declareVariable(String name, String type) {
        table.put(name, type);
    }

    public void checkVariableExists(String name) {
        if (!table.containsKey(name)) throw new RuntimeException("Erro semântico: variável '" + name + "' não declarada");
    }

    public void checkAssignment(String name, Token value) {
        checkVariableExists(name);
        String type = table.get(name);
        switch (value.type) {
            case NUMBER:
                if (!type.equals("int") && !type.equals("double"))
                    throw new RuntimeException("Erro semântico: tipo incompatível na atribuição para '" + name + "'");
                break;
            case STRING:
                if (!type.equals("string"))
                    throw new RuntimeException("Erro semântico: tipo incompatível na atribuição para '" + name + "'");
                break;
            case BOOL:
                if (!type.equals("bool"))
                    throw new RuntimeException("Erro semântico: tipo incompatível na atribuição para '" + name + "'");
                break;
            case IDENT:
                checkVariableExists(value.text);
                break;
            default:
                throw new RuntimeException("Erro semântico: tipo incompatível na atribuição para '" + name + "'");
        }
    }

    public void printSymbolTable() {
        System.out.println("=== TABELA DE SÍMBOLOS ===");
        table.forEach((k,v) -> System.out.println(k + " : " + v));
    }
}
