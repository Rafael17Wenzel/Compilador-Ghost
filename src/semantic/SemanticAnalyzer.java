package semantic;

import ast.*;
import java.util.*;

public class SemanticAnalyzer {

    public enum Type {
        INT, REAL, TEXT, BOOL, UNKNOWN
    }

    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    public SemanticAnalyzer() {
        enterScope();
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        scopes.pop();
    }

    public void declare(String name, Type type) {
        Map<String, Type> current = scopes.peek();
        if (current.containsKey(name)) {
            error("Variável '" + name + "' já declarada neste escopo.");
        }
        current.put(name, type);
    }

    public Type resolve(String name) {
        for (Map<String, Type> scope : scopes) {
            if (scope.containsKey(name)) return scope.get(name);
        }
        error("Variável '" + name + "' não declarada.");
        return Type.UNKNOWN;
    }

    public void analyze(List<Stmt> program) {
        for (Stmt stmt : program) {
            analyzeStmt(stmt);
        }
    }

    private void analyzeStmt(Stmt stmt) {
        if (stmt instanceof VarDecl d) {
            Type varType = decodeType(d.type);

            declare(d.name, varType);

            if (d.initializer != null) {
                Type initType = analyzeExpr(d.initializer);
                checkAssignTypes(varType, initType);
            }
            return;
        }

        if (stmt instanceof AssignStmt a) {
            Type varType = resolve(a.name);
            Type valueType = analyzeExpr(a.value);
            checkAssignTypes(varType, valueType);
            return;
        }

        if (stmt instanceof PrintStmt p) {
            analyzeExpr(p.value);
            return;
        }

        if (stmt instanceof InputStmt i) {
            resolve(i.name);
            return;
        }

        if (stmt instanceof IfStmt ifs) {
            analyzeExpr(ifs.condition);

            enterScope();
            for (Stmt s : ifs.thenBlock) analyzeStmt(s);
            exitScope();

            if (ifs.elseBlock != null) {
                enterScope();
                for (Stmt s : ifs.elseBlock) analyzeStmt(s);
                exitScope();
            }
            return;
        }

        if (stmt instanceof ForStmt f) {

            enterScope();
            declare(f.var, Type.INT);

            analyzeExpr(f.start);
            analyzeExpr(f.end);

            for (Stmt s : f.body) analyzeStmt(s);
            exitScope();
            return;
        }

        if (stmt instanceof BlockStmt b) {
            enterScope();
            for (Stmt s : b.statements) analyzeStmt(s);
            exitScope();
            return;
        }
    }

    private Type analyzeExpr(Expr expr) {

        if (expr instanceof LiteralExpr lit) {
            if (lit.value instanceof Integer) return Type.INT;
            if (lit.value instanceof Double) return Type.REAL;
            if (lit.value instanceof Boolean) return Type.BOOL;
            if (lit.value instanceof String) return Type.TEXT;

            return Type.UNKNOWN;
        }

        if (expr instanceof VarExpr v) {
            return resolve(v.name);
        }

        if (expr instanceof BinaryExpr bin) {
            Type left = analyzeExpr(bin.left);
            Type right = analyzeExpr(bin.right);

            return checkBinary(left, right, bin.op);
        }

        return Type.UNKNOWN;
    }

    private Type checkBinary(Type a, Type b, String op) {
        if ((a == Type.INT || a == Type.REAL) &&
                (b == Type.INT || b == Type.REAL)) {

            if ("+".equals(op) || "-".equals(op) ||
                    "*".equals(op) || "/".equals(op)) {

                if (a == Type.REAL || b == Type.REAL) return Type.REAL;
                return Type.INT;
            }

            if (op.equals(">") || op.equals("<") ||
                    op.equals(">=") || op.equals("<=") ||
                    op.equals("==") || op.equals("!=")) {
                return Type.BOOL;
            }
        }

        if (a == Type.BOOL && b == Type.BOOL &&
                (op.equals("==") || op.equals("!="))) {
            return Type.BOOL;
        }

        error("Operação inválida: " + a + " " + op + " " + b);
        return Type.UNKNOWN;
    }

    private void checkAssignTypes(Type a, Type b) {
        if (a == b) return;
        if (a == Type.REAL && b == Type.INT) return;
        error("Atribuição inválida: esperado " + a + ", recebeu " + b);
    }

    private Type decodeType(String t) {
        return switch (t) {
            case "int" -> Type.INT;
            case "real" -> Type.REAL;
            case "text" -> Type.TEXT;
            case "bool" -> Type.BOOL;
            default -> Type.UNKNOWN;
        };
    }

    private void error(String msg) {
        throw new RuntimeException("Erro semântico: " + msg);
    }
}
