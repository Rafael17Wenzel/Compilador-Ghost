package ast;

import java.util.List;

public class ForStmt extends Stmt {
    public final String var;
    public final Expr start;
    public final Expr end;
    public final List<Stmt> body;

    public ForStmt(String var, Expr start, Expr end, List<Stmt> body) {
        this.var = var;
        this.start = start;
        this.end = end;
        this.body = body;
    }
}
