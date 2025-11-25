package ast;

public class PrintStmt extends Stmt {
    public final Expr value;

    public PrintStmt(Expr value) {
        this.value = value;
    }
}

