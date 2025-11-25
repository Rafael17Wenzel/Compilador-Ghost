package ast;

public class AssignStmt extends Stmt {
    public final String name;
    public final Expr value;

    public AssignStmt(String name, Expr value) {
        this.name = name;
        this.value = value;
    }
}
