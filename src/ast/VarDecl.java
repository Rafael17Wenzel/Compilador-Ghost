package ast;

public class VarDecl extends Stmt {
    public final String name;
    public final String type;
    public final Expr initializer;

    public VarDecl(String name, String type, Expr initializer) {
        this.name = name;
        this.type = type;
        this.initializer = initializer;
    }
}
