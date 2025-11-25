package ast;

import java.util.List;

public class IfStmt extends Stmt {
    public final Expr condition;
    public final List<Stmt> thenBlock;
    public final List<Stmt> elseBlock;

    public IfStmt(Expr condition, List<Stmt> thenBlock, List<Stmt> elseBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }
}
