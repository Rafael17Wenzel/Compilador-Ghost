package ast;

public class LiteralExpr extends Expr {
    public final Object value;

    public LiteralExpr(Object value) {
        this.value = value;
    }
}