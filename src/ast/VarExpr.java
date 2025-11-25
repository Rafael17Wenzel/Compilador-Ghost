package ast;

public class VarExpr extends Expr {
    public final String name;

    public VarExpr(String name) {
        this.name = name;
    }
}