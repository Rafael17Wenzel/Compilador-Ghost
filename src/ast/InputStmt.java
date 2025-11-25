package ast;

public class InputStmt extends Stmt {
    public final String prompt;
    public final String name;

    public InputStmt(String prompt, String name) {
        this.prompt = prompt;
        this.name = name;
    }
}
